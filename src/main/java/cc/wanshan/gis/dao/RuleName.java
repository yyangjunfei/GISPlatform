package cc.wanshan.gisdev.dao;

import org.apache.ibatis.annotations.Mapper;
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
public interface RuleName {

}
