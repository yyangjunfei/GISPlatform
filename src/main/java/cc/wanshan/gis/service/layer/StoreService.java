package cc.wanshan.gis.service.layer;


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
     * @return java.util.List<cc.wanshan.demo.entity.Store>
     * @Author Li Cheng
     * @Description 根据用户名查找store
     * @Date 14:17 2019/4/22
     * @Param [username]
     **/
    public List<Store> findStoreByUsername(String username);

    /**
     * @return cc.wanshan.demo.entity.Store
     * @Author Li Cheng
     * @Description 根据storeId查询store
     * @Date 14:16 2019/4/22
     * @Param [storeId]
     **/
    public Store findStoreByStoreId(String storeId);

    /**
     * @return java.util.List<cc.wanshan.demo.entity.Store>
     * @Author Li Cheng
     * @Description 根据userId查找store
     * @Date 14:19 2019/4/22
     * @Param [userId]
     **/
    public List<Store> findStoreByUserId(String userId);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 新增store
     * @Date 14:16 2019/4/22
     * @Param [store]
     **/
    public Boolean insertStore(Store store);
}
