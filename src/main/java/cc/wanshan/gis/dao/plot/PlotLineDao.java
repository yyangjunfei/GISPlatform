package cc.wanshan.gis.dao.plot;

import cc.wanshan.gis.entity.plot.PlotLine;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PlotLineDao {
    @SelectKey(keyProperty = "plotLine.id", resultType = String.class, before = true,
            statement = "select replace(uuid(), '-', '') as id from dual")
    @Options(keyProperty = "userBase.id", useGeneratedKeys = true)
    @Insert("insert into plot_line(id, name, type, layer_id, color, style, width, opacity, symbol, geom, create_time, create_by, update_time, update_by) " +
            "values(#{id}, #{name}, #{type}, #{layer_id}, #{color}, #{style}, #{width}, #{opacity}, #{symbol}, #{geom}, #{create_time}, #{create_by}, #{update_time}, #{update_by})")
    int insert(@Param("plotLine") PlotLine plotLine);

    @Select({"select * from plot_line"})
    List<PlotLine> findAll();
}
