package cc.wanshan.gis.entity.style;

import cc.wanshan.gis.entity.drawlayer.Layer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;

@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)	//注解控制null不序列化
public class RuleName implements Serializable {

  private static final long serialVersionUID = 1L;
  @NotBlank(message = "规则id不为null")
  @Length(max = 32, message = "规则id长度不可为32字节")
  private String ruleNameId;
  @NotBlank(message = "风格不可为null")
  private Style style;
  @NotNull(message = "插入时间不可为null")
  @NotNull(message = "图层不可为null")
  private Layer layer;
  @Past(message = "插入日期必须为过去时间")
  private Date insertTime;
  @NotNull(message = "修改时间不可为null")
  @Past(message = "修改日期必须为过去时间")
  private Date updateTime;
  @Length(max = 8, message = "填充颜色别名长度不可超过8字节")
  private String fillEnv;
  @Length(max = 8, message = "填充颜色别名长度不可超过8字节")
  private String strokeEnv;
  @Length(max = 8, message = "填充颜色别名长度不可超过8字节")
  private String widthEnv;
  @Length(max = 8, message = "填充颜色别名长度不可超过8字节")
  private String opacityEnv;
  @Length(max = 8, message = "填充颜色别名长度不可超过8字节")
  private String fontFamilyEnv;
  @Length(max = 8, message = "填充颜色别名长度不可超过8字节")
  private String fontSizeEnv;
  @Length(max = 8, message = "填充颜色别名长度不可超过8字节")
  private String fontFillEnv;
  @Length(max = 8, message = "填充颜色别名长度不可超过8字节")
  private String fontStrokeEnv;
  @Length(max = 8, message = "填充颜色别名长度不可超过8字节")
  private String fontStyleEnv;
  @Length(max = 8, message = "填充颜色别名长度不可超过8字节")
  private String fontWeightEnv;
  private RuleValue ruleValue;
}
