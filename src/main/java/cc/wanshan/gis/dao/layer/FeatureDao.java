package cc.wanshan.gis.dao.layer;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.plot.of2d.Feature;

import java.util.List;

/**
 * @Author Li Cheng
 * @Description 往对应的图层表插入图层数据
 * @Date 16:52 2019/2/27
 **/
public interface FeatureDao {
    /**
     * 图层实体类
     *
     * @return java.lang.Boolean
     * @Author Li Cheng
     * @Description 图层实体类
     * @Date 16:51 2019/2/27
     * @Param [layer 图层实体类] 图层实体类
     **/
    public Result insertFeatures(List<Feature> features, String tableName, String schema);

    /**
     * description: 根据图层id查询相关元素
     *
     * @param layerId 图层Id
     * @return java.util.List<cc.wanshan.gis.entity.plot.of2d.Feature>
     **/
    public List<Feature> findFeatureByLayerId(String layerId);
}
