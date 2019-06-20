package cc.wanshan.gis.service.search;

import cc.wanshan.gis.entity.Result;
import com.alibaba.fastjson.JSONObject;

public interface SearchService {

    /**
     * 通过经纬度，级别获取行政区名称（数据库）
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param level     级别
     * @return
     */
    Result searchAreaName(double longitude, double latitude, double level);

    /**
     * 通过名称获取行政区的经纬度（数据库）
     *
     * @param name
     * @return
     */
    Result searchAreaGeo(String name);

    /**
     * 用户搜索信息入参：（level rectangle keyword）
     *
     * @param jsonObject level(级别) rectangle(经纬度范围) keyword(必选)
     * @return
     */
    Result searchPlace(JSONObject jsonObject);

    Result test();

}
