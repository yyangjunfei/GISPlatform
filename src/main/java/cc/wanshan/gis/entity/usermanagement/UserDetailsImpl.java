package cc.wanshan.gis.entity.usermanagement;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Data
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "用户名不可为null")
    @Length(max = 24,message = "长度最多24位")
    private String username;
    @NotBlank(message = "密码不可为null")
    @Length(min = 6,message = "长度最少6位")
    private String passwoed;
    private Role role;
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
