package cc.wanshan.gis.dao.searchlayertable;

import cc.wanshan.gis.entity.Result;

/**
 * @Author Li Cheng
 * @Description 查询对应表是否存在
 * @Date 17:02 2019/2/27
 * @Param
 * @return
 **/
public interface SearchLayerTableDao {
    /**
     * @Author Li Cheng
     * @Description
     * @Date 16:49 2019/2/27
     * @Param [layer]
     * @return java.lang.Boolean
     **/
    public Result searchLayer(String layerName, String schema);
}
