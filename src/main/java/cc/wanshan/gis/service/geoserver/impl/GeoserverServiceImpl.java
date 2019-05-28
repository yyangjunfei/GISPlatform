package cc.wanshan.gis.service.geoserver.impl;

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
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URISyntaxException;

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
              return ResultUtil.error(1,"插入store失败");
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
}
