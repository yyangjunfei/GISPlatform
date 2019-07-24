package cc.wanshan.gis.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@ConfigurationProperties(prefix = "geoserver")
public class GeoServerProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    //GeoServer的连接配置
    private String url;
    private String username;
    private String passwd;

    //postgis连接配置
    private String postgisHost;
    private int postgisPort;
    private String postgisUser;
    private String postgisPassword;
    private String postgisDatabase;

}
