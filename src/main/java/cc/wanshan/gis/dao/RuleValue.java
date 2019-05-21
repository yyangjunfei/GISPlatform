package cc.wanshan.gisdev.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Li Cheng
 * @date 2019/5/18 17:15
 */
/**
 rule_value character varying(32) COLLATE pg_catalog."default" NOT NULL,
 rule_value_id character varying(32) NOT NULL DEFAULT replace(uuid_generate_v1()::text,'-',''),
 rule_name_id character varying(32) NOT NULL,
 fill_value character varying(8) COLLATE pg_catalog."default",
 stroke_value character varying(8) COLLATE pg_catalog."default",
 width_value character varying(8) COLLATE pg_catalog."default",
 opacity_value character varying(8) COLLATE pg_catalog."default",
 font_family_value character varying(8) COLLATE pg_catalog."default",
 font_size_value character varying(8) COLLATE pg_catalog."default",
 font_fill_value character varying(8) COLLATE pg_catalog."default",
 font_stroke_value character varying(8) COLLATE pg_catalog."default",
 font_style_value character varying(8) COLLATE pg_catalog."default",
 font_weight_value character varying(8) COLLATE pg_catalog."default",
 insert_time timestamp(0) with time zone NOT NULL,
 update_time timestamp(0) with time zone NOT NULL,
 CONSTRAINT fkcgvb4rn18kaq4pxb0piueexx7 FOREIGN KEY (rule_name_id)
 REFERENCES public.tb_rule_name (rule_name_id) MATCH SIMPLE
 ON UPDATE NO ACTION
 ON DELETE NO ACTION,
 CONSTRAINT tb_rule_value_rule_name_id_fkey FOREIGN KEY (rule_name_id)
 REFERENCES public.tb_rule_name (rule_name_id) MATCH SIMPLE
 ON UPDATE CASCADE
 ON DELETE CASCADE,
 CONSTRAINT tb_rule_value_pkey PRIMARY KEY (rule_value_id)
 **/
@Mapper
@Component
public interface RuleValue {

}
