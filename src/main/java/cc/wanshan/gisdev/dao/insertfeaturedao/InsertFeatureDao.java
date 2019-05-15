package cc.wanshan.gisdev.dao.insertfeaturedao;



import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.entity.drawlayer.Feature;

import java.util.ArrayList;

/**
 * @Author Li Cheng
 * @Description 往对应的图层表插入图层数据
 * @Date 16:52 2019/2/27

 **/
public interface InsertFeatureDao {
    /**  图层实体类
     * @Author Li Cheng
     * @Description 图层实体类
     * @Date 16:51 2019/2/27
     * @Param [layer 图层实体类] 图层实体类
     * @return java.lang.Boolean
     **/
    public Result insertFeatures(ArrayList<Feature> features, String tableName, String schema);

}
