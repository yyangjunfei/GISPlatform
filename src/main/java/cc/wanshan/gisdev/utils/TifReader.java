package cc.wanshan.gisdev.utils;

import com.google.common.collect.Maps;
import org.apache.ibatis.datasource.DataSourceException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TifReader {

    private static final double EARTH_RADIUS = 6378137.0; // 平均半径,单位：m

    private static double rad(double d) {
        return d * Math.PI / 180.0; // 角度转化成弧度
    }

    /**
     * 通过经纬度获取两点距离（单位：米）
     *
     * @param longitude1 经度1
     * @param latitude1  纬度1
     * @param longitude2 经度2
     * @param latitude2  纬度2
     * @return
     */

    public static double DistanceOfTwoPoints(
            double longitude1, double latitude1, double longitude2, double latitude2) {
        double radLat1 = rad(latitude1); // 纬度
        double radLat2 = rad(latitude2); // 纬度
        // 纬度之差
        double a = radLat1 - radLat2;
        // 经度之差
        double b = rad(latitude1) - rad(latitude2);
        // 计算两点距离
        double s =
                2
                        * Math.asin(
                        Math.sqrt(
                                Math.pow(Math.sin(a / 2), 2)
                                        + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        return s * EARTH_RADIUS;
    }

    /**
     * 读取GeoTiff信息
     *
     * @param sourceTiffPath tif文件路劲
     * @return
     */

    private static Map<String, Object> getGeoTiffInfo(String sourceTiffPath) {
        HashMap<String, Object> map = Maps.newHashMap();
        File sourceTiff = new File(sourceTiffPath);
        try {
            // 调用Geotools读取geoTiff文件信息
            GeoTiffReader geoTiffReader = new GeoTiffReader(sourceTiff);
            GridCoverage2D coverage = geoTiffReader.read(null);
            Envelope2D envelope2D = coverage.getEnvelope2D();
            double minX = envelope2D.getMinX();
            double minY = envelope2D.getMinY();
            double maxX = envelope2D.getMaxX();
            double maxY = envelope2D.getMaxY();

            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();
            String code = CRS.toSRS(crs);
            // 根据经纬度获取两点距离
            double widthDistance = DistanceOfTwoPoints(minX, minY, maxX, minY);
            double heightDistance = DistanceOfTwoPoints(minX, minY, minX, maxY);

            RenderedImage image = coverage.getRenderedImage();
            Envelope env = coverage.getEnvelope();

            map.put("minX", minX);
            map.put("minY", minY);
            map.put("maxX", maxX);
            map.put("maxY", maxY);
            map.put("code", code);
            map.put("widthDistance", widthDistance);
            map.put("heightDistance", heightDistance);
            // TODO 获取影像瓦片级别

        } catch (DataSourceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}

