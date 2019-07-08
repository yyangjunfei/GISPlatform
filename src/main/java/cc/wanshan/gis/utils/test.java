package cc.wanshan.gis.utils;

import java.io.IOException;

public class test {
    public static void main(String[] args) {

        String s ="{\"coordinates\":[106.0006,33.177],\"type\":\"Point\"}";
        try {
            System.out.println(GeoToolsUtils.geoJson2Geometry(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
