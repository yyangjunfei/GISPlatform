package cc.wanshan.gis.mapper.search;

import cc.wanshan.gis.entity.area.Province;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProvinceMapper {
    int deleteByPrimaryKey(Integer gid);

    int insert(Province record);

    int insertSelective(Province record);

    Province selectByPrimaryKey(Integer gid);

    int updateByPrimaryKeySelective(Province record);

    int updateByPrimaryKey(Province record);
}