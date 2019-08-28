package cc.wanshan.gis.dao.road;

import cc.wanshan.gis.entity.road.Road;
import cc.wanshan.gis.entity.road.Stations;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Component;


/**
 * @author Li Cheng
 * @date 2019/8/14 8:34
 */
@Mapper
@Component
public interface RoadDao {

  /**
   * description: 根据起点坐标获得最近点公交路线索引
   *
   * @param sourcePoint 起点坐标
   * @return int
   **/
  @Select({
      //"select nl.source from xianyang_roads nl order by nl.geom <-> st_geometryfromtext(#{sourcePoint},4326) limit 1"
      "select nl.source from roads nl order by nl.geom <-> st_geometryfromtext(#{sourcePoint},4326) limit 1"
      //"select nl.source from xianyangroad nl order by nl.geom <-> st_geometryfromtext(#{sourcePoint},4326) limit 1"
  })
  @Results({
      @Result(id = true, column = "gid", property = "gid"),
      @Result(column = "source", property = "source")
  })
  Road findSource(String sourcePoint);

  /**
   * description: 根据终点坐标获得最近点公交路线索引
   *
   * @return int
   **/
  @Select({
      //"select nl.target from xianyang_roads nl order by nl.geom <-> st_geometryfromtext(#{targetPoint},4326) limit 1"
      "select nl.target from roads nl order by nl.geom <-> st_geometryfromtext(#{targetPoint},4326) limit 1"
      //"select nl.target from xianyangroad nl order by nl.geom <-> st_geometryfromtext(#{targetPoint},4326) limit 1"
  })
  @Results({
      @Result(id = true, column = "gid", property = "gid"),
      @Result(column = "target", property = "target")
  })
  Road findTarget(String targetPoint);

  /**
   * description: 根据起点查找附近公交站点
   *
   * @param point 起点坐标
   * @return cc.wanshan.gis.entity.road.Stations
   **/
  @Select({
      //"select ns.id,st_AsText(ns.the_geom) from xianyang_roads_vertices_pgr ns order by ns.the_geom <-> ST_geometryfromtext(#{point},4326) limit 1;"
      "select ns.id,st_AsText(ns.the_geom) from roads_vertices_pgr ns order by ns.the_geom <-> ST_geometryfromtext(#{point},4326) limit 1;"
      //"select ns.id,st_AsText(ns.the_geom) from xianyangroad_vertices_pgr ns order by ns.the_geom <-> ST_geometryfromtext(#{point},4326) limit 1;"
  })
  @Results({
      @Result(id = true, column = "id", property = "gid"),
      //@Result(column = "name", property = "name"),
      @Result(column = "st_AsText", property = "geom")
  })
  Stations findStation(String point);


  @Select({
      //"select ns.id,st_AsText(ns.the_geom) from xianyang_roads_vertices_pgr ns order by ns.the_geom <-> ST_geometryfromtext(#{point},4326) limit 1;"
      "select ST_AsText(st_linemerge) from table_sgt"
  })
  @Options(statementType = StatementType.STATEMENT)
  @Results({
      //@Result( column = "gid", property = "seq"),
      //@Result(column = "name", property = "name"),
      @Result(column = "ST_AsText", property = "st_linemerge")
  })
  /**
   * description: 查询规划路线
   *
   * @param source 起点索引
   * @param target 终点索引
   * @return cc.wanshan.gis.entity.road.Road
   **/
  String  findRoad();

  @Update({
      "create OR REPLACE VIEW "
          + "table_sgt "
          + "as(SELECT st_linemerge(st_union(l.geom)) from (select seq,geom  from pgr_dijkstra('"
          + "select gid as id,"
          + "source::integer,"
          + "target::integer,"
          + "length::double precision as cost,"
          + "rev_length::double precision as reverse_cost "
          + "from roads ',"
          + "${source},${target},true,true) as di "
          + "join roads pt "
          + "on di.id2 = pt.gid ORDER BY seq,geom) l where 1=1);"
  })
  //@Options(statementType = StatementType.STATEMENT)
  int update(Integer source, Integer target);
}
