package cc.wanshan.gis.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@ConfigurationProperties(prefix = "geoserver")
@Component
@Data
public class GeoServer implements Serializable {
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
