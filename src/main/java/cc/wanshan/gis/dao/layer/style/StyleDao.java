package cc.wanshan.gis.dao.layer.style;
/**
 * @author Li Cheng
 * @date 2019/5/20 8:37
 */

import cc.wanshan.gis.entity.layer.style.Style;
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
 * style_id character varying(32) NOT NULL DEFAULT replace(uuid_generate_v1()::text,'-',''),
 * style_name character varying(32) COLLATE pg_catalog."default" NOT NULL, layer_id character
 * varying(32) COLLATE pg_catalog."default" NOT NULL, insert_time timestamp(0) with time zone NOT
 * NULL, update_time timestamp(0) with time zone NOT NULL, CONSTRAINT fkcgvb4rn18kaq4pxb0piueexx7
 * FOREIGN KEY (layer_id) REFERENCES public.layer (layer_id) MATCH SIMPLE ON UPDATE NO ACTION ON
 * DELETE NO ACTION, CONSTRAINT style_layer_id_fkey FOREIGN KEY (layer_id) REFERENCES public.layer
 * (layer_id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE, CONSTRAINT style_pkey PRIMARY KEY
 * (style_id)
 **/
@Mapper
@Component
public interface StyleDao {


    /**
     * description: 根据styleId查找style
     *
     * @return cc.wanshan.gisdev.entity.style.style
     */
    @Select({
            "select "
                    + "style_id,"
                    + "style_name,"
                    + "layer_id,"
                    + "insert_time,"
                    + "update_time "
                    + "from "
                    + "style "
                    + "where "
                    + "style_id=#{styleId}"
    })
    @Results({
            @Result(id = true, column = "style_id", property = "styleId"),
            @Result(column = "style_name", property = "styleName"),
            @Result(column = "layer_id", property = "layer.layeeId"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "style_id", property = "ruleNameList",
                    many = @Many(select = "cc.wanshan.gis.dao.layer.style.RuleNameDao.findRuleNamesByStyleId", fetchType = FetchType.LAZY))
    })
    Style findByStyleId(String styleId);

    /**
     * description: 根据styleId查找style
     *
     * @return cc.wanshan.gisdev.entity.style.style
     */
    @Select({
            "select "
                    + "style_id,"
                    + "style_name,"
                    + "layer_id,"
                    + "insert_time,"
                    + "update_time "
                    + "from "
                    + "style "
                    + "where "
                    + "layer_id=#{layerId}"
    })
    @Results({
            @Result(id = true, column = "style_id", property = "styleId"),
            @Result(column = "style_name", property = "styleName"),
            @Result(column = "layer_id", property = "layer.layerId"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "style_id", property = "ruleNameList",
                    many = @Many(select = "cc.wanshan.gis.dao.layer.style.RuleNameDao.findRuleNamesByStyleId", fetchType = FetchType.LAZY))
    })
    List<Style> findByLayerId(String layerId);


    /**
     * description:新增style
     *
     * @return int
     */
    @Insert({
            "insert into "
                    + "style "
                    + "(style_name,"
                    + "insert_time,"
                    + "update_time ) "
                    + "values "
                    + "(#{styleName},"
                    + "#{insertTime,jdbcType=TIMESTAMP},"
                    + "#{updateTime,jdbcType=TIMESTAMP})"
    })
    @Options(useGeneratedKeys = true, keyColumn = "style_id", keyProperty = "styleId")
    int insertStyle(Style style);


    /**
     * description:更新style
     *
     * @return int
     */
    @Update({
            "update "
                    + "style "
                    + "set "
                    + "layer_id=#{layer.layerId},"
                    + "style_name=#{styleName},"
                    + "update_time={#updateTime,jdbcType=TIMESTAMP} "
                    + "where "
                    + "style_id=#{styleId}"
    })
    int updateStyle(Style style);


    /**
     * description:根据styleId删除style
     *
     * @return int
     */
    @Delete({
            "delete from "
                    + "style "
                    + "where "
                    + "style_id=#{styleId}"
    })
    int deleteStyle(String styleId);

    /**
     * description: 查询所有的style
     *
     * @return java.util.List<cc.wanshan.gisdev.entity.security.style>
     */
    @Select({
            "select "
                    + "style_id,"
                    + "style_name,"
                    + "layer_id,"
                    + "insert_time,"
                    + "update_time "
                    + "from "
                    + "style"})
    @Results({
            @Result(id = true, column = "style_id", property = "styleId"),
            @Result(column = "style_name", property = "styleName"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "style_id", property = "ruleNameList",
                    many = @Many(select = "cc.wanshan.gis.dao.layer.style.RuleNameDao.findRuleNamesByStyleId", fetchType = FetchType.LAZY))
    })
    List<Style> findAllStyle();


    /**
     * description: 根据styleName查询style
     *
     * @return cc.wanshan.gisdev.entity.security.style
     */
    @Select({
            "select "
                    + "style_id "
                    + "from "
                    + "style "
                    + "where "
                    + "style_name=#{styleName}"
    })
    @Results({
            @Result(id = true, column = "style_id", property = "styleId"),
            @Result(column = "style_id", property = "ruleNameList",
                    many = @Many(select = "cc.wanshan.gis.dao.layer.style.RuleNameDao.findRuleNamesByStyleId", fetchType = FetchType.LAZY))
    })
    List<Style> findStyleByStyleName(String styleName);


    /**
     * description:判断style是否已存在
     *
     * @return int
     */
    @Select({
            "select "
                    + "count(*) "
                    + "from "
                    + "style "
                    + "where "
                    + "style_name=#{styleName}"
    })
    int findCountByStyleName(String styleName);


  /*  *//**
     * description:根据styleNameZh查询style
     *
     * @return cc.wanshan.gisdev.entity.security.style
     *//*
    @Select({
            "select "
                    + "style_id,"
                    + "style_name,"
                    + "style_name_zh,"
                    + "describe,"
                    + "insert_time,"
                    + "update_time "
                    + "from "
                    + "style "
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
    Style findStyleByStyleNameZH(String styleNameZH);*/


    /**
     * description:判断styleNameZH是否存在
     *
     * @return int
     */
    @Select({
            "select "
                    + "count(*) "
                    + "from "
                    + "style "
                    + "where "
                    + "style_name_zh=#{styleNameZH}"
    })
    int findCountByStyleNameZH(String styleNameZH);


    /**
     * description: 根据authorId查询store
     *
     * @return java.util.List<cc.wanshan.gisdev.entity.security.style>
     */
    @Select({
            "select "
                    + "r.style_name "
                    + "from authority_style as ar "
                    + "inner join "
                    + "style as r "
                    + "on "
                    + "ar.style_id=r.style_id "
                    + "where "
                    + "ar.authostyle_id=#{authorId}"
    })
    @Results({
            @Result(column = "style_name", property = "styleName")
    })
    List<Style> findByAuthorId(String authorId);

}
