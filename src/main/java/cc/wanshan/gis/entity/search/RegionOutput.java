package cc.wanshan.gis.entity.search;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegionOutput {

    private String type;

    private String name;

    private Long count;

    private String geometry;

    private String centroid;

    private List<Poi> poiList;

}
