package cc.wanshan.gis.config.web;

import cc.wanshan.gis.common.filter.JWTAuthorizationFilter;
import cc.wanshan.gis.common.security.AccessDecisionManagerImpl;
import cc.wanshan.gis.common.security.AuthenticationFailHandler;
import cc.wanshan.gis.common.security.AuthenticationSuccessHandler;
import cc.wanshan.gis.common.security.JWTAuthenticationEntryPoint;
import cc.wanshan.gis.common.security.MyAccessDeniedHandler;
import cc.wanshan.gis.config.properties.IgnoredUrlsProperties;
import cc.wanshan.gis.service.authorize.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
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
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

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

    @Autowired
    JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    IgnoredUrlsProperties ignoredUrlsProperties;

    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    AuthenticationFailHandler authenticationFailHandler;

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
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler).and()
                .csrf().disable()
//                .exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
                httpSecurity.authorizeRequests();
        for (String url : ignoredUrlsProperties.getUrls()) {
            registry.antMatchers(url).permitAll();
        }
        registry.and()
                .formLogin()
                .loginPage("/auth/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailHandler).and()
                .headers().frameOptions().disable().and()
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
                }).and()
                .authorizeRequests().anyRequest().authenticated().and()
        // 添加自定义权限过滤器
//                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
//                .addFilter(new JWTAuthorizationFilter(authenticationManager(), redisTemplate))
        ;

        if (enable) {
            registry.and().addFilter(new JWTAuthorizationFilter(authenticationManager(), redisTemplate));
        }


    }
}
