package cc.wanshan.gisdev.service.thematicService;

import cc.wanshan.gisdev.entity.thematic.Thematic;

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
}
