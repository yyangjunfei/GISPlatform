package cc.wanshan.gis.entity.road;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)  //注解控制null不序列化
/**
 * @author Li Cheng
 * @date 2019/6/13 9:11
 */
public class Stations {
  private static final long serialVersionUID = 1L;
  private Integer gid;
  private String name;
  private String geom;
}
