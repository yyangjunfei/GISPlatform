package cc.wanshan.gisdev.common.constants;

public class Constant {

  /** 类型 */
  public static final String TYPE = "type";

  public static final String TYPE_GROUP = "group";

  public static final String TYPE_MEMBER = "member";

  public static final String TYPE_VEHICLE = "vehicle";

  public static final String TYPE_MATERIAL = "material";

  public static final String TYPE_EXPERT = "expert";

  public static final String TYPE_INSTITUTION = "institution";

  /** 元数据类型 */
  /** 矢量数据 */
  public static final Integer SHP_TYPE = 1;
  /** 影像数据 */
  public static final Integer TIFF_TYPE = 2;
  /** 倾斜摄影数据 */
  public static final Integer OSGB_TYPE = 3;
  /** 数字高程模型数据 */
  public static final Integer DEM_TYPE = 4;
  /** 点云数据 */
  public static final Integer LAS_TYPE = 5;

  /** Geometry空间数据类型 */
  /** 点 */
  public static String GEO_POINT = "Point";
  /** 多点 */
  public static String GEO_MULTIPOINT = "MultiPoint";
  /** 线 */
  public static String GEO_LINESTRING = "LineString";
  /** 多线 */
  public static String GEO_MULTILINESTRING = "MultiLineString";
  /** 面 */
  public static String GEO_POLYGON = "Polygon";
  /** 多面 */
  public static String GEO_MULTIPOLYGON = "MultiPolygon";
  /** 几何集合 */
  public static String GEO_GEOMETRYCOLLECTION = "GeometryCollection";
}
