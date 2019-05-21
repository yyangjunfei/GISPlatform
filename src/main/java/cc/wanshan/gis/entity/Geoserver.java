package cc.wanshan.gis.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@ConfigurationProperties(prefix = "geoserver")
@Component
public class Geoserver implements Serializable {
    private static final long serialVersionUID = 1L;
    //GeoServer的连接配置
    private String url ;
    private String username ;
    private String passwd ;
    //postgis连接配置
    private String postgisHost ;
    private int postgisPort ;
    private String postgisUser ;
    private String postgisPassword ;
    private String postgisDatabase ;

    public Geoserver() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPostgisHost() {
        return postgisHost;
    }

    public void setPostgisHost(String postgisHost) {
        this.postgisHost = postgisHost;
    }

    public int getPostgisPort() {
        return postgisPort;
    }

    public void setPostgisPort(int postgisPort) {
        this.postgisPort = postgisPort;
    }

    public String getPostgisUser() {
        return postgisUser;
    }

    public void setPostgisUser(String postgisUser) {
        this.postgisUser = postgisUser;
    }

    public String getPostgisPassword() {
        return postgisPassword;
    }

    public void setPostgisPassword(String postgisPassword) {
        this.postgisPassword = postgisPassword;
    }

    public String getPostgisDatabase() {
        return postgisDatabase;
    }

    public void setPostgisDatabase(String postgisDatabase) {
        this.postgisDatabase = postgisDatabase;
    }
}
