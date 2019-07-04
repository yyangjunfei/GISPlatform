package cc.wanshan.gis.mapper.base;

/**
 * 通用  Mapper 接口
 */
public interface BaseMapper<T, ID> {

    int deleteByPrimaryKey(ID id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(ID id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);

}
