package cc.wanshan.gisdev;

import cc.wanshan.gisdev.dao.StoreDao;
import cc.wanshan.gisdev.entity.drawlayer.Store;
import cc.wanshan.gisdev.entity.usermanagement.User;
import cc.wanshan.gisdev.service.storeservice.StoreService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Li Cheng
 * @date 2019/5/20 11:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreTest {

  private static final Logger logger = LoggerFactory.getLogger(StoreTest.class);
  @Resource
  private StoreDao storeDao;
  @Resource
  private StoreService storeService;
  private Store store=new Store();
  @Test
  public void insertStore(){
    logger.info("insertStore::");
    User user = new User();
    user.setUserId("7ca5cf8e7ab011e98ce020040ff72212");
    store.setStoreName("newStore");
    store.setUser(user);
    store.setInsertTime(new Date());
    store.setUpdateTime(new Date());
    int i = storeDao.insertStore(store);
    logger.info("insertStore::"+i);
  }
  @Test
  public void findLayersByUsername(){
    logger.info("findLayersByUsername::");
    /*List<Store> storeByUsername = storeService.findStoreByUsername("蒹葭苍苍");
    for (Store store1 : storeByUsername) {
      logger.info("findLayersByUsername::"+store1.toString());
    }*/
    List<Store> byUsername = storeDao.findStoresByUsername("蒹葭苍苍");
    for (Store store1 : byUsername) {
      logger.info("findLayersByUsername::"+store1.toString());
    }
  }
}
