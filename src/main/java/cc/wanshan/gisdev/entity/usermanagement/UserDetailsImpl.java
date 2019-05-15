package cc.wanshan.gisdev.entity.usermanagement;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private String username;
    private String passwoed;
    private Role role;

    public UserDetailsImpl() {
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswoed() {
        return passwoed;
    }

    public void setPasswoed(String passwoed) {
        this.passwoed = passwoed;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return passwoed;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    /**
     * @Author Li Cheng
     * @Description //判断账号是否已经过期默认没有过期
     * @Date 10:48 2019/3/13
     * @Param []
     * @return boolean
     **/

    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    /**
     * @Author Li Cheng
     * @Description //判断账号是否被锁定，默认没有锁定
     * @Date 10:48 2019/3/13
     * @Param []
     * @return boolean
     **/
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    /**
     * @Author Li Cheng
     * @Description //判断信用凭证是否过期，默认没有过期
     * @Date 10:48 2019/3/13
     * @Param []
     * @return boolean
     **/

    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    /**
     * @Author Li Cheng
     * @Description //判断账号是否可用，默认可用
     * @Date 10:47 2019/3/13
     * @Param []
     * @return boolean
     **/
    public boolean isEnabled() {
        return true;
    }
}
