package cc.wanshan.gis.service.authorize.impl;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.authorize.RoleDao;
import cc.wanshan.gis.dao.authorize.UserDao;
import cc.wanshan.gis.entity.authorize.User;
import cc.wanshan.gis.entity.authorize.UserDetailsImpl;
import cc.wanshan.gis.service.authorize.UserService;
import cc.wanshan.gis.utils.base.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserDetailsService, UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    private UserDao userDao;
    @Resource
    private RoleDao roleDao;

    @Override
    public User findUserByUsername(String username) {
        logger.info("findUserByUsername::username = [{}]", username);
        return userDao.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("loadUserByUsername::s = [{}]", username);
        User user = findUserByUsername(username);

        return new UserDetailsImpl(user, roleDao.findRoleByUserName(username));
    }

    @Override
    @Transactional
    public Result insertUser(User user) {
        logger.info("insertUser::security = [{}]", user);
        if (user != null) {
            if (userDao.findUserCountByUsername(user.getUsername()) == 0) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String encode = encoder.encode(user.getPassword());
                logger.info("password：" + encode);
                user.setPassword(encode);
                int i = userDao.insertUser(user);
                if (i > 0) {
                    return ResultUtil.success();
                } else {
                    return ResultUtil.error(1, "注册失败");
                }
            } else {
                return ResultUtil.error(1, "用户已存在");
            }
        } else {
            return ResultUtil.error(2, "传入参数为空");
        }
    }

    @Override
    @RequestMapping("/findAllUser")
    @ResponseBody
    public Result findAllUser() {
        List<User> user = userDao.findAllUserAndRole();
        if (user != null) {
            return ResultUtil.success(user);
        } else {
            return ResultUtil.error(1, "查询失败");
        }
    }

    @Override
    public Result findUserCountByUsername(String username) {
        logger.info("findUserCountByUsername::username = [{}]", username);
        int count = userDao.findUserCountByUsername(username);
        if (count != 0) {
            return ResultUtil.error(1, "当前用户已存在");
        } else {
            return ResultUtil.success();
        }
    }

    @Override
    public Result findUserByUserId(String userId) {
        logger.info("findUserByUserId::userId = [{}]", userId);
        User user = userDao.findUserByUserId(userId);
        if (user != null) {
            return ResultUtil.success(user);
        } else {
            return ResultUtil.error(1, "用户不存在");
        }
    }

    @Override
    public Result updateUser(User user) {

        logger.info("updateUser::authorize = [{}]", user);

        int i = userDao.updateUser(user);
        if (i != 0) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(1, "修改失败");
        }
    }

    @Override
    public Result updateUserPassword(User user) {

        logger.info("updateUserPassword::security = [{}]", user);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(user.getPassword());
        user.setPassword(encode);
        user.setUpdateTime(new Date());
        int i = userDao.updateUserPassword(user);
        if (i == 1) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(1, "修改失败");
        }
    }

    @Override
    public Result deleteUser(String userId) {

        logger.info("deleteUser::userId = [{}]", userId);

        int i = userDao.deleteUser(userId);
        if (i == 1) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(1, "删除失败");
        }
    }

    @Override
    public Result updateUserStatus(User user) {

        logger.info("updateUserStatus::security = [{}]", user);

        int i = userDao.updateUserStatus(user);
        if (i == 1) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(1, "修改失败");
        }
    }

}
