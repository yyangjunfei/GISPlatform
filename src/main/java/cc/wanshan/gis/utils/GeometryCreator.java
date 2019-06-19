package cc.wanshan.gis.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.util.List;

/**
 * Geometry 构建
 */
public class GeometryCreator {

  private static final GeometryCreator INSTANCE = new GeometryCreator();

  private GeometryCreator() {
  }

  public static GeometryCreator getInstance() {
    return INSTANCE;
  }

  private static GeometryFactory geometryFactory = new GeometryFactory();

  /**
   * 根据X，Y坐标构建一个几何对象： 点 【Point】
   */
  public static Point createPoint(double x, double y) {

    Coordinate coord = new Coordinate(x, y);
    Point point = geometryFactory.createPoint(coord);
    return point;
  }

  /**
   * 根据几何对象的WKT描述【String】创建几何对象： 点 【Point】
   */
  public static Point createPointByWKT(String PointWKT) throws ParseException {

    WKTReader reader = new WKTReader(geometryFactory);
    return (Point) reader.read(PointWKT);
  }

  /**
   * 根据几何对象的WKT描述【String】创建几何对象：多点 【MultiPoint】
   */
  public static MultiPoint createMulPointByWKT(String MPointWKT) throws ParseException {

    WKTReader reader = new WKTReader(geometryFactory);
    return (MultiPoint) reader.read(MPointWKT);
  }

  /**
   * 根据两点 创建几何对象：线 【LineString】
   */
  public static LineString createLine(Coordinate[] coords) {

    return geometryFactory.createLineString(coords);
  }

  public static Polygon createPolygon(Coordinate[] coords) {

    return geometryFactory.createPolygon(coords);
  }

  /**
   * 根据线的WKT描述创建几何对象：线 【LineString】
   */
  public static LineString createLineByWKT(String LineStringWKT) throws ParseException {

    WKTReader reader = new WKTReader(geometryFactory);
    return (LineString) reader.read(LineStringWKT);
  }

  /**
   * 根据点组合的线数组，创建几何对象：多线 【MultiLineString】
   */
  public static MultiLineString createMLine(List<Coordinate[]> list) {

    LineString[] lineStrings = new LineString[list.size()];
    int i = 0;
    for (Coordinate[] coordinates : list) {
      lineStrings[i] = geometryFactory.createLineString(coordinates);
    }
    return geometryFactory.createMultiLineString(lineStrings);
  }

  /**
   * 根据几何对象的WKT描述【String】创建几何对象 ： 多线【MultiLineString】
   */
  public static MultiLineString createMLineByWKT(String MLineStringWKT) throws ParseException {

    WKTReader reader = new WKTReader(geometryFactory);
    return (MultiLineString) reader.read(MLineStringWKT);
  }

  /**
   * 根据几何对象的WKT描述【String】创建几何对象：多边形 【Polygon】
   */
  public static Polygon createPolygonByWKT(String PolygonWKT) throws ParseException {

    WKTReader reader = new WKTReader(geometryFactory);
    return (Polygon) reader.read(PolygonWKT);
  }

  /**
   * 根据几何对象的WKT描述【String】创建几何对象： 多多边形 【MultiPolygon】
   */
  public static MultiPolygon createMulPolygonByWKT(String MPolygonWKT) throws ParseException {

    WKTReader reader = new WKTReader(geometryFactory);
    return (MultiPolygon) reader.read(MPolygonWKT);
  }

  /**
   * 根据多边形数组 进行多多边形的创建
   */
  public static MultiPolygon createMulPolygonByPolygon(Polygon[] polygons) throws ParseException {

    return geometryFactory.createMultiPolygon(polygons);
  }

  /**
   * 根据WKT创建环
   */
  public static LinearRing createLinearRingByWKT(String ringWKT) throws ParseException {

    WKTReader reader = new WKTReader(geometryFactory);
    return (LinearRing) reader.read(ringWKT);
  }

  /**
   * 根据圆点以及半径创建几何对象：特殊的多边形--圆 【Polygon】
   */
  public static Polygon createCircle(double x, double y, final double RADIUS) {

    final int SIDES = 32; // 圆上面的点个数
    Coordinate coords[] = new Coordinate[SIDES + 1];
    for (int i = 0; i < SIDES; i++) {
      double angle = ((double) i / (double) SIDES) * Math.PI * 2.0;
      double dx = Math.cos(angle) * RADIUS;
      double dy = Math.sin(angle) * RADIUS;
      coords[i] = new Coordinate((double) x + dx, (double) y + dy);
    }
    coords[SIDES] = coords[0];
    LinearRing ring = geometryFactory.createLinearRing(coords);
    return geometryFactory.createPolygon(ring, null);
  }
}
