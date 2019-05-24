package cc.wanshan.gis.dao;

import cc.wanshan.gis.entity.thematic.Thematic;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

/**
 * @author Li Cheng
 * @date 2019/5/18 16:57
 */
/**
 thematic_id character varying(32) NOT NULL DEFAULT replace(uuid_generate_v1()::text,'-',''),
 thematic_name character varying(20) COLLATE pg_catalog."default" NOT NULL,
 thematic_name_zh character varying(32) COLLATE pg_catalog."default" NOT NULL,
 insert_time timestamp(0) with time zone NOT NULL,
 update_time timestamp(0) with time zone NOT NULL,
 describe character varying(100) COLLATE pg_catalog."default",
 CONSTRAINT tb_thematic_pkey PRIMARY KEY (thematic_id)
 **/
@Mapper
@Component
public interface ThematicDao {

  @Select({
      "select "
          + "thematic_id,"
          + "thematic_name,"
          + "thematic_name_zh,"
          + "insert_time,"
          + "update_time,"
          + "describe "
          + "from "
          + "tb_thematic "
          + "where "
          + "thematic_id=#{thematicId}"
  })
  @Results({
      @Result(id = true, column = "thematic_id", property = "thematicId"),
      @Result(column = "thematic_name", property = "thematicName"),
      @Result(column = "thematic_name_zh", property = "thematicNameZH"),
      @Result(column = "describe", property = "describe"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
      //@Result(column = "thematic_id", property = "userList", many = @Many(select = "cc.wanshan.demo.repository.UserDao.findUsersByThematicId", fetchType = FetchType.LAZY)),
  })
  /**
   * description: 根据thematicId查找Thematic
   *
   * @param thematicId
   * @return cc.wanshan.gisdev.entity.thematic.Thematic
   */
  public Thematic findByThematicId(String thematicId);


  @Insert({
      "insert into "
          + "tb_thematic "
          + "(thematic_name,"
          + "thematic_name_zh,"
          + "insert_time,"
          + "update_time,"
          + "describe) "
          + "values "
          + "(#{thematicName},"
          + "#{thematicNameZH},"
          + "#{insertTime,jdbcType=TIMESTAMP},"
          + "#{updateTime,jdbcType=TIMESTAMP},"
          + "#{describe})"
  })
  @Options(useGeneratedKeys = true, keyColumn = "thematic_id", keyProperty = "thematicId")
  /**
   * description:新增thematic
   *
   * @param thematic
   * @return int
   */
  public int insertThematic(Thematic thematic);

  @Update({
      "update "
          + "tb_thematic "
          + "set "
          + "thematic_id=#{thematicId},"
          + "thematic_name=#{thematicName},"
          + "thematic_name_zh=#{thematicNameZH},"
          + "describe={#describe},"
          + "update_time={#updateTime} "
          + "where "
          + "thematic_id=#{thematicId}"
  })
  /**
   * description:更新thematic
   *
   * @param thematic
   * @return int
   */
  public int updateThematic(Thematic thematic);

  @Delete({
      "delete from "
          + "tb_thematic "
          + "where "
          + "thematic_id=#{thematicId}"
  })
  /**
   * description:根据thematicId删除thematic
   *
   * @param thematicId
   * @return int
   */
  public int deleteThematic(String thematicId);

  @Select({
      "select "
          + "* "
          + "from "
          + "tb_thematic"
          + "where "
          + "thematic_id=("
          + "select  "
          + "u.thematic_id"
          + "from "
          + "tb_user as u "
          + "where "
          + "u.username =#{username})"
  })
  @Results({
      @Result(id = true, column = "thematic_id", property = "thematicId"),
      @Result(column = "thematic_name", property = "thematicName"),
      @Result(column = "thematic_name_zh", property = "thematicNameZH"),
      @Result(column = "describe", property = "describe"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
  })
  /**
   * description:根据用户名查找thematic
   *
   * @param username
   * @return cc.wanshan.gisdev.entity.usermanagement.thematic
   */
  public Thematic findThematicByUserName(String username);

  @Select({
      "select "
          + "thematic_id ,"
          + "thematic_name,"
          + "thematic_name_zh,"
          + "describe "
          + "insert_time,"
          + "update_time "
          + "from "
          + "tb_thematic"})
  @Results({
      @Result(id = true, column = "thematic_id", property = "thematicId"),
      @Result(column = "thematic_name", property = "thematicName"),
      @Result(column = "thematic_name_zh", property = "thematicNameZH"),
      @Result(column = "describe", property = "describe"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
      @Result(column = "thematic_id", property = "userList", many = @Many(select = "cc.wanshan.demo.repository.UserDao.findUsersByThematicId", fetchType = FetchType.LAZY))
  })
  /**
   * description: 查询所有的thematic
   *
   * @param
   * @return java.util.List<cc.wanshan.gisdev.entity.usermanagement.thematic>
   */
  public List<Thematic> findAllThematic();

  @Select({
      "select"
          + "thematic_id,"
          + "thematic_name,"
          + "thematic_name_zh,"
          + "describe,"
          + "insert_time,"
          + "update_time "
          + "from "
          + "tb_thematic "
          + "where "
          + "thematic_name=#{thematicName}"
  })
  @Results({
      @Result(id = true, column = "thematic_id", property = "thematicId"),
      @Result(column = "thematic_name", property = "thematicName"),
      @Result(column = "thematic_name_zh", property = "thematicNameZH"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
      @Result(column = "describe", property = "describe")
  })
  /**
   * description: 根据thematicName查询thematic
   *
   * @param thematicName
   * @return cc.wanshan.gisdev.entity.usermanagement.thematic
   */
  public Thematic findThematicByThematicName(String thematicName);

  @Select({
      "select "
          + "count(*) "
          + "from "
          + "tb_thematic "
          + "where "
          + "thematic_name=#{thematicName}"
  })
  /**
   * description:判断thematic是否已存在
   *
   * @param thematicName
   * @return int
   */
  public int findCountByThematicName(String thematicName);


  @Select({
      "select "
          + "thematic_id,"
          + "thematic_name,"
          + "thematic_name_zh,"
          + "describe,"
          + "insert_time,"
          + "update_time "
          + "from "
          + "tb_thematic "
          + "where "
          + "thematic_name_zh=#{thematicNameZH}"
  })
  @Results({
      @Result(id = true, column = "thematic_id", property = "thematicId"),
      @Result(column = "thematic_name", property = "thematicName"),
      @Result(column = "thematic_name_zh", property = "thematicNameZH"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
      @Result(column = "describe", property = "describe")
  })
  /**
   * description:根据thematicNameZh查询thematic
   *
   * @param thematicNameZH
   * @return cc.wanshan.gisdev.entity.usermanagement.thematic
   */
  public Thematic findThematicByThematicNameZH(String thematicNameZH);

  @Select({
      "select "
          + "count(*) "
          + "from "
          + "tb_thematic "
          + "where "
          + "thematic_name_zh=#{thematicNameZH}"
  })
  /**
   * description:判断thematicNameZH是否存在
   *
   * @param thematicNameZH
   * @return int
   */
  public int findCountByThematicNameZH(String thematicNameZH);

  @Select({
      "select "
          + "r.thematic_name "
          + "from tb_authority_thematic as ar "
          + "inner join "
          + "tb_thematic as r "
          + "on "
          + "ar.thematic_id=r.thematic_id "
          + "where "
          + "ar.authothematic_id=#{authorId}"
  })
  @Results({
      @Result(column = "thematic_name", property = "thematicName")
  })
  /**
   * description: 根据authorId查询store
   *
   * @param authorId
   * @return java.util.List<cc.wanshan.gisdev.entity.usermanagement.thematic>
   */
  public List<Thematic> findByAuthorId(String authorId);
}
