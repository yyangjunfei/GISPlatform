package cc.wanshan.gis.mapper.search;

import cc.wanshan.gis.entity.search.City;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CityMapper {
    int deleteByPrimaryKey(Integer gid);

    int insert(City record);

    int insertSelective(City record);

    City selectByPrimaryKey(Integer gid);

    int updateByPrimaryKeySelective(City record);

    int updateByPrimaryKey(City record);
}