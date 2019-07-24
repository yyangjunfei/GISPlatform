package cc.wanshan.gis.service.authorize;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.authorize.Role;

import java.util.List;

public interface RoleService {

    /**
     * @return cc.wanshan.demo.entity.Role
     * @Author Li Cheng
     * @Description 根据用户名查找当前用户角色
     * @Date 15:02 2019/4/12
     * @Param [username]
     **/
    Role findRoleByUsername(String username);

    Result findRoleByRoleId(String roleId);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 根据authorId查询对应角色
     * @Date 8:30 2019/4/19
     * @Param [authorId]
     **/
    List<Role> findRoleByAuthorId(String authorId);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 查找所有角色
     * @Date 15:02 2019/4/12
     * @Param []
     **/
    Result findAllRole();

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 插入新角色
     * @Date 15:03 2019/4/12
     * @Param [role]
     **/
    Result insertRole(Role role);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 根据roleName查找当前角色
     * @Date 10:11 2019/4/15
     * @Param [roleName]
     **/
    Result findRoleByRoleName(String roleName);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 根据roleName查找重名数
     * @Date 10:53 2019/4/15
     * @Param [roleName]
     **/
    Result findCountByRoleName(String roleName);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 根据roleNameZH查找重名数
     * @Date 11:25 2019/4/15
     * @Param [roleNameZH]
     **/
    Result findCountByRoleNameZH(String roleNameZH);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 修改role
     * @Date 14:05 2019/4/15
     * @Param [role]
     **/
    Result updateRole(Role role);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 根据id删除角色
     * @Date 14:56 2019/4/15
     * @Param [roleId]
     **/
    Result deleteRole(String roleId);
}
