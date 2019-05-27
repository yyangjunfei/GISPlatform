package cc.wanshan.gis.entity.thematic;


import java.util.Date;
import lombok.Data;

/**
 * @author Li Cheng
 * @date 2019/5/25 10:39
 */
@Data
public class ThematicUser {
  private static final long serialVersionUID = 1L;
  private String thematicUserId;
  private String thematicId;
  private String userId;
  private Date insertTime;
  private Date updateTime;
}
