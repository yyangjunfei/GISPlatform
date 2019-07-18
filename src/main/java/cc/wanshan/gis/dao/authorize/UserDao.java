package cc.wanshan.gis.dao.authorize;

import cc.wanshan.gis.entity.authorize.User;
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
 * @Date 13:32 2019/3/12
 **/
@Mapper
@Component
public interface UserDao {

    @Insert({
            "insert into " +
                    "tb_user (" +
                    "username," +
                    "password," +
                    "role_id," +
                    "security," +
                    "insert_time," +
                    "update_time," +
                    "phone_num," +
                    "email," +
                    "department," +
                    "status," +
                    "delete) " +
                    "values " +
                    "(" +
                    "#{username}," +
                    "#{password}," +
                    "#{role.roleId}," +
                    "#{security}," +
                    "#{insertTime,jdbcType=TIMESTAMP}," +
                    "#{updateTime,jdbcType=TIMESTAMP}," +
                    "#{phoneNum}," +
                    "#{email}," +
                    "#{department}," +
                    "#{status}," +
                    "#{delete}" +
                    ")"
    })
    @Options(useGeneratedKeys = true, keyColumn = "user_id", keyProperty = "userId")
    /**
     * description: 新增user
     *
     * @param authorize
     * @return int
     */

    public int insertUser(User user);

    @Select({
            "select " +
                    "u.user_id," +
                    "u.username," +
                    "u.password," +
                    "u.role_id," +
                    "u.security," +
                    "u.insert_time," +
                    "u.update_time," +
                    "u.phone_num," +
                    "u.email," +
                    "u.department " +
                    "from " +
                    "tb_user as u " +
                    "where " +
                    "u.delete!=1 and " +
                    "u.status=1 and " +
                    "u.username=#{username}"
    })
    @Results({
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password"),
            @Result(column = "role_id", property = "role.roleId"),
            @Result(column = "security", property = "security"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "phone_num", property = "phoneNum"),
            @Result(column = "email", property = "email"),
            @Result(column = "department", property = "department")
    })
    /**
     * description: 根据username查找user
     *
     * @param username
     * @return cc.wanshan.gisdev.entity.authorize.User
     */
    public User findUserByUsername(String username);

    @Select({
            "select " +
                    "count(*) " +
                    "from " +
                    "tb_user " +
                    "where " +
                    "username=#{username}"
    })
    /**
     * description: 判断用户名是否存在
     *
     * @param username
     * @return int
     */
    public int findUserCountByUsername(String username);


    @Select({
            "select " +
                    "u.user_id," +
                    "u.username," +
                    "u.password," +
                    "u.role_id," +
                    "u.security," +
                    "u.insert_time," +
                    "u.update_time," +
                    "u.phone_num," +
                    "u.email," +
                    "u.department " +
                    "from " +
                    "tb_user as u " +
                    "where " +
                    "u.delete!=1 and " +
                    "u.status=1 and " +
                    "u.user_id=#{userId}"
    })
    @Results({
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password"),
            @Result(column = "role_id", property = "role.roleId"),
            @Result(column = "security", property = "security"),
            @Result(column = "insert_time", property = "insert_time"),
            @Result(column = "update_time", property = "update_time"),
            @Result(column = "phone_num", property = "phoneNum"),
            @Result(column = "email", property = "email"),
            @Result(column = "department", property = "department"),
            @Result(column = "user_id", property = "storeList", many = @Many(select = "cc.wanshan.demo.repository.StoreDao.findStoreByUserId", fetchType = FetchType.LAZY))
    })
    /**
     * description: 根据userId查找user
     *
     * @param userId
     * @return cc.wanshan.gisdev.entity.authorize.User
     */
    public User findUserByUserId(String userId);


