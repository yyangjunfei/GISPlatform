//package cc.wanshan.gisdev.filter;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.annotation.WebInitParam;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//@WebFilter(
//        filterName = "corsFilter",
//        urlPatterns = "/*",
//        initParams = {
//                @WebInitParam(name = "allowOrigin", value = "*"),
//                @WebInitParam(name = "allowMethods", value = "GET,POST,PUT,DELETE,OPTIONS"),
//                @WebInitParam(name = "allowCredentials", value = "true"),
//                @WebInitParam(name = "allowHeaders", value = "*")
//        })
//@Component
//public class CorsFilter implements Filter {
//    private String allowOrigin;
//    private String allowMethods;
//    private String allowCredentials;
//    private String allowHeaders;
//    private String exposeHeaders;
//
//    @Override
//    public void init(FilterConfig filterConfig) {
//        allowOrigin = filterConfig.getInitParameter("allowOrigin");
//        allowMethods = filterConfig.getInitParameter("allowMethods");
//        allowCredentials = filterConfig.getInitParameter("allowCredentials");
//        allowHeaders = filterConfig.getInitParameter("allowHeaders");
//        exposeHeaders = filterConfig.getInitParameter("exposeHeaders");
//    }
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
//            throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) resp;
//        if (!StringUtils.isEmpty(allowOrigin)) {
//
//            if (allowOrigin.equals("*")) {
//                response.setHeader("Access-Control-Allow-Origin", allowOrigin);
//            } else {
//                List<String> allowOriginList = Arrays.asList(allowOrigin.split(","));
//                if (allowOriginList != null && allowOriginList.size() > 0) {
//                    String currentOrigin = request.getHeader("Origin");
//                    if (allowOriginList.contains(currentOrigin)) {
//                        response.setHeader("Access-Control-Allow-Origin", currentOrigin);
//                    }
//                }
//            }
//        }
//        if (!StringUtils.isEmpty(allowMethods)) {
//            response.setHeader("Access-Control-Allow-Methods", allowMethods);
//        }
//        if (!StringUtils.isEmpty(allowCredentials)) {
//            response.setHeader("Access-Control-Allow-Credentials", allowCredentials);
//        }
//        if (!StringUtils.isEmpty(allowHeaders)) {
//            response.setHeader("Access-Control-Allow-Headers", allowHeaders);
//        }
//        if (!StringUtils.isEmpty(exposeHeaders)) {
//            response.setHeader("Access-Control-Expose-Headers", exposeHeaders);
//        }
//        filterChain.doFilter(req, resp);
//    }
//
//    @Override
//    public void destroy() {
//    }
//}
