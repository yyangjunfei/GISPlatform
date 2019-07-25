package cc.wanshan.gis.config.web;

import cc.wanshan.gis.common.filter.JWTAuthorizationFilter;
import cc.wanshan.gis.common.handler.AuthenticationFailHandler;
import cc.wanshan.gis.common.handler.AuthenticationSuccessHandler;
import cc.wanshan.gis.common.handler.CustomLogoutSuccessHandler;
import cc.wanshan.gis.common.handler.MyAccessDeniedHandler;
import cc.wanshan.gis.common.security.AccessDecisionManagerImpl;
import cc.wanshan.gis.common.security.JWTAuthenticationEntryPoint;
import cc.wanshan.gis.config.properties.IgnoredUrlsProperties;
import cc.wanshan.gis.service.authorize.impl.UserServiceImpl;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.annotation.Resource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

  @Resource
  private UserServiceImpl userServiceImpl;

  //根据一个url请求，获得访问它所需要的roles权限
  @Resource(name = "filterInvocationSecurityMetadataSourceImpl")
  private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSourceImpl;

  //接收一个用户的信息和访问一个url所需要的权限，判断该用户是否可以访问
  @Resource(name = "accessDecisionManagerImpl")
  private AccessDecisionManagerImpl accessDecisionManagerImpl;

  //403页面
  @Resource(name = "myAccessDeniedHandler")
  private MyAccessDeniedHandler myAccessDeniedHandler;

  @Autowired
  JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Autowired
  IgnoredUrlsProperties ignoredUrlsProperties;

  @Autowired
  AuthenticationSuccessHandler authenticationSuccessHandler;

  @Autowired
  AuthenticationFailHandler authenticationFailHandler;

  @Autowired
  CustomLogoutSuccessHandler customLogoutSuccessHandler;

  @Autowired
  private RedisTemplate redisTemplate;

  @Value("${gis.token.enable}")
  private Boolean enable;

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

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

    web.ignoring().antMatchers(
        "/swagger_ui.html", "/doc.html"
    );
  }

  /**
   * 定义安全策略
   */
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {

    logger.info("configure::http = [{}]", httpSecurity);

    httpSecurity
        .cors().and()
        .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(myAccessDeniedHandler)
        .and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
          ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();
        for (String url : ignoredUrlsProperties.getUrls()) {
          registry.antMatchers(url).permitAll();
          }
        registry.antMatchers(HttpMethod.POST, "/user/login", "/**").permitAll()
        .antMatchers(HttpMethod.OPTIONS).permitAll()
        .anyRequest().authenticated()
        .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
          @Override
          public <O extends FilterSecurityInterceptor> O postProcess(O o) {
            o.setSecurityMetadataSource(filterInvocationSecurityMetadataSourceImpl);
            o.setAccessDecisionManager(accessDecisionManagerImpl);
            return o;
          }
        })
        .and()
        .formLogin()
        .loginPage("/user/login")
        .successHandler(authenticationSuccessHandler)
        .failureHandler(authenticationFailHandler).and()
        .headers().frameOptions().disable().and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessHandler(customLogoutSuccessHandler)
        .and()
        .authorizeRequests().anyRequest().authenticated()
        .and()
        // 添加自定义权限过滤器
        .addFilter(new JWTAuthorizationFilter(authenticationManager(), redisTemplate))
    ;
  }
}
