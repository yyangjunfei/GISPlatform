package cc.wanshan.gis.service.userservice.impl;


import cc.wanshan.gis.dao.UserDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.usermanagement.User;
import cc.wanshan.gis.entity.usermanagement.UserDetailsImpl;
import cc.wanshan.gis.service.roleservice.impl.RoleServiceImpl;
import cc.wanshan.gis.service.userservice.UserService;
import cc.wanshan.gis.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.userdetails.UserDetailsService;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserDetailsService, UserService {
    private static final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    private UserDao userDao;
    @Resource
    private RoleServiceImpl roleServiceImpl;

    @Override
    public User findUserByUsername(String username) {
        logger.info("findUserByUsername::username = [{}]",username);
        return userDao.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.info("loadUserByUsername::s = [{}]",s);
        User user = findUserByUsername(s);
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUsername(s);
        userDetails.setPasswoed(user.getPassword());
        userDetails.setRole(roleServiceImpl.findRoleByUsername(s));
        return userDetails;
    }

    @Override
    @Transactional
    public Result insertUser(User user){
        logger.info("insertUser::user = [{}]",user);
        if (user!=null){
            if (userDao.findUserCountByUsername(user.getUsername())==0){
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String encode = encoder.encode(user.getPassword());
                user.setPassword(encode);
                int i = userDao.insertUser(user);
                if (i>0){
                    return ResultUtil.success();
                }else {
                    return ResultUtil.error(1,"注册失败");
                }
            }else {
                return ResultUtil.error(1,"用户已存在");
            }
        }else {
            return ResultUtil.error(2,"传入参数为空");
        }
    }

    @Override
    @RequestMapping("/findAllUser")
    @ResponseBody
    public Result findAllUser() {
        List<User> user = userDao.findAllUserAndRole();
        if (user!=null){
            return ResultUtil.success(user);
        }else {
            return ResultUtil.error(1,"查询失败");
        }
    }
    @Override
    public Result findUserCountByUsername(String username) {
        logger.info("findUserCountByUsername::username = [{}]",username);
        int count = userDao.findUserCountByUsername(username);
        if (count!=0){
            return ResultUtil.error(1,"当前用户已存在");
        }else {
            return ResultUtil.success();
        }
    }

    @Override
    public Result findUserByUserId(Integer userId) {
        logger.info("findUserByUserId::userId = [{}]",userId);
        User user = userDao.findUserByUserId(userId);
        if (user!=null){
            return ResultUtil.success(user);
        }else {
            return ResultUtil.error(1,"用户不存在");
        }
    }

    @Override
    public Result updateUser(User user) {
        logger.info("updateUser::user = [{}]",user);
        int i = userDao.updateUser(user);
        if (i!=0){
            return ResultUtil.success();
        }else {
            return ResultUtil.error(1,"修改失败");
        }
    }

    @Override
    public Result updateUserPassword(User user) {
        logger.info("updateUserPassword::user = [{}]",user);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(user.getPassword());
        user.setPassword(encode);
        user.setUpdateTime(new Date());
        int i = userDao.updateUserPassword(user);
        if (i==1){
            return ResultUtil.success();
        }else {
            return ResultUtil.error(1,"修改失败");
        }
    }

    @Override
    public Result deleteUser(Integer userId) {
        logger.info("deleteUser::userId = [{}]",userId);
        int i = userDao.deleteUser(userId);
        if (i==1){
            return ResultUtil.success();
        }else {
            return ResultUtil.error(1,"删除失败");
        }
    }

    @Override
    public Result updateUserStatus (User user) {
        logger.info("updateUserStatus::user = [{}]",user);
        int i = userDao.updateUserStatus(user);
        if (i==1){
            return ResultUtil.success();
        }else {
            return ResultUtil.error(1,"修改失败");
        }
    }

}
