package cc.wanshan.gis.entity.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Poi {

    private String geometry;

    private String geometryName;

    private Properties properties;

}
