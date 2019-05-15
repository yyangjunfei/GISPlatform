package cc.wanshan.gisdev.service.layerservice.impl;

import cc.wanshan.gisdev.dao.LayerDao;
import cc.wanshan.gisdev.dao.createschemadao.impl.CreateSchemaDaoImpl;
import cc.wanshan.gisdev.dao.cretelayerstabledao.CreatLayerTableDao;
import cc.wanshan.gisdev.dao.droplayerdao.DropLayerDao;
import cc.wanshan.gisdev.dao.insertfeaturedao.InsertFeatureDao;
import cc.wanshan.gisdev.dao.searchlayertabledao.SearchLayerTableDao;
import cc.wanshan.gisdev.dao.searchschemadao.impl.SearchSchemaDaoImpl;
import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.entity.drawlayer.Feature;
import cc.wanshan.gisdev.entity.drawlayer.Layer;
import cc.wanshan.gisdev.entity.drawlayer.Properties;
import cc.wanshan.gisdev.service.layerservice.LayerService;
import cc.wanshan.gisdev.utils.GeoserverUtils;
import cc.wanshan.gisdev.utils.ResultUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.core.instrument.util.StringUtils;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

@Service(value = "layerServiceImpl")
public class LayerServiceImpl implements LayerService {
    private static final Logger logger= LoggerFactory.getLogger(LayerServiceImpl.class);
    @Resource
    private LayerDao layerDao;
    @Resource
    private ResultUtil resultUtil;
    @Resource
    private GeoserverUtils geoserverUtils;
    @Resource
    private DropLayerDao dropLayerDao;
    @Resource
    private CreateSchemaDaoImpl createSchemaDao;
    @Resource
    private SearchSchemaDaoImpl searchSchemaDao;
    @Resource(name = "searchLayerTableDaoImpl")
    private SearchLayerTableDao searchLayerTableDao;
    @Resource(name = "createLayerTableDaoImpl")
    private CreatLayerTableDao createLayerTableDao;
    @Resource(name = "insertLayerDaoImpl")
    private InsertFeatureDao insertFeatureDao;
    @Override
    @Transactional
    public Result insertLayer(Layer layer, String workspace) {
        logger.info("insertLayer::layer = [{}], workspace = [{}]",layer, workspace);
        int i = layerDao.insertLayer(layer);
        if (i==1){
            Result result = newLayer(workspace, layer.getLayerName(), layer.getType(), layer.getEpsg());
            if (result.getCode()==0){
                return ResultUtil.success(layer);
            }else {
                logger.warn("新建图层失败",result.toString());
                return result;
            }
        }else {
            logger.warn("保存出错"+layer.toString());
            return ResultUtil.error(2,"保存出错");
        }
    }
    @Override
    @Transactional
    public Result deleteLayer(JSONObject jsonObject) {
        logger.info("deleteLayer::jsonObject = [{}]",jsonObject);
        if (jsonObject!=null){
            String layerName = jsonObject.getString("layerName");
            Integer storeId =jsonObject.getInteger("storeId");
            String workspace = jsonObject.getString("username");
            String storeName = jsonObject.getString("storeName");
            GeoServerRESTManager manager = GeoserverUtils.manager;
            RESTLayer layer = manager.getReader().getLayer(workspace, layerName);
            if (layer!=null){
                boolean deleteFeatureType = manager.getPublisher().unpublishFeatureType(workspace, storeName, layerName);
                if (deleteFeatureType){
                    int i = layerDao.deleteLayerByLayerNameAndStoreId(layerName, storeId);
                    Result result = dropLayerDao.dropLayer(workspace, layerName);
                    if (result.getCode()==0&&i==1){
                        return ResultUtil.success();
                    }else {
                        logger.warn("删除图层失败"+layerName);
                        return result;
                    }
                }else {
                    logger.warn("geoserver删除失败");
                    ResultUtil.error(1,"geoserver删除失败");
                }
            }else {
                int i = layerDao.deleteLayerByLayerNameAndStoreId(layerName, storeId);
                Result result = dropLayerDao.dropLayer(workspace, layerName);
                if (result.getCode()==0&&i==1){
                    return ResultUtil.success();
                }else {
                    logger.warn("删除layer表记录数storeId:"+storeId+":"+layerName+":"+i+"删除用户模式记录"+workspace+":"+layerName+":"+result.getMsg());
                    return result;
                }
            }
        }else {
            logger.warn("数据库删除失败");
            ResultUtil.error(1,"数据库删除失败");
        }
        return ResultUtil.error(1,"空指针异常");
    }
    @Override
    public Result searchLayer(JSONObject jsonObject) {
        logger.info("searchLayer::jsonObject = [{}]",jsonObject);
        if (jsonObject!=null){
            String workspace = jsonObject.getString("workspace");
            String layerName = jsonObject.getString("layerName");
            RESTLayer layer = GeoserverUtils.manager.getReader().getLayer(workspace, layerName);
            if (layer==null){
                logger.warn("用户"+workspace+"图层未发布"+layerName);
                return ResultUtil.error(1,"图层未发布");
            }else {
                return ResultUtil.success();
            }
        }else {
            logger.warn("jsonObject为null");
            return ResultUtil.error(1,"jsonObject为null");
        }
    }
    @Override
    @Transactional
    public Result newLayer(String workspace, String layerName, String type, String epsg) {
        logger.info("newLayer::workspace = [{}], layerName = [{}], type = [{}], epsg = [{}]",workspace, layerName, type, epsg);
        if (layerName!=null&&!"".equals(layerName)&&type!=null&&!"".equals(type)&&epsg!=null&&!"".equals(epsg)){
            Result creatTable = createLayerTableDao.creatTable(workspace,layerName, type, epsg);
            if (creatTable.getCode()==0){
                return ResultUtil.success();
            }else {
                return creatTable;
            }
        }else {
            logger.warn("空指针异常");
            return ResultUtil.error(2,"空指针异常");
        }
    }
    @Override
    @Transactional
    public Result insertFeatures(JSONObject object) throws IOException {
        logger.info("insertFeatures::object = [{}]",object);
        Feature feature = new Feature();
        Properties newProperties= new Properties();
        if (object!=null&&!"".equals(object)&& StringUtils.isNotBlank(object.getString("layerName"))&& StringUtils.isNotBlank(object.getString("workspace"))){
            String schema =object.getString("workspace");
            String layerName = object.getString("layerName");
            Result searchLayer = searchLayerTableDao.searchLayer(layerName, schema);
            if (searchLayer.getCode()==0){
                JSONArray features = object.getJSONArray("features");
                ArrayList<Feature> featuresList = new ArrayList<>();
                for (int i = 0; i < features.size(); i++) {
                    feature.setGeometry(features.getJSONObject(i).getString("geometry"));
                    feature.setgeoId(i);
                    JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
                    newProperties.setFclass(properties.getString("featureClass"));
                    newProperties.setName(properties.getString("name"));
                    feature.setProperties(newProperties);
                    featuresList.add(feature);
                }
                Result insertLayers = insertFeatureDao.insertFeatures(featuresList, layerName,schema);
                if (insertLayers.getCode()==0){
                    return publishLayer(schema, object.getString("storeName"), layerName);
                }else {
                    logger.warn(layerName+"插入失败");
                    return insertLayers;
                }
            }else {
                System.out.println("请先创建图层");
                logger.warn(layerName+"图层为创建");
                return searchLayer;
            }
        }
        logger.warn(object+"参数为空");
        return resultUtil.error(2,"参数为空");
    }
    @Override
    public Result findLayerCountByUsernameAndLayerName(String username,String layerName) {
        logger.info("findLayerCountByUsernameAndLayerName::username = [{}], layerName = [{}]",username, layerName);
        int layerCount = layerDao.findLayerCountByUsernameAndLayerName(username, layerName);
        Result result = searchLayerTableDao.searchLayer(layerName, username);
        if (layerCount>0||result.getCode()==0){
            logger.warn(layerName+"图层名已存在，请重新命名");
            return ResultUtil.error(1,"图层名已存在，请重新命名");
        }else {
            return ResultUtil.success();
        }
    }
    private Result publishLayer(String workspace,String store,String layerName){
        logger.info("publishLayer::workspace = [{}], store = [{}], layerName = [{}]",workspace, store, layerName);
        RESTLayer layer = GeoserverUtils.reader.getLayer(workspace, layerName);
        if (layer==null) {
            Result result = geoserverUtils.publishLayer( workspace, store, layerName);
            if (result.getCode() == 0 || result.getCode() == 1) {
                return ResultUtil.success();
            } else {
                logger.warn(layerName+"发布失败");
                return result;
            }
        }else {
            return ResultUtil.success();
        }
    }
}
