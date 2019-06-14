package cc.wanshan.gis.entity.search;

import com.vividsolutions.jts.geom.Geometry;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegionOutput {

    private String name;

    private Geometry geom;

    private String geometry;

    private Long count;

    private List<Poi> poiList;

    private String centroid;

}
