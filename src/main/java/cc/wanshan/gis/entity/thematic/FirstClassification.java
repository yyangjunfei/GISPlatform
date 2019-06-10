package cc.wanshan.gis.entity.thematic;

import cc.wanshan.gis.entity.drawlayer.Layer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author Li Cheng
 * @date 2019/5/23 16:04
 */
@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)	//注解控制null不序列化
public class FirstClassification {
  private static final long serialVersionUID = 1L;
  private String firstClassificationId;
  private String firstClassificationName;
  private Thematic thematic;
  private List<SecondClassification> secondClassificationList;
  private String describe;
  private Date insertTime;
  private Date updateTime;
}
