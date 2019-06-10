package cc.wanshan.gis.entity.style;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class RuleValue implements Serializable {

  private static final long serialVersionUID = 1L;
  @NotBlank(message = "规则值id不为null")
  @Length(max = 32, message = "规则值id长度不可超过32字节")
  private String ruleValueId;
  @NotBlank(message = "规则值不为null")
  @Length(max = 32, message = "规则值长度不可超过32字节")
  private String ruleValue;
  @NotBlank(message = "风格规则不可为null")
  private RuleName ruleName;
  @NotNull(message = "插入时间不可为null")
  @Past(message = "插入日期必须为过去时间")
  private Date insertTime;
  @NotNull(message = "修改时间不可为null")
  @Past(message = "修改日期必须为过去时间")
  private Date updateTime;
  @Length(max = 8, message = "填充颜色值长度不可超过8字节")
  private String fillValue;
  @Length(max = 8, message = "边框颜色值长度不可超过8字节")
  private String strokeValue;
  @Length(max = 8, message = "宽度值长度不可超过8字节")
  private String widthValue;
  @Length(max = 8, message = "透明度值长度不可超过8字节")
  private String opacityValue;
  @Length(max = 8, message = "字体值长度不可超过8字节")
  private String fontFamilyValue;
  @Length(max = 8, message = "字体大小长度不可超过8字节")
  private String fontSizeValue;
  @Length(max = 8, message = "字体填充颜色值长度不可超过8字节")
  private String fontFillValue;
  @Length(max = 8, message = "字体描边颜色值长度不可超过8字节")
  private String fontStrokeValue;
  @Length(max = 8, message = "字体倾斜值长度不可超过8字节")
  private String fontStyleValue;
  @Length(max = 8, message = "字体加粗值长度不可超过8字节")
  private String fontWeightValue;
}
