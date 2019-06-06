package cc.wanshan.gis.entity.drawlayer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)	//注解控制null不序列化
public class Feature implements Serializable {
    private static final long serialVersionUID = 1L;
    private int geoId;
    @NotNull(message = "geometry不可为null")
    private Object geometry;
    @NotBlank(message = "geometry不可为null")
    private Properties properties;
}
