package cc.wanshan.gis.config;

import cc.wanshan.gis.common.security.AccessDecisionManagerImpl;
import cc.wanshan.gis.common.security.MyAccessDeniedHandler;
import cc.wanshan.gis.service.user.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import javax.annotation.Resource;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
  private ObjectMapper objectMapper = new ObjectMapper();
  @Resource
  private UserServiceImpl userServiceImpl;
  //根据一个url请求，获得访问它所需要的roles权限
  @Resource
  FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;

  //接收一个用户的信息和访问一个url所需要的权限，判断该用户是否可以访问
  @Resource
  AccessDecisionManagerImpl accessDecisionManager;

  //403页面
  @Resource
  MyAccessDeniedHandler myAccessDeniedHandler;

  /**
   * 定义认证用户信息获取来源，密码校验规则等
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userServiceImpl).passwordEncoder(new BCryptPasswordEncoder());
  }

  //在这里配置哪些页面不需要认证
  @Override
  public void configure(WebSecurity web) {
    logger.info("configure::web = [{}]", web);
    web.ignoring()
        .antMatchers("/doc.html")
        .antMatchers("/swagger_ui.html");
  }

  /**
   * 定义安全策略
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    logger.info("configure::http = [{}]", http);
    http.cors()
        .and()
        .csrf()
        .disable()
        .exceptionHandling()
        .accessDeniedHandler(myAccessDeniedHandler)
        .and()
        .authorizeRequests()//配置安全策略
        .antMatchers(HttpMethod.OPTIONS)
        .permitAll()
        .antMatchers(HttpMethod.POST, "/user/login", "/**").permitAll().anyRequest().authenticated()
        .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
          @Override
          public <O extends FilterSecurityInterceptor> O postProcess(O o) {
            o.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
            o.setAccessDecisionManager(accessDecisionManager);
            return o;
          }
        })
        .and()
        .formLogin()
        .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
          logger.info(
              "onAuthenticationSuccess::httpServletRequest = [{}], httpServletResponse = [{}], authentication = [{}]",
              httpServletRequest, httpServletResponse, authentication);
          httpServletResponse.setContentType("application/json;charset=utf-8");
          String id = httpServletRequest.getSession().getId();
          logger.info("sessionId" + id);
          PrintWriter out = httpServletResponse.getWriter();
          logger.info("登录成功");
          out.write("{\"code\":0,\"msg\":\"登录成功\"}");
          out.flush();
          out.close();
        })
        .failureHandler((httpServletRequest, httpServletResponse, e) -> {
          logger.info(
              "onAuthenticationFailure::httpServletRequest = [{}], httpServletResponse = [{}], e = [{}]",
              httpServletRequest, httpServletResponse, e);
          httpServletResponse.setContentType("application/json;charset=utf-8");
          PrintWriter out = httpServletResponse.getWriter();
          StringBuffer sb = new StringBuffer();
          sb.append("{\"code\":1,\"msg\":");
          if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            sb.append("\"用户名或密码输入错误，登录失败!");
            logger.warn("用户名或密码输入错误，登录失败");
          } else if (e instanceof DisabledException) {
            sb.append("账户被禁用，登录失败，请联系管理员!");
            logger.warn("账户被禁用，登录失败，请联系管理员");
          } else {
            sb.append("登录失败!");
            logger.warn("登录失败");
          }
          sb.append("\"}");
          out.write(sb.toString());
          out.flush();
          out.close();
        })
        .loginProcessingUrl("/user/login")
        .permitAll()
        //防止iframe
        .and().headers().frameOptions().disable()
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
          logger.info(
              "onAuthenticationSuccess::httpServletRequest = [{}], httpServletResponse = [{}], authentication = [{}]",
              httpServletRequest, httpServletResponse, authentication);
          httpServletResponse.setContentType("application/json;charset=utf-8");
          PrintWriter out = httpServletResponse.getWriter();
          logger.info("退出成功");
          out.write("{\"code\":0,\"msg\":\"退出成功\"}");
          out.flush();
          out.close();
        })
        .deleteCookies("JSESSIONID").invalidateHttpSession(true).permitAll()
        .and()
        .sessionManagement()
        .maximumSessions(10)
        .maxSessionsPreventsLogin(false)
        .expiredSessionStrategy(
            sessionInformationExpiredEvent -> {
              logger.info("onExpiredSessionDetected::sessionInformationExpiredEvent = [{}]",
                  sessionInformationExpiredEvent);
              // 这里也可以根据需要返回html页面或者json数据
              Map<String, Object> map = new HashMap<>(16);
              map.put("code", 0);
              map.put("msg",
                  "已经另一台机器登录，您被迫下线。" + sessionInformationExpiredEvent.getSessionInformation()
                      .getLastRequest());
              sessionInformationExpiredEvent.getResponse()
                  .setContentType("application/json;charset=UTF-8");
              sessionInformationExpiredEvent.getResponse().getWriter()
                  .write(objectMapper.writeValueAsString(map));
            });
  }
}
