package cc.wanshan.gis.service.search;

import cc.wanshan.gis.entity.Result;

public interface SearchService {

    Result searchAreaName(double longitude, double latitude, double level);

    Result perfectField();
}
