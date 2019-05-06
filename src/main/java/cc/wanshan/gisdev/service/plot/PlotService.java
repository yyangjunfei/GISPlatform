package cc.wanshan.gisdev.service.plot;

import cc.wanshan.gisdev.entity.Result;

public interface PlotService {

    Result save(String jsonString);

    Result findAll(String type);

    Result deleteById(String type, String id);

    Result update(String jsonString);
}
