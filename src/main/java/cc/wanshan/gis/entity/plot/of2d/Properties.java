package cc.wanshan.gis.entity.plot.of2d;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)    //注解控制null不序列化
public class Properties implements Serializable {

    private static final long serialVersionUID = 1L;
    private int code;
    private String fclass;
    private String name;
    private String osmId;

}
