package cc.wanshan.gis.service.store;


import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Store;

import java.util.List;

/**
 * @Author Li Cheng
 * @Description store增删改查
 * @Date 8:40 2019/3/21
 * @Param
 * @return
 **/
public interface StoreService {
    /**
     * @Author Li Cheng
     * @Description 根据用户名查找store
     * @Date 14:17 2019/4/22
     * @Param [username]
     * @return java.util.List<cc.wanshan.demo.entity.Store>
     **/
    public List<Store> findStoreByUsername(String username);
    /**
     * @Author Li Cheng
     * @Description 根据storeId查询store
     * @Date 14:16 2019/4/22
     * @Param [storeId]
     * @return cc.wanshan.demo.entity.Store
     **/
    public Store findStoreByStoreId(String storeId);
    /**
     * @Author Li Cheng
     * @Description  根据userId查找store
     * @Date 14:19 2019/4/22
     * @Param [userId]
     * @return java.util.List<cc.wanshan.demo.entity.Store>
     **/
    public List<Store> findStoreByUserId(String userId);
    /**
     * @Author Li Cheng
     * @Description 新增store
     * @Date 14:16 2019/4/22
     * @Param [store]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Boolean insertStore(Store store);
}
