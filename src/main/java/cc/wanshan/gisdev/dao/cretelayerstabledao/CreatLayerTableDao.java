package cc.wanshan.gisdev.dao.cretelayerstabledao;

import cc.wanshan.gisdev.entity.Result;

/**
 *
 * @author Li Cheng 2019.2.27 创建图层对应的数据库表
 */
public interface CreatLayerTableDao {
    /**
     * 创建表
     * @param layerName  图层对应实体类
     * @return 是否创建成功
     */
    public Result creatTable(String workspace, String layerName, String type, String epsg);
}
