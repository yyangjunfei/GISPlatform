package cc.wanshan.gisdev.dao;

/**
 * @author Li Cheng
 * @date 2019/5/20 8:37
 */


import cc.wanshan.gisdev.entity.style.Style;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;

/**
 * style_id character varying(32) NOT NULL DEFAULT replace(uuid_generate_v1()::text,'-',''),
 * style_name character varying(32) COLLATE pg_catalog."default" NOT NULL, layer_id character
 * varying(32) COLLATE pg_catalog."default" NOT NULL, insert_time timestamp(0) with time zone NOT
 * NULL, update_time timestamp(0) with time zone NOT NULL, CONSTRAINT fkcgvb4rn18kaq4pxb0piueexx7
 * FOREIGN KEY (layer_id) REFERENCES public.tb_layer (layer_id) MATCH SIMPLE ON UPDATE NO ACTION ON
 * DELETE NO ACTION, CONSTRAINT tb_style_layer_id_fkey FOREIGN KEY (layer_id) REFERENCES
 * public.tb_layer (layer_id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE, CONSTRAINT
 * tb_style_pkey PRIMARY KEY (style_id)
 **/
public interface StyleDao {

  /*@Select({
      "select "
          + "style_id,"
          + "style_name,"
          + "style_name_zh,"
          + "insert_time,"
          + "update_time,"
          + "describe "
          + "from "
          + "tb_style "
          + "where "
          + "style_id=#{styleId}"
  })
  @Results({
      @Result(id = true, column = "style_id", property = "styleId"),
      @Result(column = "style_name", property = "styleName"),
      @Result(column = "style_name_zh", property = "styleNameZH"),
      @Result(column = "describe", property = "describe"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
      //@Result(column = "style_id", property = "userList", many = @Many(select = "cc.wanshan.demo.repository.UserDao.findUsersBystyleId", fetchType = FetchType.LAZY)),
  })
  *//**
   * description: 根据styleId查找style
   *
   * @param styleId
   * @return cc.wanshan.gisdev.entity.style.style
   *//*
  public Style findByStyleId(String styleId);

  @Insert({
      "insert into "
          + "tb_style "
          + "(style_name,"
          + "style_name_zh,"
          + "insert_time,"
          + "update_time,"
          + "describe) "
          + "values "
          + "(#{styleName},"
          + "#{styleNameZH},"
          + "#{insertTime,jdbcType=TIMESTAMP},"
          + "#{updateTime,jdbcType=TIMESTAMP},"
          + "#{describe})"
  })
  @Options(useGeneratedKeys = true, keyColumn = "style_id", keyProperty = "styleId")
  *//**
   * description:新增style
   *
   * @param style
   * @return int
   *//*
  public int insertStyle(Style style);

  @Update({
      "update "
          + "tb_style "
          + "set "
          + "style_id=#{styleId},"
          + "style_name=#{styleName},"
          + "style_name_zh=#{styleNameZH},"
          + "describe={#describe},"
          + "update_time={#updateTime} "
          + "where "
          + "style_id=#{styleId}"
  })
  *//**
   * description:更新style
   *
   * @param style
   * @return int
   *//*
  public int updateStyle(Style style);

  @Delete({
      "delete from "
          + "tb_style "
          + "where "
          + "style_id=#{styleId}"
  })
  *//**
   * description:根据styleId删除style
   *
   * @param styleId
   * @return int
   *//*
  public int deleteStyle(String styleId);

  @Select({
      "select "
          + "* "
          + "from "
          + "tb_style"
          + "where "
          + "style_id=("
          + "select  "
          + "u.style_id"
          + "from "
          + "tb_user as u "
          + "where "
          + "u.username =#{username})"
  })
  @Results({
      @Result(id = true, column = "style_id", property = "styleId"),
      @Result(column = "style_name", property = "styleName"),
      @Result(column = "style_name_zh", property = "styleNameZH"),
      @Result(column = "describe", property = "describe"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
  })
  *//**
   * description:根据用户名查找style
   *
   * @param username
   * @return cc.wanshan.gisdev.entity.usermanagement.style
   *//*
  public style findstyleByUserName(String username);

  @Select({
      "select "
          + "style_id ,"
          + "style_name,"
          + "style_name_zh,"
          + "describe "
          + "insert_time,"
          + "update_time "
          + "from "
          + "tb_style"})
  @Results({
      @Result(id = true, column = "style_id", property = "styleId"),
      @Result(column = "style_name", property = "styleName"),
      @Result(column = "style_name_zh", property = "styleNameZH"),
      @Result(column = "describe", property = "describe"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
      @Result(column = "style_id", property = "userList", many = @Many(select = "cc.wanshan.demo.repository.UserDao.findUsersBystyleId", fetchType = FetchType.LAZY))
  })
  *//**
   * description: 查询所有的style
   *
   * @param
   * @return java.util.List<cc.wanshan.gisdev.entity.usermanagement.style>
   *//*
  public List<style> findAllstyle();

  @Select({
      "select"
          + "style_id,"
          + "style_name,"
          + "style_name_zh,"
          + "describe,"
          + "insert_time,"
          + "update_time "
          + "from "
          + "tb_style "
          + "where "
          + "style_name=#{styleName}"
  })
  @Results({
      @Result(id = true, column = "style_id", property = "styleId"),
      @Result(column = "style_name", property = "styleName"),
      @Result(column = "style_name_zh", property = "styleNameZH"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
      @Result(column = "describe", property = "describe")
  })
  *//**
   * description: 根据styleName查询style
   *
   * @param styleName
   * @return cc.wanshan.gisdev.entity.usermanagement.style
   *//*
  public style findstyleBystyleName(String styleName);

  @Select({
      "select "
          + "count(*) "
          + "from "
          + "tb_style "
          + "where "
          + "style_name=#{styleName}"
  })
  *//**
   * description:判断style是否已存在
   *
   * @param styleName
   * @return int
   *//*
  public int findCountBystyleName(String styleName);


  @Select({
      "select "
          + "style_id,"
          + "style_name,"
          + "style_name_zh,"
          + "describe,"
          + "insert_time,"
          + "update_time "
          + "from "
          + "tb_style "
          + "where "
          + "style_name_zh=#{styleNameZH}"
  })
  @Results({
      @Result(id = true, column = "style_id", property = "styleId"),
      @Result(column = "style_name", property = "styleName"),
      @Result(column = "style_name_zh", property = "styleNameZH"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
      @Result(column = "describe", property = "describe")
  })
  *//**
   * description:根据styleNameZh查询style
   *
   * @param styleNameZH
   * @return cc.wanshan.gisdev.entity.usermanagement.style
   *//*
  public style findstyleBystyleNameZH(String styleNameZH);

  @Select({
      "select "
          + "count(*) "
          + "from "
          + "tb_style "
          + "where "
          + "style_name_zh=#{styleNameZH}"
  })
  *//**
   * description:判断styleNameZH是否存在
   *
   * @param styleNameZH
   * @return int
   *//*
  public int findCountBystyleNameZH(String styleNameZH);

  @Select({
      "select "
          + "r.style_name "
          + "from tb_authority_style as ar "
          + "inner join "
          + "tb_style as r "
          + "on "
          + "ar.style_id=r.style_id "
          + "where "
          + "ar.authostyle_id=#{authorId}"
  })
  @Results({
      @Result(column = "style_name", property = "styleName")
  })
  *//**
   * description: 根据authorId查询store
   *
   * @param authorId
   * @return java.util.List<cc.wanshan.gisdev.entity.usermanagement.style>
   *//*
  public List<style> findByAuthorId(String authorId);*/
}
