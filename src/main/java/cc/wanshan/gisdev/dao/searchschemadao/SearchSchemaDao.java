package cc.wanshan.gisdev.dao.searchschemadao;


import cc.wanshan.gisdev.entity.Result;

/**
 * @Author Li Cheng
 * @Description 查询schema
 * @Date 18:09 2019/3/22
 * @Param
 * @return
 **/

public interface SearchSchemaDao {
    public Result searchSchema(String schema);
}
