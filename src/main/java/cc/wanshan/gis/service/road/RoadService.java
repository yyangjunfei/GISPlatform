package cc.wanshan.gis.service.road;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.road.Stations;

/**
 * @author Li Cheng
 * @date 2019/8/14 8:38
 */
public interface RoadService {
    /**
     * description: 根据起点坐标查询最近索引
     *
     * @param sourcePoint 起点坐标
     * @return java.lang.Integer
     **/
    Integer findSource(String sourcePoint);
    /**
     * description: 根据中点坐标查询最近索引
     *
     * @param targetPoint 终点坐标
     * @return java.lang.Integer
     **/
    Integer findTarget(String targetPoint);
    /**
     * description: 根据坐标点查询公交站
     *
     * @param point 坐标点
     * @return cc.wanshan.gis.entity.road.Stations
     **/
    Stations findStation(String point);
    /**
     * description: 查询规划路径
     *
     * @param sourcePoint 起点坐标
    	* @param targetPoint 终点坐标
     * @return cc.wanshan.gis.common.pojo.Result
     **/
    Result findRoad(String sourcePoint,String  targetPoint);
}
