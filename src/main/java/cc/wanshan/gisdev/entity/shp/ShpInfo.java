package cc.wanshan.gisdev.entity.shp;

import lombok.Data;

@Data
public class ShpInfo {

    private int gid;
    private String osm_id;
    private String fclass;
    private int code;
    private String name;
    private String type;
    private Object geom;
}
