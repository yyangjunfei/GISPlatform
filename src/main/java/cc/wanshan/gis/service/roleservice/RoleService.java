package cc.wanshan.gis.service.roleservice;


import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.usermanagement.Role;

import java.util.List;

public interface RoleService {
    /**
     * @Author Li Cheng
     * @Description 根据用户名查找当前用户角色
     * @Date 15:02 2019/4/12
     * @Param [username]
     * @return cc.wanshan.demo.entity.Role
     **/
    public Role findRoleByUsername(String username);

    public Result findRoleByRoleId(String roleId);
    /**
     * @Author Li Cheng
     * @Description 根据authorId查询对应角色
     * @Date 8:30 2019/4/19
     * @Param [authorId]
     * @return cc.wanshan.demo.entity.Result
     **/
    public List<Role> findRoleByAuthorId(String authorId);

    /**
     * @Author Li Cheng
     * @Description 查找所有角色
     * @Date 15:02 2019/4/12
     * @Param []
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result findAllRole();
    /**
     * @Author Li Cheng
     * @Description 插入新角色
     * @Date 15:03 2019/4/12
     * @Param [role]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result insertRole(Role role);
    /**
     * @Author Li Cheng
     * @Description 根据roleName查找当前角色
     * @Date 10:11 2019/4/15
     * @Param [roleName]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result findRoleByRoleName(String roleName);
    /**
     * @Author Li Cheng
     * @Description 根据roleName查找重名数
     * @Date 10:53 2019/4/15
     * @Param [roleName]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result findCountByRoleName(String roleName);
    /**
     * @Author Li Cheng
     * @Description 根据roleNameZH查找重名数
     * @Date 11:25 2019/4/15
     * @Param [roleNameZH]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result findCountByRoleNameZH(String roleNameZH);
    /**
     * @Author Li Cheng
     * @Description 修改role
     * @Date 14:05 2019/4/15
     * @Param [role]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result updateRole(Role role);
    /**
     * @Author Li Cheng
     * @Description 根据id删除角色
     * @Date 14:56 2019/4/15
     * @Param [roleId]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result deleteRole(String roleId);
}
