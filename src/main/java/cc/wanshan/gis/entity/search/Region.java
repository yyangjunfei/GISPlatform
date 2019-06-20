package cc.wanshan.gis.entity.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Region {

    private String geometry;

    private String geometryName;

    private RegionProperties properties;

}
