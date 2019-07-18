package cc.wanshan.gis.common.security;

import cc.wanshan.gis.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理类
 *
 * @Auther renmaoyan
 * @Date 2019/7/18 9:49
 */
@Slf4j
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {

            ResponseUtil.out(response, ResponseUtil.resultMap(false, 403, "用户名或密码错误，请重试"));
        } else if (exception instanceof DisabledException) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 403, "账户被禁用，请联系管理员"));
        } else {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 403, "登录失败，未知错误"));
        }
    }

}
