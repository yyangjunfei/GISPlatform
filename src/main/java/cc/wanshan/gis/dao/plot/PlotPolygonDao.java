package cc.wanshan.gis.dao.plot;

import cc.wanshan.gis.entity.plot.PlotPolygon;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PlotPolygonDao {

    @Insert("insert into plot_polygon(id, name, type, layer_id, color, style, width, opacity, symbol, border_color, border_width, border_opacity, geom, create_time, create_by, update_time, update_by) " +
            "values(#{id}, #{name}, #{type}, #{layer_id}, #{color}, #{style}, #{width}, #{opacity}, #{symbol}, #{border_color}, #{border_width}, #{border_opacity}, #{geom}, #{create_time}, #{create_by}, #{update_time}, #{update_by})")
    @SelectKey(keyProperty = "id", resultType = String.class, before = true,
            statement = "select replace(uuid(), '-', '') as id from dual")
    int insert(PlotPolygon plotPolygon);

    @Select({"select * from plot_polygon"})
    List<PlotPolygon> findAll();
}
