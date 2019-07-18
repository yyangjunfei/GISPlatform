package cc.wanshan.gis.service.authorize;

import cc.wanshan.gis.common.vo.Result;
import cc.wanshan.gis.entity.authorize.User;

public interface UserService {
    /**
     * @return cc.wanshan.demo.entity.User
     * @Author Li Cheng
     * @Description 根据用户名查找当前用户
     * @Date 9:30 2019/4/17
     * @Param [username]
     **/
    public User findUserByUsername(String username);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 插入新用户
     * @Date 9:30 2019/4/17
     * @Param [userPage]
     **/
    public Result insertUser(User user);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 查找当前所有用户
     * @Date 9:29 2019/4/17
     * @Param []
     **/
    public Result findAllUser();

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 根据用户名查找当前同名对象
     * @Date 9:29 2019/4/17
     * @Param [username]
     **/
    public Result findUserCountByUsername(String username);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 根据userId查找用户信息
     * @Date 14:08 2019/4/17
     * @Param [userId]
     **/
    public Result findUserByUserId(String userId);

    public Result updateUser(User user);

    public Result updateUserPassword(User user);

    public Result deleteUser(String userId);

    public Result updateUserStatus(User user);

}
