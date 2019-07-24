package cc.wanshan.gis.service.plot.of3d;

import cc.wanshan.gis.common.pojo.Result;

public interface PlotService {

    Result save(String jsonString);

    Result findAll(String type);

    Result deleteById(String type, String id);

    Result update(String jsonString);
}