    @Select({
            "select " +
                    "u.user_id," +
                    "u.username," +
                    "u.role_id," +
                    "u.security," +
                    "u.insert_time," +
                    "u.update_time," +
                    "u.phone_num," +
                    "u.email," +
                    "u.department " +
                    "from " +
                    "tb_user as u " +
                    "where " +
                    "u.delete!=1 and " +
                    "u.status=1 and " +
                    "u.role_id=#{roleId}"
    })
    @Results({
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "role_id", property = "role.roleId"),
            @Result(column = "security", property = "security"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "phone_num", property = "phoneNum"),
            @Result(column = "email", property = "email"),
            @Result(column = "department", property = "department")
    })
    /**
     * description: 根据roleId查找用户
     *
     * @param roleId
     * @return java.util.List<cc.wanshan.gisdev.entity.authorize.User>
     */
    public List<User> findUsersByRoleId(String roleId);

    @Select({
            "select " +
                    "u.user_id," +
                    "u.username," +
                    "u.role_id," +
                    "u.security," +
                    "u.insert_time," +
                    "u.update_time," +
                    "u.phone_num," +
                    "u.email," +
                    "u.department " +
                    "from " +
                    "tb_user as u " +
                    "where " +
                    "u.delete!=1 and " +
                    "u.status=1 and " +
                    "u.thematic_id=#{thematicId}"
    })
    @Results({
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "role_id", property = "role.roleId"),
            @Result(column = "thematic_id", property = "thematic.thematicId"),
            @Result(column = "security", property = "security"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "phone_num", property = "phoneNum"),
            @Result(column = "email", property = "email"),
            @Result(column = "department", property = "department")
    })
    /**
     * description: 根据thematicId查找thematic
     *
     * @param thematicId
     * @return java.util.List<cc.wanshan.gisdev.entity.authorize.User>
     */
    /*public List<User> findUsersByThematicId(String thematicId);*/

    @Update({
            "update " +
                    "tb_user " +
                    "set " +
                    "role_id=#{role.roleId}," +
                    "security=#{security}," +
                    "update_time=#{updateTime,jdbcType=TIMESTAMP}," +
                    "phone_num=#{phoneNum}," +
                    "email=#{email}," +
                    "department=#{department} " +
                    "where " +
                    "delete = 0 and " +
                    "status = 1 and " +
                    "user_id=#{userId}"
    })
    /**
     * description: 修改除密码以外的用户信息
     *
     * @param authorize
     * @return int
     */
    public int updateUser(User user);

    @Update({
            "update " +
                    "tb_user " +
                    "set " +
                    "password=#{password}," +
                    "role_id=#{role.roleId}," +
                    "update_time=#{updateTime,jdbcType=TIMESTAMP} " +
                    "where " +
                    "delete = 0 and " +
                    "status = 1 and " +
                    "user_id=#{userId}"
    })
    /**
     * description: 修改用户密码
     *
     * @param authorize
     * @return int
     */
    public int updateUserPassword(User user);

    @Update({
            "update " +
                    "tb_user " +
                    "set " +
                    "user_id=#{userId}," +
                    "role_id=#{role.roleId}," +
                    "security=#{security}," +
                    "status=#{status}," +
                    "update_time=#{updateTime,jdbcType=TIMESTAMP} " +
                    "where " +
                    "delete=0 and " +
                    "status=0 and " +
                    "user_id=#{userId}"
    })
    /**
     * description: 修改用户状态
     *
     * @param authorize
     * @return int
     */
    public int updateUserStatus(User user);


    @Update({
            "update " +
                    "tb_user " +
                    "set " +
                    "delete=1 " +
                    "where " +
                    "user_id=#{userId}"
    })
    /**
     * description: 逻辑删除用户
     *
     * @param userId
     * @return int
     */
    public int deleteUser(String userId);


    @Select({
            "select " +
                    "user_id," +
                    "username," +
                    "role_id," +
                    "u.security," +
                    "insert_time," +
                    "update_time, " +
                    "u.phone_num," +
                    "u.email," +
                    "u.department " +
                    "from " +
                    "tb_user " +
                    "where " +
                    "u.delete!=1 and " +
                    "u.status=1 " +
                    "order by " +
                    "u.user_id asc"
    })
    @Results(value = {
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "role_id", property = "role", many = @Many(select = "cc.wanshan.demo.repository.RoleDao.findByRoleId", fetchType = FetchType.LAZY)),
            @Result(column = "security", property = "security"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "phone_num", property = "phoneNum"),
            @Result(column = "email", property = "email"),
            @Result(column = "department", property = "department")
    })
    /**
     * description: 查找所有的用户
     *
     * @param
     * @return java.util.List<cc.wanshan.gisdev.entity.authorize.User>
     */
    public List<User> findAllUser();

    @MapKey("user_id")
    @Select({
            "select " +
                    "u.user_id," +
                    "u.username," +
                    "u.role_id," +
                    "u.security," +
                    "u.insert_time," +
                    "u.update_time," +
                    "u.phone_num," +
                    "u.email," +
                    "u.department," +
                    "u.status," +
                    "r.role_name_zh " +
                    "from " +
                    "tb_user u  " +
                    "left outer join " +
                    "tb_role r " +
                    "on " +
                    "u.role_id=r.role_id " +
                    "where " +
                    "u.delete!=1 and " +
                    "status = 1 " +
                    "order by " +
                    "u.role_id asc," +
                    "u.user_id asc"
    })
    @Results({
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "security", property = "security"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "phone_num", property = "phoneNum"),
            @Result(column = "email", property = "email"),
            @Result(column = "department", property = "department"),
            @Result(column = "status", property = "status"),
            @Result(column = "role_name_zh", property = "role.roleNameZH"),
    })
    /**
     * description: 查找所有的用户并根据role和user进行分组排序
     *
     * @param
     * @return java.util.List<cc.wanshan.gisdev.entity.authorize.User>
     */
    public List<User> findAllUserAndRole();
}
