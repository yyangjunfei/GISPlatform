package cc.wanshan.gis.utils.geo;

import cc.wanshan.gis.config.properties.GeoServerProperties;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class GeoServerUtils {

    private static GeoServerProperties geoServer;

    @Resource
    public void setGeoServer(GeoServerProperties geoServer) {
        GeoServerUtils.geoServer = geoServer;
    }

    public static URL url;
    public static GeoServerRESTManager manager;
    public static GeoServerRESTReader reader;
    public static GeoServerRESTPublisher publisher;

    /**
     * 初始化，发布时进行身份认证
     */
    @PostConstruct
    public void init() {
        try {
            url = new URL(geoServer.getUrl());
            manager = new GeoServerRESTManager(url, geoServer.getUsername(), geoServer.getPasswd());
            reader = manager.getReader();
            publisher = manager.getPublisher();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
