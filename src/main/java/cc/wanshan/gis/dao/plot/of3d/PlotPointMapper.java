package cc.wanshan.gis.dao.plot.of3d;

import cc.wanshan.gis.entity.plot.of3d.PlotPoint;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlotPointMapper {
    int deleteByPrimaryKey(String id);

    int insert(PlotPoint record);

    int insertSelective(PlotPoint record);

    PlotPoint selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PlotPoint record);

    int updateByPrimaryKey(PlotPoint record);
}