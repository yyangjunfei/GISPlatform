package cc.wanshan.gis.controller.user;


import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;

@Controller
@RequestMapping("/map")
public class MapController {
    private static final Logger logger= LoggerFactory.getLogger(MapController.class);
    @GetMapping(value = "/user")
    @ResponseBody
    public Result user(HttpServletRequest request)
    {
        logger.info("user::request = [{}]",request);
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
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
