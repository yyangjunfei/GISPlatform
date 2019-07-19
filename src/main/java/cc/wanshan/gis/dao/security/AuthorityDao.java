package cc.wanshan.gis.dao.security;


import cc.wanshan.gis.entity.security.Authority;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Li Cheng
 * @Date 13:34 2019/3/12
 **/
@Mapper
@Component
public interface AuthorityDao {

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
  /**
   * description: 根据角色Id查找权限
   *
   * @param roleId
   * @return java.util.List<cc.wanshan.gisdev.entity.security.Authority>
   */
   List<Authority> findAuthoritiesByRoleId(String roleId);


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
  /**
   * description:根据url查找权限
   *
   * @param url
   * @return cc.wanshan.gisdev.entity.security.Authority
   */
   Authority findByUrl(String url);


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
  /**
   * description: 根据权限ID查找权限
   *
   * @param authorId
   * @return cc.wanshan.gisdev.entity.security.Authority
   */
  Authority findByAuthorId(String authorId);


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
  /**
   * description:新增权限记录
   *
   * @param authority
   * @return int
   */
  int insertAuthority(Authority authority);

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
  /**
   * description: 根据authorId修改authority记录
   *
   * @param authority
   * @return int
   */
   int updateAuthority(Authority authority);


  @Delete({"delete from "
      + "authority "
      + "where "
      + "author_id=#{authorId}"
  })
  /**
   * description: 根据authorId删除对应authority
   *
   * @param authorId
   * @return int
   */
   int deleteAuthority(String authorId);
}
