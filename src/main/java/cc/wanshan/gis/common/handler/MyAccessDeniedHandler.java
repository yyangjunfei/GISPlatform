package cc.wanshan.gis.common.handler;

import cc.wanshan.gis.utils.base.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ResponseUtil.out(httpServletResponse, ResponseUtil.toMap(false, HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage(), "权限不足，请联系管理员"));

    }
}
