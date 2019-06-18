package cc.wanshan.gis.entity.thematic;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Data;

/**
 * @author Li Cheng
 * @date 2019/5/25 10:39
 */
@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)	//注解控制null不序列化
public class ThematicUser {
  private static final long serialVersionUID = 1L;
  private String thematicUserId;
  private String thematicId;
  private String userId;
  private Date insertTime;
  private Date updateTime;
}
