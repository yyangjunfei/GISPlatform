//package cc.wanshan.gis.dao.plot;
//
//import cc.wanshan.gis.entity.plot.PlotPoint2;
//import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Select;
//import org.apache.ibatis.annotations.SelectKey;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Mapper
//@Component
//public interface PlotPointDao {
//
//    @Insert("insert into plot_point(id, name, type, layer_id, color, style, width, opacity, symbol, geom, create_time, create_by, update_time, update_by) " +
//            "values(#{id}, #{name}, #{type}, #{layer_id}, #{color}, #{style}, #{width}, #{opacity}, #{symbol}, #{geom}, #{create_time}, #{create_by}, #{update_time}, #{update_by})")
//    @SelectKey(keyProperty = "id", resultType = String.class, before = true,
//            statement = "select replace(uuid(), '-', '') as id from dual")
//    int insert(PlotPoint2 plotPoint);
//
//    @Select({"select * from plot_point"})
//    List<PlotPoint2> findAll();
//}
