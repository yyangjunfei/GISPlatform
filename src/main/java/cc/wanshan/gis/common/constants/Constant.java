package cc.wanshan.gis.common.constants;

public class Constant {

    /**
     * 类型
     */
    public static final String TYPE = "type";
    /**
     * 关键字
     */
    public static final String KEYWORD = "keyword";
    /**
     * 字符拆分
     */
    public static final String split = ",";
    /** 元数据类型 */
    /**
     * 矢量数据
     */
    public static final Integer SHP_TYPE = 1;
    /**
     * 影像数据
     */
    public static final Integer TIFF_TYPE = 2;
    /**
     * 倾斜摄影数据
     */
    public static final Integer OSGB_TYPE = 3;
    /**
     * 数字高程模型数据
     */
    public static final Integer DEM_TYPE = 4;
    /**
     * 点云数据
     */
    public static final Integer LAS_TYPE = 5;

    /** Geometry空间数据类型 */
    /**
     * 点
     */
    public static final String GEO_POINT = "Point";
    /**
     * 多点
     */
    public static final String GEO_MULTIPOINT = "MultiPoint";
    /**
     * 线
     */
    public static final String GEO_LINESTRING = "LineString";
    /**
     * 多线
     */
    public static final String GEO_MULTILINESTRING = "MultiLineString";
    /**
     * 面
     */
    public static final String GEO_POLYGON = "Polygon";
    /**
     * 多面
     */
    public static final String GEO_MULTIPOLYGON = "MultiPolygon";
    /**
     * 几何集合
     */
    public static final String GEO_GEOMETRYCOLLECTION = "GeometryCollection";
    /**
     * 搜索包围形
     */
    public static final String SEARCH_RECTANGLE = "rectangle";
    /**
     * 搜索级别
     */
    public static final String SEARCH_LEVEL = "level";
    /**
     * 搜索类型-行政区
     */
    public static final String SEARCH_REGION = "1";
    /**
     * 搜索类型-市区-聚合
     */
    public static final String SEARCH_REGION_TERMS = "2";
    /**
     * 搜索类型-模糊-POI
     */
    public static final String SEARCH_VAGUE_POI = "3";
    /**
     * 搜索类型-精确-POI
     */
    public static final String SEARCH_EXACT_POI = "4";
    /**
     * 搜索类型-联想-Suggest
     */
    public static final String SEARCH_SUGGEST = "5";
    /**
     * 搜索类型-通用查询
     */
    public static final String SEARCH_COMMON = "2";
    /**
     * ES搜索索引--行政区
     */
    public static final String INDEX_ES_REGION = "region_data";
    /**
     * ES搜索类型--行政区
     */
    public static final String TYPE_ES_REGION = "Feature";
    /**
     * ES搜索索引--行政区
     */
    public static final String INDEX_ES_POI = "map_data";
    /**
     * ES搜索类型--行政区
     */
    public static final String TYPE_ES_POI = "Feature";
    /**
     * ES搜索分词类型--ik_smart
     */
    public static final String ik_smart = "ik_smart";
    /**
     * ES搜索分词类型--ik_max_word
     */
    public static final String ik_max_word = "ik_max_word";
    /**
     * ES搜索查询数量
     */
    public static int SEARCH_POI_SIZE = 64;
    /**
     * ES搜索查询数量
     */
    public static int CHINA_CITY_SIZE = 334;
    /**
     * JWT生命周期
     */
    public static final long JWT_TTL = 60 * 60 * 1000;
    /**
     * 令牌无效
     */
    public static final String INVALID_TOKEN_MSG = "invalid token";
    /**
     * 过期的令牌
     */
    public static final String EXPIRED_TOKEN_MSG = "expired token";
}
