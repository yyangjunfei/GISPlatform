package cc.wanshan.gis.service.geoserver.impl;
import cc.wanshan.gis.dao.createschema.CreateSchemaDao;
import cc.wanshan.gis.dao.searchschema.SearchSchemaDao;
import cc.wanshan.gis.entity.GeoServer;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Store;
import cc.wanshan.gis.entity.usermanagement.User;
import cc.wanshan.gis.service.geoserver.GeoServerService;
import cc.wanshan.gis.service.store.StoreService;
import cc.wanshan.gis.service.user.UserService;
import cc.wanshan.gis.utils.GeoServerUtils;
import cc.wanshan.gis.utils.ResultUtil;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTDataStoreList;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service("geoServerServiceImpl")
public class GeoServerServiceImpl implements GeoServerService {

    private static final Logger LOG = LoggerFactory.getLogger(GeoServerServiceImpl.class);

    @Autowired
    private GeoServer geoServer;

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
        LOG.info("creatWorkspace::workspace = [{}]", workspace);
        Result searchSchema = searchSchemaDao.searchSchema(workspace);
        if (searchSchema.getCode() == 1) {
            Result schema = createSchemaDao.createSchema(workspace);
            if (schema.getCode() == 0) {
                boolean workSpace = createWorkSpace(workspace, workspace);
                if (workSpace) {
                    Result newStore = createDataStore("newStore", workspace);
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
                            LOG.warn("插入store失败", save.toString());
                            return ResultUtil.error(1, "插入store失败");
                        }
                    } else {
                        LOG.warn("创建store失败", newStore.toString());
                        return newStore;
                    }
                } else {
                    LOG.warn("创建工作空间失败");
                    return ResultUtil.error(1, "创建工作空间失败");
                }
            } else {
                LOG.warn("创建schema失败", schema.toString());
                return schema;
            }
        } else {
            LOG.warn(workspace + "workspace已存在");
            return ResultUtil.error(1, "workspace已存在");
        }
    }

    @Override
    public Boolean searchLayer(String thematicName, String layerName) {
        RESTLayer layer = GeoServerUtils.reader.getLayer(thematicName, layerName);
        return layer != null;
    }

    @Override
    public Boolean deleteLayer(String thematicName, String storeName, String layerName) {
        return GeoServerUtils.publisher.unpublishFeatureType(thematicName, storeName, layerName);
    }

    /**
     * 创建工作空间
     *
     * @param workSpaceName 工作空间名称
     * @param uri           工作空间uri，可为空
     * @return
     * @throws URISyntaxException
     */
    @Override
    public boolean createWorkSpace(String workSpaceName, String uri) throws URISyntaxException {

        //判断工作空间是否存在
        List<String> workspaces = GeoServerUtils.manager.getReader().getWorkspaceNames();
        if(!workspaces.contains(workSpaceName)){
            if (uri == null || uri.isEmpty()) {
                return GeoServerUtils.publisher.createWorkspace(workSpaceName);
            }else{
                return GeoServerUtils.publisher.createWorkspace(workSpaceName, new URI(uri));
            }
        }else {
            LOG.warn("workspace已经存在了,ws :" + workSpaceName);
        }
        return false;
    }

    /**
     * 创建存储名称
     *
     * @param storeName 存储名称
     * @param workspace 工作空间
     * @return
     */
    @Override
    public Result createDataStore(String storeName, String workspace) {
        LOG.info("已进入createDataStore::manager = [{}], storeName = [{}], workspace = [{}]", GeoServerUtils.manager, storeName, workspace);
        //判断数据存储（datastore）是否已经存在，不存在则创建
        if (GeoServerUtils.manager != null && storeName != null && !"".equals(storeName)) {
            RESTDataStoreList restDataStoreList = GeoServerUtils.manager.getReader().getDatastores(workspace);
            List<String> listStoreName = restDataStoreList.getNames();
            if (!listStoreName.contains(storeName)){
                GSPostGISDatastoreEncoder store = new GSPostGISDatastoreEncoder(storeName);
                //设置url
                store.setHost(geoServer.getPostgisHost());
                //设置端口
                store.setPort(geoServer.getPostgisPort());
                // 数据库的用户名
                store.setUser(geoServer.getPostgisUser());
                // 数据库的密码
                store.setPassword(geoServer.getPostgisPassword());
                // 那个数据库;
                store.setDatabase(geoServer.getPostgisDatabase());
                //当前先默认使用public这个schema
                store.setSchema(workspace);
                // 超时设置
                store.setConnectionTimeout(20);
                // 最大连接数
                store.setMaxConnections(20);
                // 最小连接数
                store.setMinConnections(1);
                store.setExposePrimaryKeys(true);
                if (GeoServerUtils.manager.getStoreManager().create(workspace, store)) {
                    return ResultUtil.success();
                } else {
                    LOG.warn("存储点创建失败：createDataStore::manager = [{}], storeName = [{}], workspace = [{}]", storeName, workspace);
                    return ResultUtil.error(3, "存储点" + storeName + "创建失败");
                }
            }
             else {
                LOG.warn("存储点已存在:createDataStore::manager = [{}], storeName = [{}], workspace = [{}]", storeName, workspace);
                return ResultUtil.error(1, "存储点" + storeName + "已存在");
            }
        } else {
            LOG.warn("空指针异常:createDataStore::manager = [{}], storeName = [{}], workspace = [{}]", storeName, workspace);
            return ResultUtil.error(2, "空指针异常");
        }
    }

    /**
     * 发布图层
     *
     * @param ws        工作空间
     * @param storeName 存储名称
     * @param tableName 数据库表名称
     * @return
     */
    @Override
    public Result publishLayer(String ws, String storeName, String tableName) {
        LOG.info("已进入：publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", ws, storeName, tableName);
        if (ws != null && !"".equals(ws) && storeName != null && !"".equals(storeName) && tableName != null && !"".equals(tableName)) {

            try {
                createWorkSpace(ws,"");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Result dataStore = createDataStore(storeName, ws);
            if (dataStore.getCode() == 0 || dataStore.getCode() == 1) {
                RESTLayer layer = GeoServerUtils.reader.getLayer(ws, tableName);
                if (layer == null) {
                    GSFeatureTypeEncoder pds = new GSFeatureTypeEncoder();
                    pds.setTitle(tableName);
                    pds.setName(tableName);
                    pds.setSRS("EPSG:4326");
                    GSLayerEncoder layerEncoder = new GSLayerEncoder();
                    boolean publishDBLayer = GeoServerUtils.publisher.publishDBLayer(ws, storeName, pds, layerEncoder);

                    if (publishDBLayer) {
                        return ResultUtil.success("图层发布成功");
                    } else {
                        LOG.warn("publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", GeoServerUtils.manager, ws, storeName, tableName);
                        return ResultUtil.error(3, "发布失败");
                    }
                } else {
                    LOG.warn("图层已存在：publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", GeoServerUtils.manager, ws, storeName, tableName);
                    return ResultUtil.error(1, "图层已存在");
                }
            } else {
                return dataStore;
            }
        } else {
            LOG.warn("空指针异常：publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", GeoServerUtils.manager, ws, storeName, tableName);
            return ResultUtil.error(2, "空指针异常");
        }
    }
}
=======
package cc.wanshan.gis.service.geoserver.impl;
import cc.wanshan.gis.dao.createschema.CreateSchemaDao;
import cc.wanshan.gis.dao.searchschema.SearchSchemaDao;
import cc.wanshan.gis.entity.geoserver.GeoServer;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Store;
import cc.wanshan.gis.entity.security.User;
import cc.wanshan.gis.service.geoserver.GeoServerService;
import cc.wanshan.gis.service.geoserver.StoreService;
import cc.wanshan.gis.service.security.UserService;
import cc.wanshan.gis.utils.GeoServerUtils;
import cc.wanshan.gis.utils.ResultUtil;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service("geoServerServiceImpl")
public class GeoServerServiceImpl implements GeoServerService {

