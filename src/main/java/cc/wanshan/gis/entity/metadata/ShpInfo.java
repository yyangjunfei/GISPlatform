package cc.wanshan.gis.entity.metadata;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShpInfo implements Serializable {

    private Geometry geometry;
    private String id;
    private String type;
    private Properties properties;

    //方便数据库操作
    private String geometryJson;
}