package cc.wanshan.gis.entity.metadata;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Geometry {
    private List<Double> coordinates;
    private String type;
}
