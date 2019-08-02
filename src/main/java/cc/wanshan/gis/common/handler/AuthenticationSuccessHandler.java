package cc.wanshan.gis.common.handler;

import cc.wanshan.gis.common.constant.SecurityConstant;
import cc.wanshan.gis.config.properties.TokenProperties;
import cc.wanshan.gis.dao.layer.UserPropsDao;
import cc.wanshan.gis.entity.authorize.TokenUser;
import cc.wanshan.gis.entity.authorize.UserDetailsImpl;
import cc.wanshan.gis.entity.layer.geoserver.UserProps;
import cc.wanshan.gis.utils.base.ResponseUtil;
import cc.wanshan.gis.utils.token.JwtTokenUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
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
 * @author renmaoyan
 * @Date 2019/7/17 9:24
 */
@Slf4j
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${gis.token.enable}")
    private Boolean enable;

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private UserPropsDao userPropsDao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        log.info("AuthenticationSuccessHandler::onAuthenticationSuccess::UserDetailsImpl==【{}】", userDetails);

        String role = userDetails.getRole();
        List<String> list = Lists.newArrayList();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            list.add(authority.getAuthority());
        }

        //登录生成token
        String token;
        if (tokenProperties.getJwt()) {

            String jwt = JwtTokenUtils.createToken(userDetails.getUsername(), new Gson().toJson(list));
            token = SecurityConstant.TOKEN_PREFIX + jwt;

            //将token存入redis缓存中
            if (tokenProperties.getRedis()) {
                redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + userDetails.getUsername(), token, SecurityConstant.EXPIRATION, TimeUnit.DAYS);
            }

        } else {

            token = (String) redisTemplate.opsForValue().get(SecurityConstant.USER_TOKEN + userDetails.getUsername());

            TokenUser tokenUser = new TokenUser();
            tokenUser.setUsername(userDetails.getUsername());
//            tokenUser.setRole(role);

            if (token == null || token.isEmpty()) {
                UserProps userProps = userPropsDao.findUsersPropsByUsername(userDetails.getUsername());
                token = userProps.getPropValue();

                if (tokenProperties.getRedis()) {
                    redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + userDetails.getUsername(), token, SecurityConstant.EXPIRATION, TimeUnit.MINUTES);
                    redisTemplate.opsForValue().set(SecurityConstant.TOKEN_USER + token, new Gson().toJson(tokenUser), SecurityConstant.EXPIRATION, TimeUnit.MINUTES);
                }
            }
        }

        response.setHeader("token", token);

        ResponseUtil.out(response, ResponseUtil.toMap(true, HttpServletResponse.SC_OK, "登录成功", token));
    }

}
