package cc.wanshan.gisdev.utils;

import cc.wanshan.gisdev.common.enums.ResultCode;
import cc.wanshan.gisdev.entity.Geoserver;
import cc.wanshan.gisdev.entity.Result;
import com.google.common.collect.Lists;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.decoder.RESTLayerGroup;
import it.geosolutions.geoserver.rest.decoder.RESTLayerList;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Component
public class GeoserverUtils {
  private static final Logger log = LoggerFactory.getLogger(GeoserverUtils.class);
  private static Geoserver geoserver;
  @Resource
  public  void setGeoserver(Geoserver geoserver) {
    GeoserverUtils.geoserver = geoserver;
  }
  public static URL url;
  public static GeoServerRESTManager manager;
  public static GeoServerRESTReader reader;
  private static GeoServerRESTPublisher publisher;

  /** 初始化，发布时进行身份认证 */

  @PostConstruct
  public static GeoServerRESTManager getGeoserverManager() {
    log.info("getGeoserverManager::");
    if (manager==null){
      synchronized (GeoserverUtils.class){
        if (manager==null){
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
   * @param uri 工作空间uri，可为空
   * @return
   * @throws URISyntaxException
   */
  public static boolean createWorkSpace(String workSpaceName, String uri)
      throws URISyntaxException {
    if (uri == null || uri.isEmpty()) {
      return publisher.createWorkspace(workSpaceName);
    } else {
      return publisher.createWorkspace(workSpaceName, new URI(uri));
    }
  }
  public static Result createDataStore(String storeName, String workspace) {
    log.info("已进入createDataStore::manager = [{}], storeName = [{}], workspace = [{}]",manager, storeName, workspace);
    //判断数据存储（datastore）是否已经存在，不存在则创建
    if (manager != null &&  storeName != null && !"".equals(storeName)) {
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
        }else {
          log.warn("存储点创建失败：createDataStore::manager = [{}], storeName = [{}], workspace = [{}]",manager, storeName, workspace);
          return ResultUtil.error(3,"存储点"+storeName+"创建失败");
        }
      } else {
        log.warn("存储点已存在:createDataStore::manager = [{}], storeName = [{}], workspace = [{}]",manager, storeName, workspace);
        return ResultUtil.error(1,"存储点"+storeName+"已存在");
      }
    }else {
      log.warn("空指针异常:createDataStore::manager = [{}], storeName = [{}], workspace = [{}]",manager, storeName, workspace);
      return ResultUtil.error(2, "空指针异常");
    }
  }
  public Result publishLayer( String ws, String storeName, String tableName) {
    log.info("已进入：publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", ws, storeName, tableName);
    if (ws != null && !"".equals(ws) && storeName != null && !"".equals(storeName) && tableName != null && !"".equals(tableName)) {
      Result dataStore = createDataStore(storeName,ws);
      if (dataStore.getCode() == 0||dataStore.getCode() == 1) {
        RESTLayer layer = manager.getReader().getLayer(ws, tableName);
        if (layer == null) {
          GSFeatureTypeEncoder pds = new GSFeatureTypeEncoder();
          pds.setTitle(tableName);
          pds.setName(tableName);
          pds.setSRS("EPSG:4326");
          GSLayerEncoder layerEncoder = new GSLayerEncoder();
          boolean publishDBLayer = manager.getPublisher().publishDBLayer(ws, storeName, pds, layerEncoder);
          if (publishDBLayer){
            return ResultUtil.success();
          }else {
            log.warn("publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]",manager, ws, storeName, tableName);
            return ResultUtil.error(3,"发布失败");
          }
        } else {
          log.warn("图层已存在：publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]",manager, ws, storeName, tableName);
          return ResultUtil.error(1,"图层已存在");
        }
      }else {
        return dataStore;
      }
    }else {
      log.warn("空指针异常：publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]",manager, ws, storeName, tableName);
      return ResultUtil.error(2,"空指针异常");
    }
  }
  /**
   * 发布shp服务
   *
   * @param zipFilePath 文件路径
   * @param workspace 工作空间名称
   * @param storeName 存储源名称
   * @param layerName 图层名称
   * @param srs 参考系
   * @param defaultStyle 图层样式
   * @return
   * @throws Exception
   */
  public static Result publishShp(
      String zipFilePath,
      String workspace,
      String storeName,
      String layerName,
      String srs,
      String defaultStyle)
      throws Exception {
    // 坐标系,判断是否为空
    if (srs == null || srs.isEmpty()) {
      srs = GeoServerRESTPublisher.DEFAULT_CRS;
    }

    File zipFile = new File(zipFilePath);
    boolean published;
    if (defaultStyle == null) {
      published = publisher.publishShp(workspace, storeName, layerName, zipFile, srs);
    } else {
      published = publisher.publishShp(workspace, storeName, layerName, zipFile, srs, defaultStyle);
    }
    if (published) {
      return ResultUtil.success();
    }
    return ResultUtil.error(ResultCode.SHP_PUBLISH_FAIL);
  }

  /**
   * 发布tif数据服务
   *
   * @param fileName 文件路径
   * @param workspace 工作空间名称
   * @param storeName 存储源名称
   * @param layerName 图层名称
   * @param srs 参考系
   * @param defaultStyle 图层样式
   * @return
   * @throws Exception
   */
  public static boolean publishGeoTiff(
      String fileName,
      String workspace,
      String storeName,
      String layerName,
      String srs,
      String defaultStyle,
      double[] bbox)
      throws Exception {
    File geotiff = new File(fileName);
    if (defaultStyle == null || defaultStyle.isEmpty()) {
      return publisher.publishGeoTIFF(workspace, storeName, layerName, geotiff);
    } else {
      return publisher.publishGeoTIFF(
          workspace,
          storeName,
          layerName,
          geotiff,
          srs,
          GSResourceEncoder.ProjectionPolicy.FORCE_DECLARED,
          defaultStyle,
          bbox);
    }
  }

  /** 显示所有发布图层 */
  public List<String> getAllLayers() {
    RESTLayerList layers = reader.getLayers();
    List<String> layerNames = Lists.newArrayList();
    for (int i = 0; i < layers.size(); i++) {
      String layer = layers.get(i).getName();
      layerNames.add(layer);
    }
    return layerNames;
  }

  /** 查找图层 */
  public boolean findLayer(String workspace, String layerName) {
    RESTLayer layer = reader.getLayer(workspace, layerName);
    return (layer == null) ? false : true;
  }

  public boolean findLayerGroup(String layerGroupName) {
    RESTLayerGroup layerGroup = reader.getLayerGroup(layerGroupName);
    return (layerGroup == null) ? false : true;
  }

  public List<String> getLayerNames(String layerGroupName) {
    RESTLayerGroup layerGroup = reader.getLayerGroup(layerGroupName);
    return layerGroup.getPublishedList().getNames();
  }
}
