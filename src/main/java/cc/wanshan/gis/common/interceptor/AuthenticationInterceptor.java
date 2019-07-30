package cc.wanshan.gis.common.interceptor;

import cc.wanshan.gis.common.annotation.LoginRequired;
import cc.wanshan.gis.common.constant.SecurityConstant;
import cc.wanshan.gis.utils.JwtTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Auther renmaoyan
 * @Date 2019/7/17 15:34
 */
@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter implements HandlerInterceptor {

    /**
     * 预处理回调方法，实现处理器的预处理（如登录检查）
     * 第三个参数为响应的处理器，即controller
     * 返回true，表示继续流程，调用下一个拦截器或者处理器
     * 返回false，表示流程中断，通过response产生响应
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 有 @LoginRequired 注解，需要认证,判断接口是否需要登录
        if (method.isAnnotationPresent(LoginRequired.class)) {
            return true;
        }
        String token = request.getHeader(SecurityConstant.TOKEN_HEADER);
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(SecurityConstant.TOKEN_HEADER);
        }
        if (token == null) {
            throw new RuntimeException("无token，请重新登录");
        }

        String username = JwtTokenUtils.getUsername(token);
        request.setAttribute("currentUser", username);
        return super.preHandle(request, response, handler);
    }

    /**
     * 当前请求进行处理之后，也就是Controller方法调用之后执行，
     * 但是它会在DispatcherServlet 进行视图返回渲染之前被调用。
     * 此时我们可以通过modelAndView对模型数据进行处理或对视图进行处理。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 方法将在整个请求结束之后，也就是在DispatcherServlet渲染了对应的视图之后执行。
     * 这个方法的主要作用是用于进行资源清理工作的。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
