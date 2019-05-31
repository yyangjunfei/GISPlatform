package cc.wanshan.gis.utils;

import cc.wanshan.gis.entity.Geoserver;
import cc.wanshan.gis.entity.Result;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Component
public class GeoserverUtils {
    private static final Logger log = LoggerFactory.getLogger(GeoserverUtils.class);
    private static Geoserver geoserver;

    @Resource
    public void setGeoserver(Geoserver geoserver) {
        GeoserverUtils.geoserver = geoserver;
    }

    public static URL url;
    public static GeoServerRESTManager manager;
    public static GeoServerRESTReader reader;
    public static GeoServerRESTPublisher publisher;

    /**
     * 初始化，发布时进行身份认证
     */

    @PostConstruct
    public static GeoServerRESTManager getGeoserverManager() {
        log.info("getGeoserverManager::");
        if (manager == null) {
            synchronized (GeoserverUtils.class) {
                if (manager == null) {
                    try {
                        url = new URL(geoserver.getUrl());
                        manager = new GeoServerRESTManager(url, geoserver.getUsername(), geoserver.getPasswd());
                    } catch (MalformedURLException e) {
                        log.warn("getGeoserverManager::创建URL失败");
                    }
                }
            }
        }
        return manager;
    }


    /**
     * 创建工作空间
     *
     * @param workSpaceName 工作空间名称
     * @param uri           工作空间uri，可为空
     * @return
     * @throws URISyntaxException
     */
    public boolean createWorkSpace(String workSpaceName, String uri)
            throws URISyntaxException {
        if (uri == null || uri.isEmpty()) {
            return manager.getPublisher().createWorkspace(workSpaceName);
        } else {
            return manager.getPublisher().createWorkspace(workSpaceName, new URI(uri));
        }
    }

    public Result createDataStore(String storeName, String workspace) {
        log.info("已进入createDataStore::manager = [{}], storeName = [{}], workspace = [{}]", manager, storeName, workspace);
        //判断数据存储（datastore）是否已经存在，不存在则创建
        if (manager != null && storeName != null && !"".equals(storeName)) {
            RESTDataStore restStore = manager.getReader().getDatastore(workspace, storeName);
            if (restStore == null) {
                GSPostGISDatastoreEncoder store = new GSPostGISDatastoreEncoder(storeName);
                //设置url
                store.setHost(geoserver.getPostgisHost());
                //设置端口
                store.setPort(geoserver.getPostgisPort());
                // 数据库的用户名
                store.setUser(geoserver.getPostgisUser());
                // 数据库的密码
                store.setPassword(geoserver.getPostgisPassword());
                // 那个数据库;
                store.setDatabase(geoserver.getPostgisDatabase());
                //当前先默认使用public这个schema
                store.setSchema(workspace);
                // 超时设置
                store.setConnectionTimeout(20);
                // 最大连接数
                store.setMaxConnections(20);
                // 最小连接数
                store.setMinConnections(1);
                store.setExposePrimaryKeys(true);
                if (manager.getStoreManager().create(workspace, store)) {
                    return ResultUtil.success();
                } else {
                    log.warn("存储点创建失败：createDataStore::manager = [{}], storeName = [{}], workspace = [{}]", storeName, workspace);
                    return ResultUtil.error(3, "存储点" + storeName + "创建失败");
                }
            } else {
                log.warn("存储点已存在:createDataStore::manager = [{}], storeName = [{}], workspace = [{}]", storeName, workspace);
                return ResultUtil.error(1, "存储点" + storeName + "已存在");
            }
        } else {
            log.warn("空指针异常:createDataStore::manager = [{}], storeName = [{}], workspace = [{}]", storeName, workspace);
            return ResultUtil.error(2, "空指针异常");
        }
    }

    public Result publishLayer(String ws, String storeName, String tableName) {
        log.info("已进入：publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", ws, storeName, tableName);
        if (ws != null && !"".equals(ws) && storeName != null && !"".equals(storeName) && tableName != null && !"".equals(tableName)) {
            Result dataStore = createDataStore(storeName, ws);
            if (dataStore.getCode() == 0 || dataStore.getCode() == 1) {
                RESTLayer layer = manager.getReader().getLayer(ws, tableName);
                if (layer == null) {
                    GSFeatureTypeEncoder pds = new GSFeatureTypeEncoder();
                    pds.setTitle(tableName);
                    pds.setName(tableName);
                    pds.setSRS("EPSG:3857");
                    GSLayerEncoder layerEncoder = new GSLayerEncoder();
                    boolean publishDBLayer = manager.getPublisher().publishDBLayer(ws, storeName, pds, layerEncoder);
                    if (publishDBLayer) {
                        return ResultUtil.success();
                    } else {
                        log.warn("publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", manager, ws, storeName, tableName);
                        return ResultUtil.error(3, "发布失败");
                    }
                } else {
                    log.warn("图层已存在：publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", manager, ws, storeName, tableName);
                    return ResultUtil.error(1, "图层已存在");
                }
            } else {
                return dataStore;
            }
        } else {
            log.warn("空指针异常：publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", manager, ws, storeName, tableName);
            return ResultUtil.error(2, "空指针异常");
        }
    }

    /**
     * 获取WMTS  GeoServer 的连接
     *
     * @param style
     * @param layerGroupName
     * @param srs_epsg
     * @return
     */
    public static String getWmtsUrl(String style, String layerGroupName, String srs_epsg) {

        String wmtsUrl = geoserver.getUrl() + "gwc/service/wmts?layer=#layergroupName#&style=#style#&tilematrixset=#srs_epsg#&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image/png&TileMatrix=#srs_epsg#:{TileMatrix}&TileCol={TileCol}&TileRow={TileRow}&TRANSPARENT=true";
        return wmtsUrl.replaceAll("#layergroupName#", layerGroupName).replaceAll("#srs_epsg#", srs_epsg == null ? "EPSG:900913" : srs_epsg).replaceAll("#style#", style == null ? "" : style);
    }

    /**
     * 获取WMS  GeoServer 的连接
     *
     * @param workspaceName
     * @param layerGroupName
     * @param srs_epsg
     * @return
     */

    public static String getWmsUrl(String workspaceName, String layerGroupName, String srs_epsg) {

        String url = geoserver.getUrl() + "/wms?service=WMS&version=1.1.0&request=GetMap&layers=#layergroupName&srs=#srs_epsg&format=image/jpeg";
        String replace = url.replaceAll("#layergroupName", layerGroupName).replaceAll("#srs_epsg", srs_epsg);
        return replace;
    }

    /**
     * 获取WFS  GeoServer 的连接
     *
     * @param workSpace
     * @param layerName
     * @return
     */
    public static String getWfsUrl(String workSpace, String layerName) {
        String wfsUrl = geoserver.getUrl() + "/#workSpace#/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=#workSpace#:#layerName#&outputFormat=application%2Fjson";
        return wfsUrl.replaceAll("#workSpace#", workSpace).replaceAll("#layerName#", layerName);
    }
}
