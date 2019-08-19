package cc.wanshan.gis.dao.road;

import cc.wanshan.gis.entity.road.Stations;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
/**
 * @author Li Cheng
 * @date 2019/6/13 9:37
 */
public interface NanJingStationsDao {

    /**
     * description: 根据起点查找附近公交站点
     *
     * @param point 起点坐标
     * @return cc.wanshan.gis.entity.road.Stations
     **/
    @Select({
            //"select ns.id,st_AsText(ns.the_geom) from xianyang_roads_vertices_pgr ns order by ns.the_geom <-> ST_geometryfromtext(#{point},4326) limit 1;"
            "select ns.id,st_AsText(ns.the_geom) from roads_vertices_pgr ns order by ns.the_geom <-> ST_geometryfromtext(#{point},4326) limit 1;"
    })
    @Results({
            @Result(id = true, column = "id", property = "gid"),
            //@Result(column = "name", property = "name"),
            @Result(column = "st_AsText", property = "geom")
    })
    Stations findStation(String point);
}
