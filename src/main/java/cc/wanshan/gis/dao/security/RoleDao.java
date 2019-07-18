package cc.wanshan.gis.dao.security;


import cc.wanshan.gis.entity.security.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @Author Li Cheng
 * @Date 13:33 2019/3/12
 **/
@Mapper
@Component
public interface RoleDao {

  @Select({
      "select "
          + "role_id,"
          + "role_name,"
          + "role_name_zh,"
          + "insert_time,"
          + "update_time,"
          + "describe "
          + "from "
          + "role "
          + "where "
          + "role_id=#{roleId}"
  })
  @Results({
      @Result(id = true, column = "role_id", property = "roleId"),
      @Result(column = "role_name", property = "roleName"),
      @Result(column = "role_name_zh", property = "roleNameZH"),
      @Result(column = "describe", property = "describe"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
      @Result(column = "role_id", property = "userList", many = @Many(select = "cc.wanshan.demo.repository.UserDao.findUsersByRoleId", fetchType = FetchType.LAZY)),
      @Result(column = "role_id", property = "authorityList", many = @Many(select = "cc.wanshan.demo.repository.AuthorityDao.findAuthoritiesByRoleId", fetchType = FetchType.LAZY))
  })
  /**
   * description: 根据roleId查找role
   *
   * @param roleId
   * @return cc.wanshan.gisdev.entity.security.Role
   */
   Role findByRoleId(String roleId);

  @Insert({
      "insert into "
          + "role "
          + "(role_name,"
          + "role_name_zh,"
          + "insert_time,"
          + "update_time,"
          + "describe) "
          + "values "
          + "(#{roleName},"
          + "#{roleNameZH},"
          + "#{insertTime,jdbcType=TIMESTAMP},"
          + "#{updateTime,jdbcType=TIMESTAMP},"
          + "#{describe})"
  })
  @Options(useGeneratedKeys = true, keyColumn = "role_id", keyProperty = "roleId")
  /**
   * description:新增role
   *
   * @param role
   * @return int
   */
   int insertRole(Role role);

  @Update({
      "update "
          + "role "
          + "set "
          + "role_id=#{roleId},"
          + "role_name=#{roleName},"
          + "role_name_zh=#{roleNameZH},"
          + "describe=#{describe},"
          + "update_time=#{updateTime} "
          + "where "
          + "role_id=#{roleId}"
  })
  /**
   * description:更新role
   *
   * @param role
   * @return int
   */
   int updateRole(Role role);

  @Delete({
      "delete from "
          + "role "
          + "where "
          + "role_id=#{roleId}"
  })
  /**
   * description:根据roleId删除role
   *
   * @param roleId
   * @return int
   */
   int deleteRole(String roleId);

  @Select({
      "select "
          + "* "
          + "from "
          + "role "
          + "where "
          + "role_id =( "
          + "select  "
          + "u.role_id "
          + "from "
          + "users as u "
          + "where "
          + "u.name=#{username})"
  })
  @Results({
      @Result(id = true, column = "role_id", property = "roleId"),
      @Result(column = "role_name", property = "roleName"),
      @Result(column = "role_name_zh", property = "roleNameZH"),
      @Result(column = "describe", property = "describe"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
  })
  /**
   * description:根据用户名查找role
   *
   * @param username
   * @return cc.wanshan.gisdev.entity.security.Role
   */
   Role findRoleByUserName(String username);

  @Select({
      "select "
          + "role_id ,"
          + "role_name,"
          + "role_name_zh,"
          + "describe "
          + "insert_time,"
          + "update_time "
          + "from "
          + "role"})
  @Results({
      @Result(id = true, column = "role_id", property = "roleId"),
      @Result(column = "role_name", property = "roleName"),
      @Result(column = "role_name_zh", property = "roleNameZH"),
      @Result(column = "describe", property = "describe"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
      @Result(column = "role_id", property = "userList", many = @Many(select = "cc.wanshan.demo.repository.UserDao.findUsersByRoleId", fetchType = FetchType.LAZY))
  })
  /**
   * description: 查询所有的role
   *
   * @param
   * @return java.util.List<cc.wanshan.gisdev.entity.security.Role>
   */
   List<Role> findAllRole();

  @Select({
      "select"
          + "role_id,"
          + "role_name,"
          + "role_name_zh,"
          + "describe,"
          + "insert_time,"
          + "update_time "
          + "from "
          + "role "
          + "where "
          + "role_name=#{roleName}"
  })
  @Results({
      @Result(id = true, column = "role_id", property = "roleId"),
      @Result(column = "role_name", property = "roleName"),
      @Result(column = "role_name_zh", property = "roleNameZH"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
      @Result(column = "describe", property = "describe")
  })
  /**
   * description: 根据roleName查询role
   *
   * @param roleName
   * @return cc.wanshan.gisdev.entity.security.Role
   */
   Role findRoleByRoleName(String roleName);

  @Select({
      "select "
          + "count(*) "
          + "from "
          + "role "
          + "where "
          + "role_name=#{roleName}"
  })
  /**
   * description:判断role是否已存在
   *
   * @param roleName
   * @return int
   */
   int findCountByRoleName(String roleName);


  @Select({
      "select "
          + "role_id,"
          + "role_name,"
          + "role_name_zh,"
          + "describe,"
          + "insert_time,"
          + "update_time "
          + "from "
          + "role "
          + "where "
          + "role_name_zh=#{roleNameZH}"
  })
  @Results({
      @Result(id = true, column = "role_id", property = "roleId"),
      @Result(column = "role_name", property = "roleName"),
      @Result(column = "role_name_zh", property = "roleNameZH"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "insert_time", property = "updateTime"),
      @Result(column = "describe", property = "describe")
  })
  /**
   * description:根据roleNameZh查询role
   *
   * @param roleNameZH
   * @return cc.wanshan.gisdev.entity.security.Role
   */
   Role findRoleByRoleNameZH(String roleNameZH);

  @Select({
      "select "
          + "count(*) "
          + "from "
          + "role "
          + "where "
          + "role_name_zh=#{roleNameZH}"
  })
  /**
   * description:判断roleNameZH是否存在
   *
   * @param roleNameZH
   * @return int
   */
   int findCountByRoleNameZH(String roleNameZH);

  @Select({
      "select "
          + "r.role_name "
          + "from authority_role as ar "
          + "inner join "
          + "role as r "
          + "on "
          + "ar.role_id=r.role_id "
          + "where "
          + "ar.author_id=#{authorId}"
  })
  @Results({
      @Result(column = "role_name", property = "roleName")
  })
  /**
   * description: 根据authorId查询store
   *
   * @param authorId
   * @return java.util.List<cc.wanshan.gisdev.entity.security.Role>
   */
   List<Role> findByAuthorId(String authorId);
}
