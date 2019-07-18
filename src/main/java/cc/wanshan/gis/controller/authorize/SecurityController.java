package cc.wanshan.gis.controller.authorize;


import cc.wanshan.gis.common.constants.SecurityConstant;
import cc.wanshan.gis.common.vo.Result;
import cc.wanshan.gis.service.authorize.AuthorityService;
import cc.wanshan.gis.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/auth")
public class SecurityController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    private AuthorityService authorityService;

    @RequestMapping(value = "loginSuccess")
    public Result loginSuccess() {
        return ResultUtil.success();
    }

    @RequestMapping(value = "loginError")
    public Result loginError(AuthenticationException e) {
        logger.error("loginError::e = [{}]", e);
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            logger.warn("用户名或密码输入错误，登录失败!");
            return ResultUtil.error(1, "用户名或密码输入错误，登录失败!");
        } else if (e instanceof DisabledException) {
            logger.warn("账户被禁用，登录失败，请联系管理员!");
            return ResultUtil.error(1, "账户被禁用，登录失败，请联系管理员!");
        } else {
            return ResultUtil.error(1, "登录失败!");
        }
    }

    @GetMapping(value = "refresh")
    public Result refreshToken(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstant.TOKEN_HEADER);

        return authorityService.refresh(token);
    }

    @GetMapping(value = "logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstant.TOKEN_HEADER);

        return authorityService.logout(token);
    }

}
