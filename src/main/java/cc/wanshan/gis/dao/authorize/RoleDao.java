package cc.wanshan.gis.dao.authorize;

import cc.wanshan.gis.entity.authorize.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.MapKey;
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
 * @Author Li Cheng
 * @Date 13:33 2019/3/12
 **/
@Mapper
@Component
public interface RoleDao {

    /**
     * description: 根据roleId查找role
     *
     * @return cc.wanshan.gisdev.entity.security.Role
     */
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
            @Result(column = "role_id", property = "userList", many = @Many(select = "cc.wanshan.gis.dao.authorize.UserDao.findByRoleId", fetchType = FetchType.LAZY)),
            @Result(column = "role_id", property = "authorityList", many = @Many(select = "cc.wanshan.gis.dao.authorize.AuthorityDao.findByRoleId", fetchType = FetchType.LAZY))
    })
    Role findByRoleId(String roleId);


    /**
     * description:新增role
     *
     * @return int
     */
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
    int insertRole(Role role);


    /**
     * description:更新role
     *
     * @return int
     */
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
    int updateRole(Role role);

    /**
     * description:根据roleId删除role
     *
     * @return int
     */
    @Delete({
            "delete from "
                    + "role "
                    + "where "
                    + "role_id=#{roleId}"
    })
    int deleteRole(String roleId);


    /**
     * description:根据用户名查找role
     *
     * @return cc.wanshan.gisdev.entity.security.Role
     */
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
    Role findRoleByUserName(String username);


    /**
     * description: 查询所有的role
     *
     * @return java.util.List<cc.wanshan.gisdev.entity.security.Role>
     */
    @MapKey("role_id")
    @Select({
            "select "
                    + "role_id,"
                    + "role_name,"
                    + "role_name_zh,"
                    + "describe,"
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
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "role_id", property = "userList", many = @Many(select = "cc.wanshan.gis.dao.authorize.UserDao.findByRoleId",fetchType = FetchType.LAZY))
    })
    List<Role> findAllRole();


    /**
     * description: 根据roleName查询role
     *
     * @return cc.wanshan.gisdev.entity.security.Role
     */
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
    Role findRoleByRoleName(String roleName);


    /**
     * description:判断role是否已存在
     *
     * @return int
     */
    @Select({
            "select "
                    + "count(*) "
                    + "from "
                    + "role "
                    + "where "
                    + "role_name=#{roleName}"
    })
    int findCountByRoleName(String roleName);


    /**
     * description:根据roleNameZh查询role
     *
     * @return cc.wanshan.gisdev.entity.security.Role
     */
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
    Role findRoleByRoleNameZH(String roleNameZH);


    /**
     * description:判断roleNameZH是否存在
     *
     * @return int
     */
    @Select({
            "select "
                    + "count(*) "
                    + "from "
                    + "role "
                    + "where "
                    + "role_name_zh=#{roleNameZH}"
    })
    int findCountByRoleNameZH(String roleNameZH);


    /**
     * description: 根据authorId查询Role
     *
     * @return java.util.List<cc.wanshan.gisdev.entity.security.Role>
     */
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
    List<Role> findByAuthorId(String authorId);
}
