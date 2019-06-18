package cc.wanshan.gis.dao;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * @author Li Cheng
 * @date 2019/6/12 16:46
 */
public interface StationDao {
  @Select({
      "select nl.source from nanjing_lines2 nl order by nl.geom <-> st_geomfromgeojson({#sourcePoint}) limit 1"
  })
  @Results({
      @Result(id = true,column = "gid")
  })
  /**
   * description: 根据起点坐标获得最近点公交路线索引
   *
   * @param sourcePoint 起点坐标
   * @return int
   **/
  public int findSource(String sourcePoint);
}
