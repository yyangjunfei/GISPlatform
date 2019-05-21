package cc.wanshan.gis.dao;


import cc.wanshan.gis.entity.usermanagement.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @Author Li Cheng
 * @Description 
 * @Date 13:33 2019/3/12
 * @Param 
 * @return 
 **/
@Mapper
@Component
public interface RoleDao {
    /**
     * @Author Li Cheng
     * @Description 根据roleId查询当前role
     * @Date 9:08 2019/4/10
     * @Param [roleId]
     * @return cc.wanshan.demo.entity.Role
     **/
    @Select({"select r_id,role_name,role_name_zh,describe from tb_role where r_id=#{roleId}"})
    @Results({
            @Result(id = true,column = "r_id",property = "roleId"),
            @Result(column = "role_name",property = "roleName"),
            @Result(column = "role_name_zh",property = "roleNameZH"),
            @Result(column = "describe",property = "describe"),
            @Result(column = "r_id",property = "userList",many = @Many(select = "cc.wanshan.demo.repository.UserDao.findUsersByRoleId",fetchType = FetchType.LAZY)),
            @Result(column = "r_id",property = "authorityList",many = @Many(select = "cc.wanshan.demo.repository.AuthorityDao.findAuthoritiesByRoleId",fetchType = FetchType.LAZY))
    })
    public Role findByRoleId(Integer roleId);
    /**
     * @Author Li Cheng
     * @Description 新增角色记录
     * @Date 9:13 2019/4/10
     * @Param [role]
     * @return int
     **/
    @Insert({"insert into tb_role (role_name,role_name_zh,describe) values (#{roleName},#{roleNameZH},#{describe})"})
    @Options(useGeneratedKeys = true,keyColumn = "r_id",keyProperty = "roleId")
    public int insertRole(Role role);
    /**
     * @Author Li Cheng
     * @Description 根据角色Id修改角色信息
     * @Date 9:30 2019/4/10
     * @Param [role]
     * @return int
     **/
    @Update({"update tb_role set r_id=#{roleId},role_name=#{roleName},role_name_zh=#{roleNameZH} where r_id=#{roleId}"})
    public int updateRole(Role role);
    /**
     * @Author Li Cheng
     * @Description  根据角色id删除对应角色记录
     * @Date 9:33 2019/4/10
     * @Param [roleId]
     * @return int
     **/
    @Delete({"delete from tb_role where r_id=#{roleId}"})
    public int deleteRole(Integer roleId);
    /**
     * @Author Li Cheng
     * @Description 根据用户名查找角色
     * @Date 10:33 2019/4/15
     * @Param [username]
     * @return cc.wanshan.demo.entity.Role
     **/
    @Select({"select * from tb_role where r_id=(select  u.r_id from tb_user as u where u.username =#{username})"})
    @Results({
            @Result(id = true,column = "r_id",property = "roleId"),
            @Result(column = "role_name",property = "roleName"),
            @Result(column = "role_name_zh",property = "roleNameZH"),
    })
    public Role findRoleByUserName(String username);
    /**
     * @Author Li Cheng
     * @Description 查找所用角色
     * @Date 10:34 2019/4/15
     * @Param []
     * @return java.util.List<cc.wanshan.demo.entity.Role>
     **/
    @Select({"select r_id ,role_name,role_name_zh,describe from tb_role"})
    @Results({
            @Result(id = true,column = "r_id",property = "roleId"),
            @Result(column = "role_name",property = "roleName"),
            @Result(column = "role_name_zh",property = "roleNameZH"),
            @Result(column = "describe",property = "describe"),
            @Result(column = "r_id",property = "userList",many = @Many(select = "cc.wanshan.demo.repository.UserDao.findUsersByRoleId",fetchType = FetchType.LAZY))
    })
    public List<Role> findAllRole();
    /**
     * @Author Li Cheng
     * @Description 根据roleName查找角色
     * @Date 10:32 2019/4/15
     * @Param []
     * @return cc.wanshan.demo.entity.Role
     **/
    @Select({"select r_id,role_name,role_name_zh,describe from tb_role where role_name=#{roleName}"})
    @Results({
            @Result(id = true,column = "r_id",property = "roleId"),
            @Result(column = "role_name",property = "roleName"),
            @Result(column = "role_name_zh",property = "roleNameZH"),
            @Result(column = "describe",property = "describe")
    })
    public Role findRoleByRoleName(String roleName);
    /**
     * @Author Li Cheng
     * @Description  根据roleName查找角色重名数
     * @Date 11:12 2019/4/15
     * @Param [roleName]
     * @return int
     **/
    @Select({"select count(*) from tb_role where role_name=#{roleName}"})
    public int findCountByRoleName(String roleName);
    /**
     * @Author Li Cheng
     * @Description 根据roleNameZH查找对应角色
     * @Date 10:38 2019/4/15
     * @Param [roleNameZH]
     * @return cc.wanshan.demo.entity.Role
     **/
    @Select({"select r_id,role_name,role_name_zh,describe from tb_role where role_name_zh=#{roleNameZH}"})
    @Results({
            @Result(id = true,column = "r_id",property = "roleId"),
            @Result(column = "role_name",property = "roleName"),
            @Result(column = "role_name_zh",property = "roleNameZH"),
            @Result(column = "describe",property = "describe")
    })
    public Role findRoleByRoleNameZH(String roleNameZH);
    /**
     * @Author Li Cheng
     * @Description 根据roleNameZH查找当前重名数
     * @Date 11:13 2019/4/15
     * @Param [roleNameZH]
     * @return int
     **/
    @Select({"select count(*) from tb_role where role_name_zh=#{roleNameZH}"})
    public int findCountByRoleNameZH(String roleNameZH);
    @Select({"select r.role_name from tb_authority_role as ar inner join tb_role as r on ar.role_id=r.r_id where ar.author_id=#{authorId}"})
    @Results({
            @Result(column = "role_name",property = "roleName")
    })
    public List<Role> findByAuthorId(Integer authorId);
}
