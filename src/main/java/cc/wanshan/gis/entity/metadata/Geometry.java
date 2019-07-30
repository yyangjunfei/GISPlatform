package cc.wanshan.gis.entity.metadata;

import lombok.Data;

import java.util.List;

@Data
public class Geometry {
    private List<Double> coordinates;
    private String type;
}
