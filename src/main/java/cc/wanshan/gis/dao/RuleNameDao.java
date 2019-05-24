package cc.wanshan.gis.dao;

import cc.wanshan.gis.entity.style.RuleName;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @author Li Cheng
 * @date 2019/5/18 17:15
 */
/**
 rule_name_id character varying(32) NOT NULL DEFAULT replace(uuid_generate_v1()::text,'-',''),
 style_id character varying(32) NOT NULL,
 fill_env character varying(16) COLLATE pg_catalog."default",
 stroke_env character varying(16) COLLATE pg_catalog."default",
 width_env character varying(16) COLLATE pg_catalog."default",
 opacity_env character varying(16) COLLATE pg_catalog."default",
 font_family_env character varying(16) COLLATE pg_catalog."default",
 font_size_env character varying(16) COLLATE pg_catalog."default",
 font_fill_env character varying(16) COLLATE pg_catalog."default",
 font_stroke_env character varying(16) COLLATE pg_catalog."default",
 font_style_env character varying(16) COLLATE pg_catalog."default",
 font_weight_env character varying(16) COLLATE pg_catalog."default",
 insert_time timestamp(0) with time zone NOT NULL,
 update_time timestamp(0) with time zone NOT NULL,
 CONSTRAINT fkcgvb4rn18kaq4pxb0piueexx7 FOREIGN KEY (style_id)
 REFERENCES public.tb_style (style_id) MATCH SIMPLE
 ON UPDATE NO ACTION
 ON DELETE NO ACTION,
 CONSTRAINT tb_rule_name_style_id_fkey FOREIGN KEY (style_id)
 REFERENCES public.tb_style (style_id) MATCH SIMPLE
 ON UPDATE CASCADE
 ON DELETE CASCADE,
 CONSTRAINT tb_rule_name_pkey PRIMARY KEY (rule_name_id)
 **/
@Mapper
@Component
public interface RuleNameDao {

  @Delete({
      "delete from "
          + "tb_ruleName "
          + "where "
          + "ruleName_id=#{ruleNameId} and "
          + "style_id=#{styleId}"
  })
  /**
   * description: 根据layerId和ruleNameId删除ruleName
   *
   * @param ruleNameId
   * @param styleId
   * @return int
   */
  public int deleteRuleNameByRuleNameIdAndLayerId(String ruleNameId, String styleId);
  
  @Insert({
      "insert into "
          + "tb_ruleName ("
          + "style_id,"
          + "fill_env,"
          + "stroke_env,"
          + "width_env,"
          + "opacity_env,"
          + "font_family_env,"
          + "font_size_env,"
          + "font_fill_env,"
          + "font_stroke_env,"
          + "font_style_env,"
          + "font_weight_env,"
          + "insert_time,"
          + "update_time "
          + ") values ("
          + "#{style.styleId},"
          + "#{fillEnv},"
          + "#{strokeEnv},"
          + "#{widthEnv},"
          + "#{opacityEnv},"
          + "#{fontFamilyEnv},"
          + "#{fontSizeEnv},"
          + "#{fontFillEnv},"
          + "#{fontStrokeEnv},"
          + "#{fontStyleEnv},"
          + "#{fontWeightEnv},"
          + "#{insertTime},"
          + "#{updateTime}"
  })
  @Options(useGeneratedKeys = true, keyColumn = "ruleName_id", keyProperty = "ruleNameId")
  /**
   * description: 新增图层
   *
   * @param ruleName
   * @return int
   */
  public int insertRuleName(RuleName ruleName);

  @Update({
      "update "
          + "tb_ruleName "
          + "set "
          + "style_id=#{styleId},"
          + "fill_env=#{fillEnv},"
          + "stroke_env=#{strokeEnv},"
          + "width_env=#{widthEnv},"
          + "opacity_env=#{opacityEnv} "
          + "font_family_env=#{fontFamilyEnv} "
          + "font_size_env=#{fontSizeEnv} "
          + "font_fill_env=#{fontFillEnv} "
          + "font_stroke_env=#{fontStrokeEnv} "
          + "font_style_env=#{fontStyleEnv} "
          + "font_weight_env=#{fontWeightEnv} "
          + "update_time=#{updateTime,jdbcType=TIMESTAMP}"
          + "where "
          + "ruleName_id=#{ruleNameId}"
  })
  /**
   * description: 更新图层信息
   *
   * @param ruleName
   * @return int
   */
  public int updateRuleName(RuleName ruleName);


