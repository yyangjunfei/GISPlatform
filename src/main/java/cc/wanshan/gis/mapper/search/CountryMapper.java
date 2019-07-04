package cc.wanshan.gis.mapper.search;

import cc.wanshan.gis.entity.search.Country;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CountryMapper {
    int deleteByPrimaryKey(Integer gid);

    int insert(Country record);

    int insertSelective(Country record);

    Country selectByPrimaryKey(Integer gid);

    int updateByPrimaryKeySelective(Country record);

    int updateByPrimaryKey(Country record);
}