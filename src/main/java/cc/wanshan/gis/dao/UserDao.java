package cc.wanshan.gis.dao;

import cc.wanshan.gis.entity.usermanagement.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Li Cheng
 * @Description
 * @Date 13:32 2019/3/12
 * @Param
 * @return
 **/
@Mapper
@Component
public interface UserDao {
    /**
     * @return cc.wanshan.demo.entity.User
     * @Author Li Cheng
     * @Description 根据用户名查找用户信息
     * @Date 11:42 2019/4/9
     * @Param [username]
     **/
    @Select({"select u_id,username,password,r_id from tb_user as u where u.delete!=1 and u.status=1 and u.username=#{username}"})
    @Results({
            @Result(id = true,column = "u_id",property = "userId"),
            @Result(column = "username",property = "username"),
            @Result(column = "password",property = "password"),
            @Result(column = "r_id",property = "role.roleId")
    })
    public User findUserByUsername(String username);
    /**
     * @return int
     * @Author Li Cheng
     * @Description 注册用户
     * @Date 11:43 2019/4/9
     * @Param [username, password, rId]
     **/
    @Insert({"insert into tb_user (username,password,r_id,insert_time,update_time,status,delete) values (#{username},#{password},#{role.roleId},#{insertTime,jdbcType=TIMESTAMP},#{updateTime,jdbcType=TIMESTAMP},#{status},#{delete})"})
    @Options(useGeneratedKeys = true,keyColumn = "u_id",keyProperty = "userId")
    public int insertUser(User user);

    /**
     * @return int 用户重名数
     * @Author Li Cheng
     * @Description 根据用户名查找当前重名用户
     * @Date 11:44 2019/4/9
     * @Param [username] 用户名
     **/
    @Select({"select count(*) from tb_user where username=#{username}"})
    public int findUserCountByUsername(String username);

    /**
     * @return cc.wanshan.demo.entity.User
     * @Author Li Cheng
     * @Description 根据用户id查找当前用户信息
     * @Date 11:45 2019/4/9
     * @Param [userId]
     **/
    @Select({"select u.u_id,u.username,u.password,u.r_id from tb_user as u where u.delete!=1 and u.status=1 and u.u_id=#{userId}"})
    @Results({
            @Result(id = true, column = "u_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password"),
            @Result(column = "r_id", property = "role.roleId"),
            @Result(column = "insert_time", property = "insert_time"),
            @Result(column = "update_time", property = "update_time"),
            @Result(column = "u_id", property = "storeList", many = @Many(select = "cc.wanshan.demo.repository.StoreDao.findStoreByUserId", fetchType = FetchType.LAZY))
    })
    public User findUserByUserId(Integer userId);

    /**
     * @return java.util.List<cc.wanshan.demo.entity.User>
     * @Author Li Cheng
     * @Description 根据角色查找用户
     * @Date 16:45 2019/4/9
     * @Param [roleId]
     **/
    @Select({"select u.u_id,u.username,u.r_id from tb_user as u where u.delete!=1 and u.status=1 and u.r_id=#{roleId}"})
    @Results({
            @Result(id = true, column = "u_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "r_id", property = "role.roleId"),
    })
    public List<User> findUsersByRoleId(Integer roleId);
    /**
     * @Author Li Cheng
     * @Description 修改用户信息
     * @Date 8:36 2019/4/10
     * @Param [userPage] 用户实体
     * @return int 数据库修改条数
     **/
    @Update({"update tb_user set r_id=#{role.roleId},update_time=#{updateTime} where delete!=1 and u_id=#{userId}"})
    public int updateUser(User user);
    /**
     * @Author Li Cheng
     * @Description 修改用户密码
     * @Date 14:22 2019/4/17
     * @Param [userPage]
     * @return int
     **/
    @Update({"update tb_user set password=#{password},r_id=#{role.roleId},update_time=#{updateTime} where delete!=1 and u_id=#{userId}"})
    public int updateUserPassword(User user);
    @Update({"update tb_user set status=#{status},update_time=#{updateTime} where delete!=1 and u_id=#{userId}"})
    public int updateUserStatus(User user);
    /**
     * @Author Li Cheng
     * @Description 根据用户Id删除用户信息
     * @Date 8:37 2019/4/10
     * @Param [userId] 用户Id
     * @return int 数据库删除条数
     **/
    @Update({"update tb_user set delete=1 where u_id=#{userId}"})
    public int deleteUser(Integer userId);
    /**
     * @Author Li Cheng
     * @Description 查找所有的用户
     * @Date 17:32 2019/4/15
     * @Param []
     * @return java.util.List<cc.wanshan.demo.entity.User>
     **/
    @Select({"select u_id,username,r_id,insert_time,update_time from tb_user where u.delete!=1 and u.status=1 order by u.u_id asc"})
    @Results(value = {
            @Result(id = true, column = "u_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "r_id", property = "role",many = @Many(select = "cc.wanshan.demo.repository.RoleDao.findByRoleId",fetchType = FetchType.LAZY)),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    public List<User> findAllUser();
    @MapKey("u_id")
    @Select({"select u.u_id,u.username,u.insert_time,u.update_time,u.status,r.role_name_zh from tb_user u  left outer join tb_role r on u.r_id=r.r_id where u.delete!=1 order by u.r_id asc,u.u_id asc"})
    @Results({
            @Result(id = true,column = "u_id",property = "userId"),
            @Result(column = "username",property = "username"),
            @Result(column = "insert_time",property = "insertTime"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "status",property = "status"),
            @Result(column = "role_name_zh",property = "role.roleNameZH"),
    })
    public List<User>findAllUserAndRole();
}
