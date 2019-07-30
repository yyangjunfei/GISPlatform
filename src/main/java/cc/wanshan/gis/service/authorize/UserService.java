package cc.wanshan.gis.service.authorize;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.authorize.User;
import java.util.Date;
import org.springframework.security.core.Authentication;

public interface UserService {

    /**
     * @return cc.wanshan.demo.entity.User
     * @Author Li Cheng
     * @Description 根据用户名查找当前用户
     * @Date 9:30 2019/4/17
     * @Param [username]
     **/
    Result findByUsername(String username);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 插入新用户
     * @Date 9:30 2019/4/17
     * @Param [userPage]
     **/
    Result insert(String username,String password,String roleId,String security,String  phoneNum,String email,String department,Integer status,
        Integer delete,String enabledd);

    /**
     *
     *  查找当前所有用户
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Date 9:29 2019/4/17
     * @Param []
     **/
    Result findAll();

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 根据用户名查找当前同名对象
     * @Date 9:29 2019/4/17
     * @Param [username]
     **/
    Result findCountByUsername(String username);

    /**
     * 根据userId查找用户信息
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 根据userId查找用户信息
     * @Date 14:08 2019/4/17
     * @Param [userId]
     **/
    Result findByUserId(String userId);

    Result update(String userId,String roleId,String security ,Integer phoneNum,String email,String department);

    Result updatePassword(String username,String password, String newPassword);

    Result delete(String userId);

    Result updateStatus(String userId, Integer status);
    /**
     * description: 查找当前用户信息
     *
     * @param authentication 认证类
     * @return cc.wanshan.gis.common.pojo.Result
     **/
    Result findCurrent(Authentication authentication);

}
