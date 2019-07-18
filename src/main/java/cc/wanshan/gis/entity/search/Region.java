package cc.wanshan.gis.entity.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Region {

    private int gid;

    private String name;

    private String centroid;

    private String envelope;

    private String rectangle;

    private String autocomplete;

//    private String remarks;

    private String geometry;
}
