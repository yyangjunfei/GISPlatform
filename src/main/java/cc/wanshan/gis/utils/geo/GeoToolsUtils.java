package cc.wanshan.gis.utils.geo;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.IOException;
import java.util.List;

/**
 * GeoTools工具
 */
public class GeoToolsUtils {

    static GeometryJSON geometryJson = new GeometryJSON(20);

    private GeoToolsUtils() {
    }

    /**
     * GeoJson 转 Geometry
     *
     * @param geoJson
     * @return
     * @throws IOException
     */
    public static Geometry geoJson2Geometry(String geoJson) throws IOException {
        return geometryJson.read(geoJson);
    }

    /**
     * Geometry 转 GeoJson
     *
     * @param geometry
     * @return
     */
    public static String geometry2GeoJson(Geometry geometry) {
        return geometryJson.toString(geometry);
    }

    /**
     * 通过最小最大经纬度构建Geometry
     *
     * @param xMin 最小经度
     * @param yMin 最小纬度
     * @param xMax 最大经度
     * @param yMax 最大纬度
     * @return
     * @throws IOException
     */
    public static Geometry polygon2Geometry(double xMin, double yMin, double xMax, double yMax) throws IOException {

        JSONObject polygonObject = new JSONObject();
        List<List<Double>> coordinates = Lists.newArrayList();
        List<Double> point1 = Lists.newArrayList();
        List<Double> point2 = Lists.newArrayList();
        List<Double> point3 = Lists.newArrayList();
        List<Double> point4 = Lists.newArrayList();
        List<Double> point5 = Lists.newArrayList();
        point1.add(xMin);
        point1.add(yMin);
        point2.add(xMax);
        point2.add(yMin);
        point3.add(xMax);
        point3.add(yMax);
        point4.add(xMin);
        point4.add(yMax);
        point5.add(xMin);
        point5.add(yMin);
        coordinates.add(point1);
        coordinates.add(point2);
        coordinates.add(point3);
        coordinates.add(point4);
        coordinates.add(point5);
        polygonObject.put("type", "Polygon");
        polygonObject.put("coordinates", coordinates);
        return geoJson2Geometry(polygonObject.toString());
    }

    /**
     * 空间坐标系转化
     *
     * @param geometry  空间位置
     * @param sourceCRS 源空间坐标
     * @param targetCRS 目标空间坐标
     * @return
     * @throws FactoryException
     * @throws TransformException
     */
    public static Geometry transform(Geometry geometry, String sourceCRS, String targetCRS)
            throws FactoryException, TransformException {

        CoordinateReferenceSystem targetStr = CRS.decode(targetCRS);
        CoordinateReferenceSystem sourceStr = CRS.decode(sourceCRS);
        MathTransform transform = CRS.findMathTransform(sourceStr, targetStr);
        return JTS.transform(geometry, transform);
    }

    /**
     * 空间坐标系转化
     *
     * @param geoJson   geoJson字符串
     * @param sourceCRS 源空间坐标 EPSG：3857
     * @param targetCRS 目标空间坐标 EPSG：4326
     * @return
     * @throws FactoryException
     * @throws TransformException
     */
    public static String transform(String geoJson, String sourceCRS, String targetCRS)
            throws IOException, FactoryException, TransformException {

        Geometry geometry = transform(geoJson2Geometry(geoJson), sourceCRS, targetCRS);
        return geometry2GeoJson(geometry);
    }

    /**
     * 空间坐标系转化
     *
     * @param coordinates 坐标数组
     * @param sourceCRS   源空间坐标 EPSG：3857
     * @param targetCRS   目标空间坐标 EPSG：4326
     * @return
     * @throws FactoryException
     * @throws TransformException
     */
    public static Coordinate[] transform(Coordinate[] coordinates, String sourceCRS, String targetCRS)
            throws FactoryException, TransformException {

        CoordinateReferenceSystem targetStr = CRS.decode(targetCRS);
        CoordinateReferenceSystem sourceStr = CRS.decode(sourceCRS);
        MathTransform transform = CRS.findMathTransform(sourceStr, targetStr, true);
        for (int i = 0; i < coordinates.length; i++) {
            JTS.transform(coordinates[i], coordinates[i], transform);
        }
        return coordinates;
    }

    /**
     * 空间坐标系转化
     *
     * @param coordinate 坐标
     * @param sourceCRS  源空间坐标 EPSG：3857
     * @param targetCRS  目标空间坐标 EPSG：4326
     * @return
     * @throws FactoryException
     * @throws TransformException
     */
    public static Coordinate transform(Coordinate coordinate, String sourceCRS, String targetCRS)
            throws FactoryException, TransformException {

        CoordinateReferenceSystem targetStr = CRS.decode(targetCRS);
        CoordinateReferenceSystem sourceStr = CRS.decode(sourceCRS);
        MathTransform transform = CRS.findMathTransform(sourceStr, targetStr, true);
        JTS.transform(coordinate, coordinate, transform);
        return coordinate;
    }
}
