package cc.wanshan.gis.dao.layer.style;

import cc.wanshan.gis.entity.layer.style.RuleValue;
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
 * rule_value character varying(32) COLLATE pg_catalog."default" NOT NULL, rule_value_id character
 * varying(32) NOT NULL DEFAULT replace(uuid_generate_v1()::text,'-',''), rule_name_id character
 * varying(32) NOT NULL, fill_value character varying(8) COLLATE pg_catalog."default", stroke_value
 * character varying(8) COLLATE pg_catalog."default", width_value character varying(8) COLLATE
 * pg_catalog."default", opacity_value character varying(8) COLLATE pg_catalog."default",
 * font_family_value character varying(8) COLLATE pg_catalog."default", font_size_value character
 * varying(8) COLLATE pg_catalog."default", font_fill_value character varying(8) COLLATE
 * pg_catalog."default", font_stroke_value character varying(8) COLLATE pg_catalog."default",
 * font_style_value character varying(8) COLLATE pg_catalog."default", font_weight_value character
 * varying(8) COLLATE pg_catalog."default", insert_time timestamp(0) with time zone NOT NULL,
 * update_time timestamp(0) with time zone NOT NULL, CONSTRAINT fkcgvb4rn18kaq4pxb0piueexx7 FOREIGN
 * KEY (rule_name_id) REFERENCES public.rule_name (rule_name_id) MATCH SIMPLE ON UPDATE NO ACTION ON
 * DELETE NO ACTION, CONSTRAINT rule_value_rule_name_id_fkey FOREIGN KEY (rule_name_id) REFERENCES
 * public.rule_name (rule_name_id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE, CONSTRAINT
 * rule_value_pkey PRIMARY KEY (rule_value_id)
 **/
@Mapper
@Component
public interface RuleValueDao {


    /**
     * description: 根据layerId和ruleValueId删除ruleValue
     *
     * @return int
     */
    @Delete({
            "delete from "
                    + "rule_value "
                    + "where "
                    + "rule_value_id=#{ruleValueId} and "
                    + "rule_name_id=#{ruleName.ruleNameId}"
    })
    int deleteRuleValueByRuleValueIdAndRuleNameId(String ruleValueId, String ruleNameId);


    /**
     * description: 新增ruleValue
     *
     * @return int
     */
    @Insert({
            "insert into "
                    + "rule_value ("
                    + "rule_value,"
                    + "rule_name_id,"
                    + "fill_value,"
                    + "stroke_value,"
                    + "width_value,"
                    + "opacity_value,"
                    + "font_family_value,"
                    + "font_size_value,"
                    + "font_fill_value,"
                    + "font_stroke_value,"
                    + "font_style_value,"
                    + "font_weight_value,"
                    + "insert_time,"
                    + "update_time "
                    + ") values ("
                    + "#{ruleValue},"
                    + "#{ruleName.ruleNameId},"
                    + "#{fillValue},"
                    + "#{strokeValue},"
                    + "#{widthValue},"
                    + "#{opacityValue},"
                    + "#{fontFamilyValue},"
                    + "#{fontSizeValue},"
                    + "#{fontFillValue},"
                    + "#{fontStrokeValue},"
                    + "#{fontStyleValue},"
                    + "#{fontWeightValue},"
                    + "#{insertTime,jdbcType=TIMESTAMP},"
                    + "#{updateTime,jdbcType=TIMESTAMP}"
                    + ")"
    })
    @Options(useGeneratedKeys = true, keyColumn = "rule_value_id", keyProperty = "ruleValueId")
    int insertRuleValue(RuleValue ruleValue);


    /**
     * description: 更新ruleValue信息
     *
     * @return int
     */
    @Update({
            "update "
                    + "rule_value "
                    + "set "
                    + "rule_value_id=#{ruleValueId},"
                    + "rule_name_id=#{ruleName.ruleNameId},"
                    + "rule_value=#{ruleValue},"
                    + "fill_value=#{fillValue},"
                    + "stroke_value=#{strokeValue},"
                    + "width_value=#{widthValue},"
                    + "opacity_value=#{opacityValue},"
                    + "font_family_value=#{fontFamilyValue},"
                    + "font_size_value=#{fontSizeValue},"
                    + "font_fill_value=#{fontFillValue},"
                    + "font_stroke_value=#{fontStrokeValue},"
                    + "font_style_value=#{fontStyleValue},"
                    + "font_weight_value=#{fontWeightValue},"
                    + "update_time =#{updateTime,jdbcType=TIMESTAMP} "
                    + "where "
                    + "rule_value_id=#{ruleValueId}"
    })
    int updateRuleValue(RuleValue ruleValue);


    /**
     * description: 删除ruleValue
     *
     * @return int
     */
    @Delete({"delete from "
            + "rule_value "
            + "where "
            + "rule_value_id=#{ruleValueId}"
    })
    int deleteRuleValue(String ruleValueId);


    /**
     * description:
     *
     * @return java.util.List<cc.wanshan.gis.entity.style.RuleValue>
     */
    @Select({"select "
            + "* "
            + "from "
            + "rule_value "
            + "where "
            + "rule_name_id =#{ruleName.ruleNameId}"
    })
    @Results({
            @Result(id = true, column = "rule_value_id", property = "ruleValueId"),
            @Result(column = "rule_value", property = "ruleValue"),
            @Result(column = "fill_value", property = "fillValue"),
            @Result(column = "fill_value", property = "fillValue"),
            @Result(column = "stroke_value", property = "strokeValue"),
            @Result(column = "width_value", property = "widthValue"),
            @Result(column = "opacity_value", property = "opacityValue"),
            @Result(column = "font_family_value", property = "fontFamilyValue"),
            @Result(column = "font_size_value", property = "fontSizeValue"),
            @Result(column = "font_fill_value", property = "fontFillValue"),
            @Result(column = "font_stroke_value", property = "fontStrokeValue"),
            @Result(column = "font_style_value", property = "fontStyleValue"),
            @Result(column = "font_weight_value", property = "fontWeightValue"),
    })
    RuleValue findRuleValuesByRuleNameId(String ruleNameId);

    /**
     * description: 根据ruleValueId查询ruleValue
     */
    @Select({"select "
            + "* "
            + "from "
            + "rule_value "
            + "where "
            + "rule_value_id =#{ruleValueId}"
    })
    @Results({
            @Result(id = true, column = "rule_value_id", property = "ruleValueId"),
            @Result(column = "fill_value", property = "fillValue"),
            @Result(column = "stroke_value", property = "strokeValue"),
            @Result(column = "width_value", property = "widthValue"),
            @Result(column = "opacity_value", property = "opacityValue"),
            @Result(column = "font_family_value", property = "fontFamilyValue"),
            @Result(column = "font_size_value", property = "fontSizeValue"),
            @Result(column = "font_fill_value", property = "fontFillValue"),
            @Result(column = "font_stroke_value", property = "fontStrokeValue"),
            @Result(column = "font_style_value", property = "fontStyleValue"),
            @Result(column = "font_weight_value", property = "fontWeightValue"),
    })
    RuleValueDao findRuleValueByRuleValueId(String ruleValueId);
}
