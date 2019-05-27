package cc.wanshan.gis.service.thematic;

import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.thematic.Thematic;
import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/5/20 16:11
 */

public interface ThematicService {

  /**
   * description: 新增thematic
   *
   * @param thematic
   * @return
   */
  public Boolean insertThematic(Thematic thematic);
  /**
   * description: 根据专题id 查询专题图层信息
   *
   * @param thematicId 专题id
   * @return java.util.List<cc.wanshan.gis.entity.drawlayer.Layer>
   **/
  public Thematic findByThematicId(String thematicId);
}
