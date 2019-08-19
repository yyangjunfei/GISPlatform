package cc.wanshan.gis.dao.road;

import cc.wanshan.gis.entity.road.Road;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author Li Cheng
 * @date 2019/6/12 17:04
 */
@Mapper
@Component
public interface NanJingLines2Dao {
    /**
     * description: 根据起点坐标获得最近点公交路线索引
     *
     * @param sourcePoint 起点坐标
     * @return int
     **/
    @Select({
            //"select nl.source from xianyang_roads nl order by nl.geom <-> st_geometryfromtext(#{sourcePoint},4326) limit 1"
            "select nl.source from roads nl order by nl.geom <-> st_geometryfromtext(#{sourcePoint},4326) limit 1"
    })
    @Results({
            @Result(id = true, column = "gid", property = "gid"),
            @Result(column = "source", property = "source")
    })
    Road findSource(String sourcePoint);
    /**
     * description: 根据终点坐标获得最近点公交路线索引
     *
     * @param targetPoint
     * @return int
     **/
    @Select({
            //"select nl.target from xianyang_roads nl order by nl.geom <-> st_geometryfromtext(#{targetPoint},4326) limit 1"
            "select nl.target from roads nl order by nl.geom <-> st_geometryfromtext(#{targetPoint},4326) limit 1"
    })
    @Results({
            @Result(id = true, column = "gid", property = "gid"),
            @Result(column = "target", property = "target")
    })
    Road findTarget(String targetPoint);
}
