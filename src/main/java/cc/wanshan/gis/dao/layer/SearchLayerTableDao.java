package cc.wanshan.gis.dao.layer;

import cc.wanshan.gis.common.pojo.Result;

/**
 * @Author Li Cheng
 * @Description 查询对应表是否存在
 * @Date 17:02 2019/2/27
 * @Param
 * @return
 **/
public interface SearchLayerTableDao {
    /**
     * @return java.lang.Boolean
     * @Author Li Cheng
     * @Description
     * @Date 16:49 2019/2/27
     * @Param [layer]
     **/
    public Result searchLayer(String layerName, String schema);
}
