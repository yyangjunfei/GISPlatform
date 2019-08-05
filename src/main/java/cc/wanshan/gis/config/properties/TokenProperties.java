package cc.wanshan.gis.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author renmaoyan
 * @date 2019/8/1
 * @description TODO
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "gis.token")
public class TokenProperties {

    private Boolean jwt;

    private Boolean redis;
}
