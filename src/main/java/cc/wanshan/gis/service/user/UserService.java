package cc.wanshan.gis.service.user;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.usermanagement.User;

public interface UserService {
    /**
     * @Author Li Cheng
     * @Description 根据用户名查找当前用户
     * @Date 9:30 2019/4/17
     * @Param [username]
     * @return cc.wanshan.demo.entity.User
     **/
    public User findUserByUsername(String username);
    /**
     * @Author Li Cheng
     * @Description 插入新用户
     * @Date 9:30 2019/4/17
     * @Param [userPage]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result insertUser(User user);
    /**
     * @Author Li Cheng
     * @Description 查找当前所有用户
     * @Date 9:29 2019/4/17
     * @Param []
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result findAllUser();
    /**
     * @Author Li Cheng
     * @Description 根据用户名查找当前同名对象
     * @Date 9:29 2019/4/17
     * @Param [username]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result findUserCountByUsername(String username);
    /**
     * @Author Li Cheng
     * @Description 根据userId查找用户信息
     * @Date 14:08 2019/4/17
     * @Param [userId]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result findUserByUserId(String userId);
    public Result updateUser(User user);
    public Result updateUserPassword(User user);
    public Result deleteUser(String userId);
    public Result updateUserStatus(User user);

}
