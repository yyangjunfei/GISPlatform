package cc.wanshan.gisdev.controller.user;


import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping
public class SecurityController {
    private static final Logger logger= LoggerFactory.getLogger(SecurityController.class);
    @RequestMapping(value = "loginSuccess")
    @ResponseBody
    public Result loginSuccess(){
        return ResultUtil.success();
    }
    @RequestMapping(value = "loginError")
    @ResponseBody
    public Result loginError(AuthenticationException e){
        logger.error("loginError::e = [{}]",e);
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            logger.warn("用户名或密码输入错误，登录失败!");
            return ResultUtil.error(1,"用户名或密码输入错误，登录失败!");
        } else if (e instanceof DisabledException) {
            logger.warn("账户被禁用，登录失败，请联系管理员!");
            return ResultUtil.error(1,"账户被禁用，登录失败，请联系管理员!");
        } else {
            logger.warn("登录失败!");
            return ResultUtil.error(1,"登录失败!");
        }
    }

}
