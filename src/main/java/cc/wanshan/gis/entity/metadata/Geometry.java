package cc.wanshan.gis.entity.metadata;

import lombok.Data;

import java.util.List;

@Data
public class Geometry<T> {
    private List<T> coordinates;
    private String type;
}
