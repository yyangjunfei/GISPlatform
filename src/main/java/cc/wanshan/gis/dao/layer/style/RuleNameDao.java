package cc.wanshan.gis.dao.layer.style;

import cc.wanshan.gis.entity.layer.style.RuleName;
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

import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/5/18 17:15
 */

/**
 * rule_name_id character varying(32) NOT NULL DEFAULT replace(uuid_generate_v1()::text,'-',''),
 * style_id character varying(32) NOT NULL, fill_env character varying(16) COLLATE
 * pg_catalog."default", stroke_env character varying(16) COLLATE pg_catalog."default", width_env
 * character varying(16) COLLATE pg_catalog."default", opacity_env character varying(16) COLLATE
 * pg_catalog."default", font_family_env character varying(16) COLLATE pg_catalog."default",
 * font_size_env character varying(16) COLLATE pg_catalog."default", font_fill_env character
 * varying(16) COLLATE pg_catalog."default", font_stroke_env character varying(16) COLLATE
 * pg_catalog."default", font_style_env character varying(16) COLLATE pg_catalog."default",
 * font_weight_env character varying(16) COLLATE pg_catalog."default", insert_time timestamp(0) with
 * time zone NOT NULL, update_time timestamp(0) with time zone NOT NULL, CONSTRAINT
 * fkcgvb4rn18kaq4pxb0piueexx7 FOREIGN KEY (style_id) REFERENCES public.style (style_id) MATCH
 * SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION, CONSTRAINT rule_name_style_id_fkey FOREIGN KEY
 * (style_id) REFERENCES public.style (style_id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
 * CONSTRAINT rule_name_pkey PRIMARY KEY (rule_name_id)
 **/
@Mapper
@Component
public interface RuleNameDao {


    /**
     * description: 根据styleId和ruleNameId删除ruleName
     *
     * @return int
     */
    @Delete({
            "delete from "
                    + "rule_name "
                    + "where "
                    + "rule_name_id=#{ruleNameId} and "
                    + "style_id=#{styleId}"
    })
    int deleteRuleNameByRuleNameIdAndLayerId(String ruleNameId, String styleId);


    /**
     * description: 新增ruleName
     *
     * @return int
     */
    @Insert({
            "insert into "
                    + "rule_name ("
                    + "style_id,"
                    + "layer_name,"
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
                    + "#{layer.layerName},"
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
                    + "#{insertTime,jdbcType=TIMESTAMP},"
                    + "#{updateTime,jdbcType=TIMESTAMP}"
                    + ")"
    })
    @Options(useGeneratedKeys = true, keyColumn = "rule_name_id", keyProperty = "ruleNameId")
    int insertRuleName(RuleName ruleName);


    /**
     * description: 更新ruleName信息
     *
     * @return int
     */
    @Update({
            "update "
                    + "rule_name "
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
                    + "rule_name_id=#{ruleNameId}"
    })
    int updateRuleName(RuleName ruleName);


    /**
     * description: 删除ruleName
     *
     * @return int
     */
    @Delete({"delete from "
            + "rule_name "
            + "where "
            + "rule_name_id=#{ruleNameId}"
    })
    int deleteRuleName(String ruleNameId);


    /**
     * description: 根据styleId查询ruleName
     *
     * @return java.util.List<cc.wanshan.gis.entity.style.RuleName>
     */
    @Select({"select "
            + "* "
            + "from "
            + "rule_name "
            + "where "
            + "style_id =#{styleId}"
    })
    @Results({
            @Result(id = true, column = "rule_name_id", property = "ruleNameId"),
            @Result(column = "layer_name", property = "layer.layerName"),
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
            @Result(column = "rule_name_id", property = "ruleValue",
                    many = @Many(select = "cc.wanshan.gis.dao.layer.style.RuleValueDao.findRuleValuesByRuleNameId", fetchType = FetchType.LAZY))
    })
    List<RuleName> findRuleNamesByStyleId(String styleId);


    /**
     * description: 根据layerName查询ruleName集合
     *
     * @return java.util.List<cc.wanshan.gis.entity.style.RuleName>
     */
    @Select({"select "
            + "* "
            + "from "
            + "rule_name "
            + "where "
            + "layer_name =#{layer.layerName}"
    })
    @Results({
            @Result(id = true, column = "rule_name_id", property = "ruleNameId"),
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
            @Result(column = "rule_name_id", property = "ruleValue",
                    many = @Many(select = "cc.wanshan.gis.dao.layer.style.RuleValueDao.findRuleValuesByRuleNameId", fetchType = FetchType.LAZY))
    })
    List<RuleName> findRuleNamesByLayerName(String layerName);

    /**
     * description: 根据ruleNameId查询ruleName
     */
    @Select({"select "
            + "* "
            + "from "
            + "rule_name "
            + "where "
            + "rule_name_id =#{ruleNameId}"
    })
    @Results({
            @Result(id = true, column = "rule_name_id", property = "ruleNameId"),
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
    })
    RuleNameDao findRuleNameByRuleNameId(String ruleNameId);


}
