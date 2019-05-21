package cc.wanshan.gis.entity.drawlayer;

import javax.validation.constraints.NotNull;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
public class Feature implements Serializable {
    private static final long serialVersionUID = 1L;
    private int geoId;
    @NotNull(message = "geometry不可为null")
    private Object geometry;
    @NotBlank(message = "geometry不可为null")
    private Properties properties;
}
