package cc.wanshan.gis.service.bus;

import cc.wanshan.gis.entity.bus.NanJingStations;

/**
 * @author Li Cheng
 * @date 2019/6/12 17:34
 */
public interface BusService {
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
   * @return cc.wanshan.gis.entity.bus.NanJingStations
   **/
  NanJingStations findStation(String point);
}
