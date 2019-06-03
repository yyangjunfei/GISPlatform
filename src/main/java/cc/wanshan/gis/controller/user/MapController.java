package cc.wanshan.gis.controller.user;


import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.utils.ResultUtil;
import javax.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;

@Controller
@RequestMapping("/map")
public class MapController {
    private static final Logger logger= LoggerFactory.getLogger(MapController.class);
    @RequestMapping(value = "/user")
    @ResponseBody
    public Result user(HttpServletRequest request)
    {
        logger.info("user::request = [{}]",request);
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
          String value = cookie.getValue();
          logger.info("cookie"+value);
        }
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        logger.info("securityContextImpl"+securityContextImpl.toString());
        String username = securityContextImpl.getAuthentication().getName();
        HashMap<String, String> map = new HashMap<>();
        Authentication authentication = securityContextImpl.getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            String authority1 = authority.getAuthority();
            map.put("role",authority1);
        }
        map.put("username",username);
        return ResultUtil.success(map);
    }
}
