package cc.wanshan.gis.service.layer.geoserver.impl;

import cc.wanshan.gis.dao.layer.StoreDao;
import cc.wanshan.gis.entity.plot.of2d.Store;
import cc.wanshan.gis.service.layer.geoserver.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service(value = "storeServiceImpl")
public class StoreServiceImpl implements StoreService {

    private static final Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Resource
    private StoreDao storeDao;

    @Override
    public List<Store> findStoreByUsername(String username) {
        logger.info("findStoreByUsername::username = [{}]", username);
        return storeDao.findStoresByUsername(username);
    }

    @Override
    @Transactional
    public Boolean insertStore(Store store) {
        logger.info("insertStore::store = [{}]", store);
        int i = storeDao.insertStore(store);
        if (i == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Store findStoreByStoreId(String storeId) {
        logger.info("findStoreByStoreId::storeId = [{}]", storeId);
        return storeDao.findStoreByStoreId(storeId);
    }

    @Override
    public List<Store> findStoreByUserId(String userId) {
        logger.info("findStoreByUserId::userId = [{}]", userId);
        return storeDao.findStoreByUserId(userId);
    }
}
