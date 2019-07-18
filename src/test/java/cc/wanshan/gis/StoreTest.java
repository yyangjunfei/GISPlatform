package cc.wanshan.gis;

import cc.wanshan.gis.dao.layer.StoreDao;
import cc.wanshan.gis.entity.authorize.User;
import cc.wanshan.gis.entity.drawlayer.Store;
import cc.wanshan.gis.service.layer.StoreService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreTest {

    private static final Logger logger = LoggerFactory.getLogger(StoreTest.class);
    @Resource
    private StoreDao storeDao;
    @Resource
    private StoreService storeService;
    private Store store = new Store();

    @Test
    public void insertStore() {
        logger.info("insertStore::");
        User user = new User();
        user.setUserId("1628256880eb11e99f2320040ff72212");
        store.setStoreName("newStore");
        store.setUser(user);
        store.setInsertTime(new Date());
        store.setUpdateTime(new Date());
        int i = storeDao.insertStore(store);
        logger.info("insertStore::" + i);
    }

    @Test
    public void findLayersByUsername() {
        logger.info("findLayersByUsername::");
        List<Store> storeByUsername = storeService.findStoreByUsername("蒹葭苍苍");
        for (Store store1 : storeByUsername) {
            logger.info("findLayersByUsername::" + store1.toString());
        }
        List<Store> byUsername = storeDao.findStoresByUsername("蒹葭苍苍");
        for (Store store1 : byUsername) {
            logger.info("findLayersByUsername::" + store1.toString());
        }
    }
}
