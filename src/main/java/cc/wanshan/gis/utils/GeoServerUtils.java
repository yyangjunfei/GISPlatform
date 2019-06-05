package cc.wanshan.gis.utils;

import cc.wanshan.gis.entity.GeoServer;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class GeoServerUtils {
    private static final Logger LOG = LoggerFactory.getLogger(GeoServerUtils.class);

    private static GeoServer geoServer;

    @Resource
    public void setGeoServer(GeoServer geoServer) {
        GeoServerUtils.geoServer = geoServer;
    }

    public static URL url;
    public static GeoServerRESTManager manager;
    public static GeoServerRESTReader reader;
    public static GeoServerRESTPublisher publisher;

//    @PostConstruct
//    public static GeoServerRESTManager getGeoserverManager() {
//        if (manager == null) {
//            synchronized (GeoServerUtils.class) {
//                if (manager == null) {
//                    try {
//                        url = new URL(geoserver.getUrl());
//                        manager = new GeoServerRESTManager(url, geoserver.getUsername(), geoserver.getPasswd());
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        return manager;
//    }

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

    /**
     * 获取WMTS  GeoServer 的连接
     *
     * @param style
     * @param layerGroupName
     * @param srs_epsg
     * @return
     */
    public static String getWmtsUrl(String style, String layerGroupName, String srs_epsg) {

        String wmtsUrl = geoServer.getUrl() + "gwc/service/wmts?layer=#layergroupName#&style=#style#&tilematrixset=#srs_epsg#&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image/png&TileMatrix=#srs_epsg#:{TileMatrix}&TileCol={TileCol}&TileRow={TileRow}&TRANSPARENT=true";
        return wmtsUrl.replaceAll("#layergroupName#", layerGroupName).replaceAll("#srs_epsg#", srs_epsg == null ? "EPSG:900913" : srs_epsg).replaceAll("#style#", style == null ? "" : style);
    }

    /**
     * 获取WMS  GeoServer 的连接
     *
     * @param workspaceName
     * @param layerGroupName
     * @param srs_epsg
     * @return
     */

    public static String getWmsUrl(String workspaceName, String layerGroupName, String srs_epsg) {

        String url = geoServer.getUrl() + "/wms?service=WMS&version=1.1.0&request=GetMap&layers=#layergroupName&srs=#srs_epsg&format=image/jpeg";
        String replace = url.replaceAll("#layergroupName", layerGroupName).replaceAll("#srs_epsg", srs_epsg);
        return replace;
    }

    /**
     * 获取WFS  GeoServer 的连接
     *
     * @param workSpace
     * @param layerName
     * @return
     */
    public static String getWfsUrl(String workSpace, String layerName) {
        String wfsUrl = geoServer.getUrl() + "/#workSpace#/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=#workSpace#:#layerName#&outputFormat=application%2Fjson";
        return wfsUrl.replaceAll("#workSpace#", workSpace).replaceAll("#layerName#", layerName);
    }
}
