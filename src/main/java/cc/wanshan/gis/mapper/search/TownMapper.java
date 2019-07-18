package cc.wanshan.gis.mapper.search;

import cc.wanshan.gis.entity.search.Town;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TownMapper {
    int deleteByPrimaryKey(Integer gid);

    int insert(Town record);

    int insertSelective(Town record);

    Town selectByPrimaryKey(Integer gid);

    int updateByPrimaryKeySelective(Town record);

    int updateByPrimaryKey(Town record);
}