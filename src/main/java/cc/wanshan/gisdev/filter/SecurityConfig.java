//package cc.wanshan.gisdev.filter;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers("/**")
//                .antMatchers("/swagger-ui.html")
//                .antMatchers("/webjars/**")
//                .antMatchers("/v2/**")
//                .antMatchers("/swagger-resources/**");
//    }
//
//
//}
