package cc.wanshan.gis.service.store.impl;


import cc.wanshan.gis.dao.StoreDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Store;
import cc.wanshan.gis.service.store.StoreService;
import cc.wanshan.gis.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "storeServiceImpl")
public class StoreServiceImpl implements StoreService {
    private static final Logger logger= LoggerFactory.getLogger(StoreServiceImpl.class);
    @Resource
    private StoreDao storeDao;
    @Override
    public List<Store> findStoreByUsername(String username) {
        logger.info("findStoreByUsername::username = [{}]",username);
        return storeDao.findStoresByUsername(username);
    }
    @Override
    @Transactional
    public Boolean insertStore(Store store) {
        logger.info("insertStore::store = [{}]",store);
        int i = storeDao.insertStore(store);
        if (i==1){
            return true;
        }
        return false;
    }
    @Override
    public Store findStoreByStoreId(String storeId) {
        logger.info("findStoreByStoreId::storeId = [{}]",storeId);
        return storeDao.findStoreByStoreId(storeId);
    }

    @Override
    public List<Store> findStoreByUserId (String userId) {
        logger.info("findStoreByUserId::userId = [{}]",userId);
        return storeDao.findStoreByUserId(userId);
    }
}
