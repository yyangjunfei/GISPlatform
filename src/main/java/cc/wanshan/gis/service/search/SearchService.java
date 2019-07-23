package cc.wanshan.gis.service.search;

import cc.wanshan.gis.common.pojo.Result;
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
    Result searchByLocation(double longitude, double latitude, double level);

    /**
     * 通过名称获取行政区的经纬度（数据库）
     *
     * @param name 行政名称
     * @return
     */
    Result searchByName(String name);

    /**
     * 用户搜索信息入参：（level rectangle keyword）
     *
     * @param jsonObject level(级别) rectangle(经纬度范围) keyword(必选)
     * @return
     */
    Result searchByPlace(JSONObject jsonObject);

    /**
     * 根据关键字联想自动补全
     *
     * @param keyword 关键字
     * @return
     */
    Result getSuggestSearch(String keyword);

    /**
     * 调测
     *
     * @return
     */
    Result test();

}