  @Delete({"delete from "
      + "tb_ruleName "
      + "where "
      + "ruleName_id=#{ruleNameId}"
  })
  /**
   * description: 删除ruleName
   *
   * @param ruleNameId
   * @return int
   */
  public int deleteRuleName(String ruleNameId);

  @Select({"select "
      + "* "
      + "from "
      + "tb_ruleName "
      + "where "
      + "style_id =#{styleId}"
  })
  /**
   + "style_id=#{styleId},"
   + "fill_env=#{fillEnv},"
   + "stroke_env=#{strokeEnv},"
   + "width_env=#{widthEnv},"
   + "opacity_env=#{opacityEnv} "
   + "font_family_env=#{fontFamilyEnv} "
   + "font_size_env=#{fontSizeEnv} "
   + "font_fill_env=#{fontFillEnv} "
   + "font_stroke_env=#{fontStrokeEnv} "
   + "font_style_env=#{fontStyleEnv} "
   + "font_weight_env=#{fontWeightEnv} "
   + "update_time "
   **/
  @Results({
      @Result(id = true, column = "ruleName_id", property = "ruleNameId"),
      @Result(column = "fill_env", property = "fillEnv"),
      @Result(column = "stroke_env", property = "strokeEnv"),
      @Result(column = "width_env", property = "widthEnv"),
      @Result(column = "opacity_env", property = "opacityEnv"),
      @Result(column = "font_family_env", property = "fontFamilyEnv"),
      @Result(column = "font_size_env", property = "fontSizeEnv"),
      @Result(column = "font_fill_env", property = "fontFillEnv"),
      @Result(column = "font_stroke_env", property = "fontStrokeEnv"),
      @Result(column = "font_style_env", property = "fontStyleEnv"),
      @Result(column = "font_weight_env", property = "fontWeightEnv"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "update_time", property = "updateTime"),
  })
  /**
   * description: 根据styleId查询ruleName
   *
   * @param styleid
   * @return java.util.List<cc.wanshan.gis.entity.style.RuleName>
   */
  public List<RuleName> findRuleNamesByStyleId(String styleid);
  @Select({"select "
      + "* "
      + "from "
      + "tb_ruleName "
      + "where "
      + "ruleName_id =#{ruleNameId}"
  })
  @Results({
      @Result(id = true, column = "ruleName_id", property = "ruleNameId"),
      @Result(column = "fill_env", property = "fillEnv"),
      @Result(column = "stroke_env", property = "strokeEnv"),
      @Result(column = "width_env", property = "widthEnv"),
      @Result(column = "opacity_env", property = "opacityEnv"),
      @Result(column = "font_family_env", property = "fontFamilyEnv"),
      @Result(column = "font_size_env", property = "fontSizeEnv"),
      @Result(column = "font_fill_env", property = "fontFillEnv"),
      @Result(column = "font_stroke_env", property = "fontStrokeEnv"),
      @Result(column = "font_style_env", property = "fontStyleEnv"),
      @Result(column = "font_weight_env", property = "fontWeightEnv"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "update_time", property = "updateTime"),
  })
  /**
   * description: 根据ruleNameId查询ruleName
   *
   * @param ruleNameId
   * @return
   */
  public RuleNameDao findRuleNameByRuleNameId(String ruleNameId);

  /*@Select({
      "select "
          + "count(*) "
          + "from "
          + "tb_ruleName as l "
          + "where "
          + "l.ruleName_name = #{ruleNameName} and "
          + "l.store_id = ("
          + "select "
          + "s.store_id "
          + "from "
          + "tb_user as u "
          + "inner join "
          + "tb_store as s "
          + "on "
          + "u.u_id = s.u_id "
          + "where u.username=#{username})"
  })
  *//**
   * description:
   *
   * @param username
   * @param ruleNameName
   * @return int
   *//*
  public int findRuleNameCountByUsernameAndRuleNameName(String username, String ruleNameName);*/

}
