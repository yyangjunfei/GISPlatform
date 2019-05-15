package cc.wanshan.gisdev.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.io.Serializable;

@Component
@ConfigurationProperties(prefix = "postgis")
public class Postgis implements Serializable {
    private static final long serialVersionUID = 1L;
    private String url;
    private String user;
    private String password;

    public Postgis() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
