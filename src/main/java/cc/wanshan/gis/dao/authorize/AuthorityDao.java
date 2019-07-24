package cc.wanshan.gis.dao.authorize;

import cc.wanshan.gis.entity.authorize.Authority;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @Author Li Cheng
 * @Date 13:34 2019/3/12
 **/
@Mapper
@Component
public interface AuthorityDao {


    /**
     * description: 根据角色Id查找权限
     *
     * @return java.util.List<cc.wanshan.gisdev.entity.security.Authority>
     */
    @Select({
            "select "
                    + "* "
                    + "from "
                    + "authority "
                    + "where "
                    + "author_id "
                    + "in ("
                    + "select "
                    + "author_id "
                    + "from "
                    + "authority_role "
                    + "where role_id=#{roleId})"
    })
    List<Authority> findAuthoritiesByRoleId(String roleId);

    /**
     * description:根据url查找权限
     *
     * @return cc.wanshan.gisdev.entity.security.Authority
     */
    @Select({
            "select "
                    + "* "
                    + "from "
                    + "authority "
                    + "where url=#{url}"
    })
    @Results({
            @Result(id = true, column = "author_id", property = "authorId"),
            @Result(column = "author_name", property = "authorName"),
            @Result(column = "url", property = "url"),
    })
    Authority findByUrl(String url);

    /**
     * description: 根据权限ID查找权限
     *
     * @return cc.wanshan.gisdev.entity.security.Authority
     */
    @Select({
            "select "
                    + "* "
                    + "from "
                    + "authority "
                    + "where "
                    + "author_id=#{authorId}"
    })
    @Results({
            @Result(id = true, column = "author_id", property = "authorId"),
            @Result(column = "author_name", property = "authorName"),
            @Result(column = "url", property = "url"),
    })
    Authority findByAuthorId(String authorId);

    /**
     * description:新增权限记录
     *
     * @return int
     */
    @Insert({"insert into "
            + "authority ("
            + "author_name,"
            + "url,"
            + "insert_time,"
            + "update_time ) "
            + "values ("
            + "#{authorName},"
            + "#{url},"
            + "#{insertTime,jdbcType=TIMESTAMP},"
            + "#{updateTime,jdbcType=TIMESTAMP})"
    })
    @Options(useGeneratedKeys = true, keyColumn = "author_id", keyProperty = "authorId")
    int insertAuthority(Authority authority);


    /**
     * description: 根据authorId修改authority记录
     *
     * @return int
     */
    @Update({
            "update "
                    + "authority "
                    + "set "
                    + "author_id=#{authorId},"
                    + "author_name=#{authorName},"
                    + "url=#{url} "
                    + "update_time=#{updateTime,jdbcType=TIMESTAMP}) "
                    + "where "
                    + "author_id=#{authorId}"
    })
    int updateAuthority(Authority authority);


    /**
     * description: 根据authorId删除对应authority
     *
     * @return int
     */
    @Delete({"delete from "
            + "authority "
            + "where "
            + "author_id=#{authorId}"
    })
    int deleteAuthority(String authorId);
}
