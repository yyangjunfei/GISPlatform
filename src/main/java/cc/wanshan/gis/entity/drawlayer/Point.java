package cc.wanshan.gis.entity.drawlayer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Li Cheng
 * @date 2019/6/3 11:07
 */
@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)	//注解控制null不序列化
public class Point {
  private static final long serialVersionUID = 1L;
  private String featureId;
  private String featureName;
  private Object geom;
  private String featureClass;
  private Layer layer;
  @NotNull(message = "修改时间不可为null")
  @Past(message = "修改时间必须为过去时间")
  private Date updateTime;
  @NotNull(message = "插入时间不可为null")
  @Past(message = "插入时间时间必须为过去时间")
  private Date insertTime;
  @NotBlank(message = "坐标系不可为null")
  @Length(max = 4,message = "坐标系长度不可超过4字节")
  private String epsg;
  private String describe;
  private String fillColor;
  private String strokeColor;
  private String strokeWidth;
  private Integer opacity;
}
