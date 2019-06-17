package cc.wanshan.gis.service.search;

import cc.wanshan.gis.entity.Result;
import com.alibaba.fastjson.JSONObject;

public interface SearchService {

    Result searchAreaName(double longitude, double latitude, double level);

    Result searchAreaGeo(String name);

    Result searchAreaGeoFromES(String name);

    Result searchPlace(JSONObject jsonObject);

    Result test();

}
