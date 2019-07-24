package cc.wanshan.gis.common.handler;

import cc.wanshan.gis.common.constants.SecurityConstant;
import cc.wanshan.gis.entity.authorize.UserDetailsImpl;
import cc.wanshan.gis.utils.JwtTokenUtils;
import cc.wanshan.gis.utils.base.ResponseUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 登录成功处理类
 *
 * @Auther renmaoyan
 * @Date 2019/7/17 9:24
 */
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${gis.token.enable}")
    private Boolean enable;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> list = Lists.newArrayList();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            list.add(authority.getAuthority());
        }

        String token = JwtTokenUtils.createToken(userDetails.getUsername(), new Gson().toJson(list));

        //将token存入redis缓存中
        redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + userDetails.getUsername(), SecurityConstant.TOKEN_PREFIX + token, SecurityConstant.EXPIRATION, TimeUnit.SECONDS);
//        redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PREFIX + token, new Gson().toJson(list), SecurityConstant.EXPIRATION, TimeUnit.SECONDS);

        // 返回创建成功的token,Bearer token
        response.setHeader("token", SecurityConstant.TOKEN_PREFIX + token);

        ResponseUtil.out(response, ResponseUtil.resultMap(true, HttpServletResponse.SC_OK, "登录成功", SecurityConstant.TOKEN_PREFIX + token));

    }

}
