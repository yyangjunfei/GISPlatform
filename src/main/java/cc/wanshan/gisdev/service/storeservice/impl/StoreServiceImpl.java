package cc.wanshan.gisdev.service.storeservice.impl;


import cc.wanshan.gisdev.dao.StoreDao;
import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.entity.drawlayer.Store;
import cc.wanshan.gisdev.service.storeservice.StoreService;
import cc.wanshan.gisdev.utils.ResultUtil;
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
    public Result insertStore(Store store) {
        logger.info("insertStore::store = [{}]",store);
        int i = storeDao.insertStore(store);
        if (i==1){
            return ResultUtil.success(store);
        }
        return ResultUtil.error(1,"保存失败");
    }
    @Override
    public Store findStoreByStoreId(Integer storeId) {
        logger.info("findStoreByStoreId::storeId = [{}]",storeId);
        return storeDao.findStoreByStoreId(storeId);
    }

    @Override
    public List<Store> findStoreByUserId (Integer userId) {
        logger.info("findStoreByUserId::userId = [{}]",userId);
        return storeDao.findStoreByUserId(userId);
    }
}
