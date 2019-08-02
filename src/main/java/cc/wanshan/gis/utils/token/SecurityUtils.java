package cc.wanshan.gis.utils.token;

import cc.wanshan.gis.dao.authorize.RoleDao;
import cc.wanshan.gis.dao.authorize.UserDao;
import cc.wanshan.gis.dao.layer.UserPropsDao;
import cc.wanshan.gis.entity.authorize.User;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author renmaoyan
 * @date 2019/8/1
 * @description TODO
 */
@Component
public class SecurityUtils {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserPropsDao userPropsDao;

    /**
     * 获取当前用户
     *
     * @return
     */
    public User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDao.findByUsername(userDetails.getUsername());
    }

    /**
     * 获取当前用户的权限
     *
     * @param username
     * @return
     */
    public List<GrantedAuthority> getCurrentUserRole(String username) {

        return Lists.newArrayList(new SimpleGrantedAuthority(roleDao.findRoleByUserName(username).getRoleName()));
    }

    /**
     * 通过token获取当前用户
     *
     * @param token
     * @return
     */
    public String getUserProps(String token) {

        return userPropsDao.findUsersPropsByPropValue(token).getUsername();
    }

}
