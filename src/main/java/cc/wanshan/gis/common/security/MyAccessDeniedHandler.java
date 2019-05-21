package cc.wanshan.gis.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger= LoggerFactory.getLogger(MyAccessDeniedHandler.class);
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletRequest.getRequestDispatcher("/403").forward(httpServletRequest,httpServletResponse);
        PrintWriter out = httpServletResponse.getWriter();
        logger.warn("handle::httpServletRequest = [{}], httpServletResponse = [{}], e = [{}]",httpServletRequest, httpServletResponse, e);
        logger.warn("{\"status\":\"error\",\"msg\":\"权限不足，请联系管理员!\"}");
        out.write("{\"status\":\"error\",\"msg\":\"权限不足，请联系管理员!\"}");
        out.flush();
        out.close();
    }
}
