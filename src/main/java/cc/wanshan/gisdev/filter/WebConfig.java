package cc.wanshan.gisdev.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源配置，拦截器增加
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static Logger LOG = LoggerFactory.getLogger(WebConfig.class);

    /**
     * 跨域支持
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "HEAD", "OPTIONS")
                .maxAge(3600);
    }

    /**
     * 添加静态资源--过滤swagger-api (开源的在线API文档)
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        LOG.info("=========addResourceHandlers==========");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**")
                .addResourceLocations("classpath:/templates/");

        //过滤swagger
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/swagger-resources/**")
//                .addResourceLocations("classpath:/META-INF/resources/swagger-resources/");
//
//        registry.addResourceHandler("/swagger/**")
//                .addResourceLocations("classpath:/META-INF/resources/swagger*");
//
//        registry.addResourceHandler("/v2/api-docs/**")
//                .addResourceLocations("classpath:/META-INF/resources/v2/api-docs/");
    }

}