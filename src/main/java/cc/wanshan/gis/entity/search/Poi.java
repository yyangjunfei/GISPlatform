package cc.wanshan.gis.entity.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Poi {

    //Poi
    private Integer id;
    private String provinceName;
    private String cityName;
    private String areaName;
    private String name;
    private String addr;
    private String firstName;
    private String secondName;
    private String phone;
    private String geometry;
    private Object geom;

}
