package cc.wanshan.gis.dao.droplayer;

import cc.wanshan.gis.entity.Result;

/**
 * @Author Li Cheng
 * @Description 删除layer数据对应表
 * @Date 10:39 2019/4/1
 * @Param
 * @return
 **/
public interface DropLayerDao {
    Result dropLayer(String schema, String layerName);
}
