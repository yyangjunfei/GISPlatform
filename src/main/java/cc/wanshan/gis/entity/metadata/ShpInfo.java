package cc.wanshan.gis.entity.metadata;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShpInfo {
    private Geometry geometry;
    private String id;
    private String type;
    private Properties properties;
}
