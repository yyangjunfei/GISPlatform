package cc.wanshan.gis.dao.authorize;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Li Cheng
 * @date 2019/5/18 17:24
 */

/**
 * author_role_id character varying(32) NOT NULL DEFAULT replace(uuid_generate_v1()::text,'-',''),
 * author_id character varying(32) COLLATE pg_catalog."default" NOT NULL,
 * role_id character varying(32) COLLATE pg_catalog."default" NOT NULL,
 * CONSTRAINT fkaukq8hfdqf5vjcrwgps5tojpq FOREIGN KEY (role_id)
 * REFERENCES public.tb_role (role_id) MATCH SIMPLE
 * ON UPDATE NO ACTION
 * ON DELETE NO ACTION,
 * CONSTRAINT fkgfb2hd9j9ryp5jpxw5q2xiubj FOREIGN KEY (author_id)
 * REFERENCES public.tb_authority (author_id) MATCH SIMPLE
 * ON UPDATE NO ACTION
 * ON DELETE NO ACTION,
 * CONSTRAINT tb_authority_role_pkey PRIMARY KEY (author_role_id)
 **/
@Mapper
@Component
public interface AuthorityRoleDao {

}
