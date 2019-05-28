package cc.wanshan.gis.dao;

import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.thematic.Thematic;
import cc.wanshan.gis.entity.thematic.ThematicUser;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author Li Cheng
 * @date 2019/5/25 10:41
 */
@Mapper
@Component
public interface ThematicUserDao {

  @Insert("insert into "
      + "tb_thematic_user ("
      + "thematic_id,"
      + "user_id,"
      + "insert_time,"
      + "update_time "
      + ") values "
      + "("
      + "#{thematicId},"
      + "#{userId},"
      + "#{insertTime,jdbcType=TIMESTAMP},"
      + "#{updateTime,jdbcType=TIMESTAMP}"
      + ")"
  )
  /**
   * description: 新增专题和用户记录
   *
   * @param thematicUser 专题和用户关联对象
   * @return int 新增记录数
   **/
  public int insertThematicUser(ThematicUser thematicUser);

  @Update("update "
      + "tb_thematic_user "
      + "set "
      + "thematic_id =#{thematicId},"
      + "user_id=#{userId},"
      + "update_time=#{updateTime} "
      + "where "
      + "thematic_user_id=#{thematicUserId}"
  )
  /**
   * description: 根据thematicUserId修改专题和用户记录
   *
   * @param thematicUser 专题和用户关联对象
   * @return int 修改记录数
   **/
  public int updateThematicUser(ThematicUser thematicUser);

  @Delete("delete from "
      + "tb_thematic_user "
      + "where "
      + "thematic_user_id = #{thematicUserId}"
  )
  /**
   * description: 根据thematicUserId删除专题和用户记录
   *
   * @param thematicUser 专题和用户关联对象
   * @return int 删除记录数
   **/
  public int deleteThematicUser(String thematicUser);

  /**
   * description: 根据专题id查询关联用户
   *
   * @param thematicId 专题Id
   * @return java.util.List<cc.wanshan.gis.entity.thematic.ThematicUser>
   **/
  public List<ThematicUser> findThematicUserByThematicId(String thematicId);

  @Select({"select "
      + "t.thematic_id,"
      + "t.thematic_name,"
      + "t.thematic_name_zh "
      + "from "
      + "tb_thematic_user as tu "
      + "left outer join "
      + "tb_thematic as t "
      + "on "
      + "tu.thematic_id=t.thematic_id "
      + "where "
      + "tu.user_id=#{userId}"
  })
  @Results({
      @Result(id = true, column = "thematic_user_id", property = "thematicUserId"),
      @Result(column = "thematic_id", property = "thematicId"),
      @Result(column = "thematic_name", property = "thematicName"),
      @Result(column = "thematic_name_zh", property = "thematicNameZH"),
      @Result(column = "thematic_id", property = "firstClassificationList", many = @Many(select = "cc.wanshan.gis.dao.FirstClassificationDao.findByThematicId", fetchType = FetchType.LAZY)),
  })

  /**
   * description: 根据用户Id查询当前用户的关联图层
   *
   * @param userId 用户Id
   * @return java.util.List<cc.wanshan.gis.entity.drawlayer.Layer>
   **/
  public List<Thematic> findThematicLayersByUserId(String userId);
}
