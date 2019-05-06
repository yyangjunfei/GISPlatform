package cc.wanshan.gisdev.entity.plot;

import cc.wanshan.gisdev.common.constants.Constant;
import cc.wanshan.gisdev.common.factory.PlotFactory;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.HashMap;


public class Plot {

    private static HashMap<String, PlotFactory<? extends Plot>> map = Maps.newHashMap();

    static {
        map.put(Constant.GEO_POINT, new PlotPoint());
        map.put(Constant.GEO_LINESTRING, new PlotLine());
        map.put(Constant.GEO_POLYGON, new PlotPolygon());
    }

    public static Plot create(JSONObject jsonString) throws IOException {
        String type = jsonString.getString(Constant.TYPE);
        if (null == map.get(type)) {
            return null;
        }
        return map.get(type).create(jsonString.toJSONString());
    }
}
