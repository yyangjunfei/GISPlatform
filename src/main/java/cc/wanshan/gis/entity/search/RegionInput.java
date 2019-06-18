package cc.wanshan.gis.entity.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegionInput {

    private String name;

    private String centroid;

}
