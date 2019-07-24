package cc.wanshan.gis.common.handler;

import cc.wanshan.gis.utils.base.ResponseUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录处理
 *
 * @Auther renmaoyan
 * @Date 2019/7/19
 */

@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //TODO 退出登录成功处理

        ResponseUtil.out(response, ResponseUtil.resultMap(true, HttpServletResponse.SC_OK, "退出登录成功"));
    }

}
