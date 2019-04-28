package cc.wanshan.gisdev.utils;

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

/** Geotools工具 */
public class GeotoolsUtils {

  static GeometryJSON geometryJson = new GeometryJSON(20);

  private GeotoolsUtils() {}

  /** GeoJson 转 Geometry */
  public static Geometry geoJson2Geometry(String geoJson) throws IOException {
    return geometryJson.read(geoJson);
  }

  /** Geometry 转 GeoJson */
  public static String geometry2GeoJson(Geometry geometry) {
    return geometryJson.toString(geometry);
  }

  /** 空间坐标系转化 */
  public static Geometry transform(Geometry geometry, String sourceCRS, String targetCRS)
      throws FactoryException, TransformException {

    CoordinateReferenceSystem targetStr = CRS.decode(targetCRS);
    CoordinateReferenceSystem sourceStr = CRS.decode(sourceCRS);
    MathTransform transform = CRS.findMathTransform(sourceStr, targetStr, true);
    return JTS.transform(geometry, transform);
  }

  public static String transform(String geoJson, String sourceCRS, String targetCRS)
      throws IOException, FactoryException, TransformException {

    Geometry geometry = transform(geoJson2Geometry(geoJson), sourceCRS, targetCRS);
    return geometry2GeoJson(geometry);
  }

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

  public static Coordinate transform(Coordinate coordinate, String sourceCRS, String targetCRS)
      throws FactoryException, TransformException {

    CoordinateReferenceSystem targetStr = CRS.decode(targetCRS);
    CoordinateReferenceSystem sourceStr = CRS.decode(sourceCRS);
    MathTransform transform = CRS.findMathTransform(sourceStr, targetStr, true);
    JTS.transform(coordinate, coordinate, transform);
    return coordinate;
  }
}
