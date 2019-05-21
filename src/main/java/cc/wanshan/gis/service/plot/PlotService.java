package cc.wanshan.gis.service.plot;

import cc.wanshan.gis.entity.Result;

public interface PlotService {

    Result save(String jsonString);

    Result findAll(String type);

    Result deleteById(String type, String id);

    Result update(String jsonString);
}
