package cc.wanshan.gis.entity.thematic;

import cc.wanshan.gis.entity.drawlayer.Layer;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author Li Cheng
 * @date 2019/5/23 16:04
 */
@Data
public class SecondClassification {
  private static final long serialVersionUID = 1L;
  private String secondClassificationId;
  private FirstClassification firstClassification;
  private String secondClassificationName;
  private Layer layer;
  private String describe;
  private Date insertTime;
  private Date updateTime;
}
