package cc.wanshan.gis.entity.layer.geoserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Li Cheng
 * @Description UserProps用来维护geoserver用户授权key
 * @date 2019/7/17 14:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = {"handler"}) //排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL) //注解控制null不序列化
public class UserProps implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private String propName;
    private String propValue;

}
