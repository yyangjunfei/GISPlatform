package cc.wanshan.gis.entity.road;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)  //注解控制null不序列化
/**
 * @author Li Cheng
 * @date 2019/6/12 16:59
 */
public class Road {

  private static final long serialVersionUID = 1L;
  private Integer gid;
  private Integer source;
  private Integer target;
  private String geom;
}
