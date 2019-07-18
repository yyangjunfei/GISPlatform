package cc.wanshan.gis.service.thematic;

import cc.wanshan.gis.entity.thematic.Thematic;
import cc.wanshan.gis.entity.thematic.ThematicUser;
import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/5/25 11:24
 */
public interface ThematicUserService {

  /**
   * description: 插入专题用户关联记录
   *
   * @param thematicUser 专题和用户关联对象
   * @return java.lang.Boolean
   **/
  Boolean insertThematicUser(ThematicUser thematicUser);

  /**
   * description: 修改专题用户关联记录
   *
   * @param thematicUser 专题和用户关联对象
   * @return java.lang.Boolean
   **/
  Boolean updateThematicUser(ThematicUser thematicUser);

  /**
   * description: 删除专题用户关联记录
   *
   * @param thematicUserId 专题和用户关联对象Id
   * @return java.lang.Boolean
   **/
  Boolean deleteThematicUser(String thematicUserId);

  /**
   * description: 根据用户Id查询专题图层记录
   *
   * @param userId 用户Id
   * @return java.util.List<cc.wanshan.gis.entity.drawlayer.Layer>
   **/
  List<Thematic> findThematicLayersByUserId(String userId);
}
