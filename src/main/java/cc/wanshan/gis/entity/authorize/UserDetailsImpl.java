package cc.wanshan.gis.entity.authorize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Collections;

@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)    //注解控制null不序列化
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank(message = "用户名不可为null")
    @Length(max = 24, message = "长度最多24位")
    private String username;
    @NotBlank(message = "密码不可为null")
    @Length(min = 6, message = "长度最少6位")
    private String password;
    private Role role;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl() {
    }

    public UserDetailsImpl(User user, Role role) {
        this.id = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = role;
        authorities = Collections.singleton(new SimpleGrantedAuthority(role.getRoleName()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(role.getRoleName()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
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
