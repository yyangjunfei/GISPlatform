package cc.wanshan.gis.dao.bus;

import cc.wanshan.gis.entity.bus.NanJingStations;
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
     * @return cc.wanshan.gis.entity.bus.NanJingStations
     **/
    @Select({
            "select ns.gid,ns.name,st_AsText(ns.geom) from nanjing_stations ns order by ns.geom <-> ST_geometryfromtext(#{point},4326) limit 1;"
    })
    @Results({
            @Result(id = true, column = "gid", property = "gid"),
            @Result(column = "name", property = "name"),
            @Result(column = "st_AsText", property = "geom")
    })
    NanJingStations findStation(String point);
}
