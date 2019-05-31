package cc.wanshan.gis.service.geoserver.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.dao.createschema.CreateSchemaDao;
import cc.wanshan.gis.dao.searchschema.SearchSchemaDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Store;
import cc.wanshan.gis.entity.usermanagement.User;
import cc.wanshan.gis.service.geoserver.GeoserverService;
import cc.wanshan.gis.service.store.StoreService;
import cc.wanshan.gis.service.user.UserService;
import cc.wanshan.gis.utils.GeoserverUtils;
import cc.wanshan.gis.utils.ResultUtil;
import com.google.common.collect.Lists;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.decoder.RESTLayerGroup;
import it.geosolutions.geoserver.rest.decoder.RESTLayerList;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

@Service("geoserverServiceImpl")
public class GeoserverServiceImpl implements GeoserverService {

    private static final Logger logger = LoggerFactory.getLogger(GeoserverServiceImpl.class);
    private GeoServerRESTManager manager = GeoserverUtils.manager;
    @Resource
    private GeoserverUtils geoserverUtils;
    @Resource(name = "storeServiceImpl")
    private StoreService storeService;
    @Resource(name = "userServiceImpl")
    private UserService userService;
    @Resource(name = "createSchemaDaoImpl")
    private CreateSchemaDao createSchemaDao;
    @Resource(name = "searchSchemaDaoImpl")
    private SearchSchemaDao searchSchemaDao;

    @Override
    public Result creatWorkspace(String workspace) throws URISyntaxException {
        logger.info("creatWorkspace::workspace = [{}]", workspace);
        Result searchSchema = searchSchemaDao.searchSchema(workspace);
        if (searchSchema.getCode() == 1) {
            Result schema = createSchemaDao.createSchema(workspace);
            if (schema.getCode() == 0) {
                boolean workSpace = geoserverUtils.createWorkSpace(workspace, workspace);
                if (workSpace) {
                    Result newStore = geoserverUtils.createDataStore("newStore", workspace);
                    if (newStore.getCode() == 0) {
                        Store store = new Store();
                        User user = userService.findUserByUsername(workspace);
                        System.out.println("user为" + user.getUserId());
                        store.setStoreName("newStore");
                        store.setUser(user);
                        Boolean save = storeService.insertStore(store);
                        if (save) {
                            return ResultUtil.success();
                        } else {
                            logger.warn("插入store失败", save.toString());
                            return ResultUtil.error(1, "插入store失败");
                        }
                    } else {
                        logger.warn("创建store失败", newStore.toString());
                        return newStore;
                    }
                } else {
                    logger.warn("创建工作空间失败");
                    return ResultUtil.error(1, "创建工作空间失败");
                }
            } else {
                logger.warn("创建schema失败", schema.toString());
                return schema;
            }
        } else {
            logger.warn(workspace + "workspace已存在");
            return ResultUtil.error(1, "workspace已存在");
        }
    }

    @Override
    public Boolean searchLayer(String thematicName, String layerName) {
        RESTLayer layer = manager.getReader().getLayer(thematicName, layerName);
        return layer != null;
    }

    @Override
    public Boolean deleteLayer(String thematicName, String storeName, String layerName) {
        return manager.getPublisher()
                .unpublishFeatureType(thematicName, storeName, layerName);
    }

    /**
     * 发布shp服务
     *
     * @param zipFilePath  文件路径
     * @param workspace    工作空间名称
     * @param storeName    存储源名称
     * @param layerName    图层名称
     * @param srs          参考系
     * @param defaultStyle 图层样式
     * @return
     * @throws Exception
     */
    public Result publishShp(
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
            published = GeoserverUtils.publisher.publishShp(workspace, storeName, layerName, zipFile, srs);
        } else {
            published = GeoserverUtils.publisher.publishShp(workspace, storeName, layerName, zipFile, srs, defaultStyle);
        }
        if (published) {
            return ResultUtil.success();
        }
        return ResultUtil.error(ResultCode.SHP_PUBLISH_FAIL);
    }


    public Result publishShp(String workspace, String storename, String datasetname, File zipFile) {
        boolean published;
        try {
            published = GeoserverUtils.publisher.publishShp(workspace, storename, datasetname, zipFile);
            if (published) {
                return ResultUtil.success();
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        return ResultUtil.error(ResultCode.SHP_PUBLISH_FAIL);
    }

    /**
     * 发布tif数据服务
     *
     * @param fileName     文件路径
     * @param workspace    工作空间名称
     * @param storeName    存储源名称
     * @param layerName    图层名称
     * @param srs          参考系
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
            return GeoserverUtils.publisher.publishGeoTIFF(workspace, storeName, layerName, geotiff);
        } else {
            return GeoserverUtils.publisher.publishGeoTIFF(
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

    /**
     * 显示所有发布图层
     */
    public List<String> getAllLayers() {
        RESTLayerList layers = GeoserverUtils.reader.getLayers();
        List<String> layerNames = Lists.newArrayList();
        for (int i = 0; i < layers.size(); i++) {
            String layer = layers.get(i).getName();
            layerNames.add(layer);
        }
        return layerNames;
    }

    /**
     * 查找图层
     */
    public boolean findLayer(String workspace, String layerName) {
        RESTLayer layer = GeoserverUtils.reader.getLayer(workspace, layerName);
        return (layer == null) ? false : true;
    }

    public boolean findLayerGroup(String layerGroupName) {
        RESTLayerGroup layerGroup = GeoserverUtils.reader.getLayerGroup(layerGroupName);
        return (layerGroup == null) ? false : true;
    }

    public List<String> getLayerNames(String layerGroupName) {
        RESTLayerGroup layerGroup = GeoserverUtils.reader.getLayerGroup(layerGroupName);
        return layerGroup.getPublishedList().getNames();
    }

}
