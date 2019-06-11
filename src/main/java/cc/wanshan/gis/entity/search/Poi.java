package cc.wanshan.gis.entity.search;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Poi {

    private String geometry;

    private String geometryName;

    private List<Properties> properties;

}
