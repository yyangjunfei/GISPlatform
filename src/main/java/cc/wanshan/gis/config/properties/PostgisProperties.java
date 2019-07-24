package cc.wanshan.gis.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@ConfigurationProperties(prefix = "postgis")
public class PostgisProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    private String url;
    private String user;
    private String password;

}
