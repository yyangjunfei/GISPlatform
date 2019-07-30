package cc.wanshan.gis.dao.authorize;

import cc.wanshan.gis.entity.authorize.User;
import java.util.Date;
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
                    "users (" +
                    "name," +
                    "password," +
                    "role_id," +
                    "security," +
                    "insert_time," +
                    "update_time," +
                    "phone_num," +
                    "email," +
                    "department," +
                    "status," +
                    "enabled," +
                    "delete) " +
                    "values " +
                    "(" +
                    "#{username}," +
                    "#{password}," +
                    "#{roleId}," +
                    "#{security}," +
                    "#{insertTime,jdbcType=TIMESTAMP}," +
                    "#{updateTime,jdbcType=TIMESTAMP}," +
                    "#{phoneNum}," +
                    "#{email}," +
                    "#{department}," +
                    "#{status}," +
                    "#{enabled}," +
                    "#{delete}" +
                    ")"
    })
    /**
     * description: 插入用户
     *
     * @param username 用户名（必传参数）
    	* @param password 密码（必传参数）
    	* @param roleId 角色Id（必传参数）
    	* @param security 安全级别（必传参数）
    	* @param insertTime 插入时间（必传参数）
    	* @param updateTime 更新时间（必传参数）
    	* @param phoneNum 手机号
    	* @param email 电子邮件
    	* @param department 部门
    	* @param status 是否激活（必传参数）
    	* @param delete 是否删除（必传参数）
    	* @param enabled geoserver激活状态（必传参数）
     * @return int
     **/
    int insert(String username,String password,String roleId,String security,Date insertTime,
        Date updateTime,String  phoneNum,String email,String department,Integer status,
        Integer delete,String enabled);

    @Select({
            "select " +
                    "u.user_id," +
                    "u.name," +
                    "u.password," +
                    "u.role_id," +
                    "u.security," +
                    "u.insert_time," +
                    "u.update_time," +
                    "u.phone_num," +
                    "u.email," +
                    "u.department " +
                    "from " +
                    "users as u " +
                    "where " +
                    "u.delete!=1 and " +
                    "u.status=1 and " +
                    "u.name=#{username}"
    })
    @Results({
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "name", property = "username"),
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
     * description: 根据用户名查找用户信息
     *
     * @param username 用户名（必传参数）
     * @return cc.wanshan.gis.entity.authorize.User
     **/
    User findByUsername(String username);



    @Select({
            "select " +
                    "count(*) " +
                    "from " +
                    "users " +
                    "where " +
                    "name=#{username}"
    })
    /**
     * description:判断用户名是否存在
     *
     * @param username 用户名
     * @return int
     **/
    int findCountByUsername(String username);



    @Select({
            "select " +
                    "u.user_id," +
                    "u.name," +
                    "u.password," +
                    "u.role_id," +
                    "u.security," +
                    "u.insert_time," +
                    "u.update_time," +
                    "u.phone_num," +
                    "u.email," +
                    "u.department " +
                    "from " +
                    "users as u " +
                    "where " +
                    "u.delete!=1 and " +
                    "u.status=1 and " +
                    "u.user_id=#{userId}"
    })
    @Results({
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "name", property = "username"),
            @Result(column = "password", property = "password"),
            @Result(column = "role_id", property = "role.roleId"),
            @Result(column = "security", property = "security"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "phone_num", property = "phoneNum"),
            @Result(column = "email", property = "email"),
            @Result(column = "department", property = "department"),
            @Result(column = "user_id", property = "storeList", many = @Many(select = "cc.wanshan.gis.dao.layer.StoreDao.findStoreByUserId", fetchType = FetchType.LAZY))
    })
    /**
     * description: 根据用户Id查询用户信息
     *
     * @param userId 用户Id（必传参数）
     * @return cc.wanshan.gis.entity.authorize.User
     **/
    User findByUserId(String userId);


    @Select({
            "select " +
                    "u.user_id," +
                    "u.name," +
                    "u.role_id," +
                    "u.security," +
                    "u.insert_time," +
                    "u.update_time," +
                    "u.phone_num," +
                    "u.email," +
                    "u.department " +
                    "from " +
                    "users as u " +
                    "where " +
                    "u.delete!=1 and " +
                    "u.status=1 and " +
                    "u.role_id=#{roleId}"
    })
    @Results({
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "name", property = "username"),
            @Result(column = "role_id", property = "role.roleId"),
            @Result(column = "security", property = "security"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "phone_num", property = "phoneNum"),
            @Result(column = "email", property = "email"),
            @Result(column = "department", property = "department")
    })
    /**
     * description: 根据角色Id查询用户信息
     *
     * @param roleId 角色Id
     * @return java.util.List<cc.wanshan.gis.entity.authorize.User>
     **/
    List<User> findByRoleId(String roleId);

    @Select({
            "select " +
                    "u.user_id," +
                    "u.name," +
                    "u.role_id," +
                    "u.security," +
                    "u.insert_time," +
                    "u.update_time," +
                    "u.phone_num," +
                    "u.email," +
                    "u.department " +
                    "from " +
                    "users as u " +
                    "where " +
                    "u.delete!=1 and " +
                    "u.status=1 and " +
                    "u.thematic_id=#{thematicId}"
    })
    @Results({
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "name", property = "username"),
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
     * description: 根据专题Id查询用户信息
     *
     * @param thematicId 专题Id
     * @return java.util.List<cc.wanshan.gis.entity.authorize.User>
     **/
    List<User> findByThematicId(String thematicId);



    @Update({
            "update " +
                    "users " +
                    "set " +
                    "role_id=#{roleId}," +
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
     * @return int
     * @param userId 用户id（必传参数）
     * @param roleId 角色Id（必传参数）
     * @param security 安全级别（必传参数）
     * @param updateTime 更新时间（必传参数）
     * @param phoneNum 手机号
     * @param email 电子邮件
     * @param department 部门
     */
    int update(String userId,String roleId,String security ,Date updateTime,Integer phoneNum,String email,String department);

    @Update({
            "update " +
                    "users " +
                    "set " +
                    "password=#{password}," +
                    "update_time=#{updateTime,jdbcType=TIMESTAMP} " +
                    "where " +
                    "delete = 0 and " +
                    "status = 1 and " +
                    "user_id=#{userId}"
    })
    /**
     * @description: 修改用户密码
     *
     * @return int
     * @param userId 用户id
     * @param password 用户密码
     * @param updateTime 更新时间
     */
    int updatePassword(String userId,String password,Date updateTime);

    @Update({
            "update " +
                    "users " +
                    "set " +
                    "status=#{status}," +
                    "update_time=#{updateTime,jdbcType=TIMESTAMP} " +
                    "where " +
                    "delete=0 and " +
                    "user_id=#{userId}"
    })
    /**
     * description: 修改用户状态
     *
     * @return int
     * @param userId 用户id
     * @param updateTime 更新时间
     * @param status 用户状态
     */
    int updateStatus(String userId,Integer status, Date updateTime);

    @Update({
            "update " +
                    "users " +
                    "set " +
                    "delete=1 " +
                    "where " +
                    "user_id=#{userId}"
    })
    /**
     * description: 根据用户Id逻辑删除用户
     * @return int
     * @param 用户Id
     */
    int delete(String userId);



    @Select({
            "select " +
                    "user_id," +
                    "name," +
                    "role_id," +
                    "u.security," +
                    "insert_time," +
                    "update_time, " +
                    "u.phone_num," +
                    "u.email," +
                    "u.department " +
                    "from " +
                    "users " +
                    "where " +
                    "u.delete!=1 and " +
                    "u.status=1 " +
                    "order by " +
                    "u.user_id asc"
    })
    @Results(value = {
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "name", property = "username"),
            @Result(column = "role_id", property = "role", many = @Many(select = "cc.wanshan.gis.dao.authorize.RoleDao.findByRoleId", fetchType = FetchType.LAZY)),
            @Result(column = "security", property = "security"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "phone_num", property = "phoneNum"),
            @Result(column = "email", property = "email"),
            @Result(column = "department", property = "department")
    })
    /**
     * description: 查找所有用户
     * @return java.util.List<cc.wanshan.gis.entity.authorize.User>
     **/
    List<User> findAll();



    @MapKey("user_id")
    @Select({
            "select " +
                    "u.user_id," +
                    "u.name," +
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
                    "users u  " +
                    "left outer join " +
                    "role r " +
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
            @Result(column = "name", property = "username"),
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
     * @return java.util.List<cc.wanshan.gisdev.entity.security.User>
     */
    List<User> findAllByRoleAsc();
}