    private static final Logger LOG = LoggerFactory.getLogger(GeoServerServiceImpl.class);

    @Autowired
    private GeoServer geoServer;

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
        LOG.info("creatWorkspace::workspace = [{}]", workspace);
        Result searchSchema = searchSchemaDao.searchSchema(workspace);
        if (searchSchema.getCode() == 1) {
            Result schema = createSchemaDao.createSchema(workspace);
            if (schema.getCode() == 0) {
                boolean workSpace = createWorkSpace(workspace, workspace);
                if (workSpace) {
                    Result newStore = createDataStore("newStore", workspace);
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
                            LOG.warn("插入store失败", save.toString());
                            return ResultUtil.error(1, "插入store失败");
                        }
                    } else {
                        LOG.warn("创建store失败", newStore.toString());
                        return newStore;
                    }
                } else {
                    LOG.warn("创建工作空间失败");
                    return ResultUtil.error(1, "创建工作空间失败");
                }
            } else {
                LOG.warn("创建schema失败", schema.toString());
                return schema;
            }
        } else {
            LOG.warn(workspace + "workspace已存在");
            return ResultUtil.error(1, "workspace已存在");
        }
    }

    @Override
    public Boolean searchLayer(String thematicName, String layerName) {
        RESTLayer layer = GeoServerUtils.reader.getLayer(thematicName, layerName);
        return layer != null;
    }

    @Override
    public Boolean deleteLayer(String thematicName, String storeName, String layerName) {
        return GeoServerUtils.publisher.unpublishFeatureType(thematicName, storeName, layerName);
    }

    /**
     * 创建工作空间
     *
     * @param workSpaceName 工作空间名称
     * @param uri           工作空间uri，可为空
     * @return
     * @throws URISyntaxException
     */
    @Override
    public boolean createWorkSpace(String workSpaceName, String uri) throws URISyntaxException {

        //判断工作空间是否存在
        List<String> workspaces = GeoServerUtils.manager.getReader().getWorkspaceNames();
        if(!workspaces.contains(workSpaceName)){
            if (uri == null || uri.isEmpty()) {
                return GeoServerUtils.publisher.createWorkspace(workSpaceName);
            }else{
                return GeoServerUtils.publisher.createWorkspace(workSpaceName, new URI(uri));
            }
        }else {
            LOG.warn("workspace已经存在了,ws :" + workSpaceName);
        }
        return false;
    }

    /**
     * 创建存储名称
     *
     * @param storeName 存储名称
     * @param workspace 工作空间
     * @return
     */
    @Override
    public Result createDataStore(String storeName, String workspace) {
        LOG.info("已进入createDataStore::manager = [{}], storeName = [{}], workspace = [{}]", GeoServerUtils.manager, storeName, workspace);
        //判断数据存储（datastore）是否已经存在，不存在则创建
        if (GeoServerUtils.manager != null && storeName != null && !"".equals(storeName)) {
            RESTDataStore restStore = GeoServerUtils.reader.getDatastore(workspace, storeName);
            if (restStore == null) {
                GSPostGISDatastoreEncoder store = new GSPostGISDatastoreEncoder(storeName);
                //设置url
                store.setHost(geoServer.getPostgisHost());
                //设置端口
                store.setPort(geoServer.getPostgisPort());
                // 数据库的用户名
                store.setUser(geoServer.getPostgisUser());
                // 数据库的密码
                store.setPassword(geoServer.getPostgisPassword());
                // 那个数据库;
                store.setDatabase(geoServer.getPostgisDatabase());
                //当前先默认使用public这个schema
                    store.setSchema(workspace);
                // 超时设置
                store.setConnectionTimeout(20);
                // 最大连接数
                store.setMaxConnections(20);
                // 最小连接数
                store.setMinConnections(1);
                store.setExposePrimaryKeys(true);
                if (GeoServerUtils.manager.getStoreManager().create(workspace, store)) {
                    return ResultUtil.success();
                } else {
                    LOG.warn("存储点创建失败：createDataStore::manager = [{}], storeName = [{}], workspace = [{}]", storeName, workspace);
                    return ResultUtil.error(3, "存储点" + storeName + "创建失败");
                }
            } else {
                LOG.warn("存储点已存在:createDataStore::manager = [{}], storeName = [{}], workspace = [{}]", storeName, workspace);
                return ResultUtil.error(1, "存储点" + storeName + "已存在");
            }
        } else {
            LOG.warn("空指针异常:createDataStore::manager = [{}], storeName = [{}], workspace = [{}]", storeName, workspace);
            return ResultUtil.error(2, "空指针异常");
        }
    }

    /**
     * 发布图层
     *
     * @param ws        工作空间
     * @param storeName 存储名称
     * @param tableName 数据库表名称
     * @return
     */
    @Override
    public Result publishLayer(String ws, String storeName, String tableName) {
        LOG.info("已进入：publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", ws, storeName, tableName);
        if (ws != null && !"".equals(ws) && storeName != null && !"".equals(storeName) && tableName != null && !"".equals(tableName)) {

            try {
                createWorkSpace(ws,"");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Result dataStore = createDataStore(storeName, ws);
            if (dataStore.getCode() == 0 || dataStore.getCode() == 1) {
                RESTLayer layer = GeoServerUtils.reader.getLayer(ws, tableName);
                if (layer == null) {
                    GSFeatureTypeEncoder pds = new GSFeatureTypeEncoder();
                    pds.setTitle(tableName);
                    pds.setName(tableName);
                    pds.setSRS("EPSG:4326");
                    GSLayerEncoder layerEncoder = new GSLayerEncoder();
                    boolean publishDBLayer = GeoServerUtils.publisher.publishDBLayer(ws, storeName, pds, layerEncoder);

                    if (publishDBLayer) {
                        return ResultUtil.success("图层发布成功");
                    } else {
                        LOG.warn("publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", GeoServerUtils.manager, ws, storeName, tableName);
                        return ResultUtil.error(3, "发布失败");
                    }
                } else {
                    LOG.warn("图层已存在：publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", GeoServerUtils.manager, ws, storeName, tableName);
                    return ResultUtil.error(1, "图层已存在");
                }
            } else {
                return dataStore;
            }
        } else {
            LOG.warn("空指针异常：publishLayer::manager = [{}], ws = [{}], storeName = [{}], tableName = [{}]", GeoServerUtils.manager, ws, storeName, tableName);
            return ResultUtil.error(2, "空指针异常");
        }
    }
}
>>>>>>> src/main/java/cc/wanshan/gis/service/geoserver/impl/GeoServerServiceImpl.java
