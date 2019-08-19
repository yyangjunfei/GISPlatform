package cc.wanshan.gis.service.layer.export.impl;

import cc.wanshan.gis.dao.layer.LayerDao;
import cc.wanshan.gis.entity.plot.of2d.Layer;
import cc.wanshan.gis.service.layer.export.ExportService;
import cc.wanshan.gis.utils.geo.GeoToolsUtils;
import cc.wanshan.gis.utils.geo.GeometryCreator;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "exportServiceImpl")
/**
 * @author Li Cheng
 * @date 2019/6/14 16:25
 */
public class ExportServiceImpl implements ExportService {

  private static final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);
  @Resource
  private LayerDao layerDao;

  @Override
  public void export(String layerId, String suffix, HttpServletResponse response) throws Exception {
    if (!StringUtils.isNotBlank(layerId)) {
      return;
    }
    Layer layer = layerDao.findLayerByLayerId(layerId);
    if (layer == null) {
      return;
    }
    if ("shp".equals(suffix.toLowerCase())) {
      writeShp(layer, response);
    }
    if ("json".equals(suffix.toLowerCase())) {
      writeJson(layer, response);
    }
    if ("kml".equals(suffix.toLowerCase())) {
      writeKml(layer, response);
    }
    if ("gpx".equals(suffix.toLowerCase())) {

    }
  }

  private void writeShp(Layer layer, HttpServletResponse response) throws Exception {
    Boolean aBoolean = writeSHP(layer);
    if (aBoolean) {
      String path = "./download/" + layer.getLayerNameZH() + layer.getLayerName();
      List files = new ArrayList();
      File file1 = new File(path + ".shp");
      File file2 = new File(path + ".dbf");
      File file3 = new File(path + ".fix");
      File file4 = new File(path + ".prj");
      File file5 = new File(path + ".shx");
      files.add(file1);
      files.add(file2);
      files.add(file3);
      files.add(file4);
      files.add(file5);
      downLoadFiles(path, files, response);
    }
  }

  private void writeJson(Layer layer, HttpServletResponse response) throws Exception {
    Boolean aBoolean = writeJSON(layer);
    if (aBoolean) {
      String path = "./download/" + layer.getLayerNameZH() + layer.getLayerName();
      List files = new ArrayList();
      File file1 = new File(path + ".json");
      files.add(file1);
      downLoadFiles(path, files, response);
    }
  }

  private void writeKml(Layer layer, HttpServletResponse response) throws Exception {
    Boolean aBoolean = writeKML(layer);
    if (aBoolean) {
      String path = "./download/" + layer.getLayerNameZH() + layer.getLayerName();
      List files = new ArrayList();
      File file1 = new File(path + ".kml");
      files.add(file1);
      downLoadFiles(path, files, response);
    }
  }

  private static HttpServletResponse downLoadFiles(String path, List<File> files,
      HttpServletResponse response)
      throws Exception {
    logger.info("downLoadFiles::files = [{}], response = [{}]", files, response);
    try {
      // List<File> 作为参数传进来，就是把多个文件的路径放到一个list里面
      // 创建一个临时压缩文件

      // 临时文件可以放在CDEF盘中，但不建议这么做，因为需要先设置磁盘的访问权限，最好是放在服务器上，方法最后有删除临时文件的步骤
      String zipFilename = path + ".zip";
      File file = new File(zipFilename);
      file.createNewFile();
      if (!file.exists()) {
        file.createNewFile();
      }
      response.reset();
      // response.getWriter()
      // 创建文件输出流
      FileOutputStream fous = new FileOutputStream(file);
      ZipOutputStream zipOut = new ZipOutputStream(fous);
      zipFile(files, zipOut);
      zipOut.close();
      fous.close();
      return downloadZip(file, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  /**
   * 把接受的全部文件打成压缩包
   */
  private static void zipFile(List files, ZipOutputStream outputStream) {
    int size = files.size();
    for (int i = 0; i < size; i++) {
      File file = (File) files.get(i);
      zipFile(file, outputStream);
    }
  }

  /**
   * 根据输入的文件与输出流对文件进行打包
   */
  private static void zipFile(File inputFile, ZipOutputStream ouputStream) {
    try {
      if (inputFile.exists()) {
        if (inputFile.isFile()) {
          FileInputStream IN = new FileInputStream(inputFile);
          BufferedInputStream bins = new BufferedInputStream(IN, 512);
          ZipEntry entry = new ZipEntry(inputFile.getName());
          ouputStream.putNextEntry(entry);
          // 向压缩文件中输出数据
          int nNumber;
          byte[] buffer = new byte[512];
          while ((nNumber = bins.read(buffer)) != -1) {
            ouputStream.write(buffer, 0, nNumber);
          }
          // 关闭创建的流对象
          bins.close();
          IN.close();
        } else {
          try {
            File[] files = inputFile.listFiles();
            for (int i = 0; i < files.length; i++) {
              zipFile(files[i], ouputStream);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        File f = new File(inputFile.getPath());
        f.delete();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static HttpServletResponse downloadZip(File file, HttpServletResponse response) {
    if (!file.exists()) {
      System.out.println("待压缩的文件目录：" + file + "不存在.");
    } else {
      try {
        // 以流的形式下载文件。
        InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        // 如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
        response.setHeader("Content-Disposition",
            "attachment;filename=" + new String(file.getName().getBytes("GB2312"), "ISO8859-1"));
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      } finally {
        try {
          File f = new File(file.getPath());
          f.delete();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return response;
  }

  @Override
  public Boolean writeSHP(Layer layer) throws Exception {
    logger.info("writeSHP::path = [{}], layer = [{}]", layer);
    ShapefileDataStore ds = null;
    FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;
    try {
      /**
       * 将几何对象信息写入一个shapfile文件
       */
      //String path="C:\\my.shp";

      //1.创建shape文件对象
      String path = "./download/" + layer.getLayerNameZH() + layer.getLayerName() + ".shp";
      File file = new File(path);
      Map<String, Serializable> params = new HashMap<>();
      //用于捕获参数需求的数据类
      //URLP:url to the .shp file.
      params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
      //2.创建一个新的数据存储——对于一个还不存在的文件。
      ds = (ShapefileDataStore) new ShapefileDataStoreFactory()
          .createNewDataStore(params);

      //3.定义图形信息和属性信息
      //SimpleFeatureTypeBuilder 构造简单特性类型的构造器
      SimpleFeatureTypeBuilder tBuilder = new SimpleFeatureTypeBuilder();
      tBuilder.setCRS(DefaultGeographicCRS.WGS84);
      tBuilder.setName("shapefile");
      tBuilder.add("gid", Long.class);
      tBuilder.add("name", String.class);
      tBuilder.add("describe", String.class);
      String type = layer.getType();
      if ("point".equals(type.toLowerCase())) {
        tBuilder.add("the_geom", Point.class);
        ds.createSchema(tBuilder.buildFeatureType());
        ds.setCharset(Charset.forName("gbk"));
        writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
        logger.info("元素类型为point");
        List<cc.wanshan.gis.entity.plot.of2d.Point> pointList = layer.getPointList();
        if (pointList.size() > 0) {
          logger.info("size为：" + pointList.size());
          Long po = 0L;
          SimpleFeature next;
          for (cc.wanshan.gis.entity.plot.of2d.Point point : pointList) {
            po = po++;
            String string = (String) point.getGeom();
            next = writer.next();
            Point point1 = pointGeoJsonStringToText(string);
            logger.info("pointGeoJsonStringToText(string)" + point1);
            next.setAttribute("the_geom", point1);
            next.setAttribute("gid", po);
            next.setAttribute("name", point.getFeatureName());
            next.setAttribute("describe", point.getDescribe());
          }
        }
      }
      if ("linestring".equals(type.toLowerCase())) {
        tBuilder.add("the_geom", LineString.class);
        ds.createSchema(tBuilder.buildFeatureType());
        ds.setCharset(Charset.forName("gbk"));
        writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
        logger.info("写入类型为linestring");
        List<cc.wanshan.gis.entity.plot.of2d.LineString> lineStringList = layer.getLineStringList();
        if (lineStringList.size() > 0) {
          Long li = 0L;
          logger.info("size为：" + lineStringList.size());
          SimpleFeature next;
          for (cc.wanshan.gis.entity.plot.of2d.LineString lineString : lineStringList) {
            li = li++;
            String string = (String) lineString.getGeom();
            LineString lineString1 = lineStringGeoJsonStringToText(string);
            next = writer.next();
            logger.info("GeometryCreator.createLine(objects)" + lineString1);
            next.setAttribute("the_geom", lineString1);
            next.setAttribute("gid", li);
            next.setAttribute("name", lineString.getFeatureName());
            next.setAttribute("describe", lineString.getDescribe());
          }
        }
      }
      if ("polygon".equals(type.toLowerCase())) {
        tBuilder.add("the_geom", Polygon.class);
        ds.createSchema(tBuilder.buildFeatureType());
        ds.setCharset(Charset.forName("gbk"));
        writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
        logger.info("写入类型为polygon");
        List<cc.wanshan.gis.entity.plot.of2d.Polygon> polygonList = layer.getPolygonList();
        if (polygonList.size() > 0) {
          logger.info("size为：" + polygonList.size());
          Long pg = 0L;
          SimpleFeature next;
          for (cc.wanshan.gis.entity.plot.of2d.Polygon polygon : polygonList) {
            pg = ++pg;
            Polygon polygon1;
            if (polygon.getGeom() != null) {
              String string = (String) polygon.getGeom();
              polygon1 = polygonGeoJsonStringToText(string);
            } else {
              String circle = polygon.getCircle();
              polygon1 = circleStringToText(circle);
            }
            next = writer.next();
            logger.info("GeometryCreator.createLine(objects)" + polygon1);
            next.setAttribute("the_geom", polygon1);
            next.setAttribute("gid", pg);
            next.setAttribute("name", polygon.getFeatureName());
            next.setAttribute("describe", polygon.getDescribe());
          }
        }
      }
      //写入
      writer.write();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //关闭
      assert writer != null;
      writer.close();
      //释放资源
      ds.dispose();
    }
    return false;
  }

  @Override
  public Boolean writeJSON(Layer layer) {
    String path = "./download/" + layer.getLayerNameZH() + layer.getLayerName() + ".json";
    File file = new File(path);
    BufferedWriter bufferedWriter;
    try {
      file.createNewFile();
      if (!file.exists()) {
        file.createNewFile();
      }
      bufferedWriter = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath())));
      String type = layer.getType();
      SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
      simpleFeatureTypeBuilder.setName("testType");
      simpleFeatureTypeBuilder.crs(DefaultGeographicCRS.WGS84);
      simpleFeatureTypeBuilder.add("geom", Geometry.class);
      simpleFeatureTypeBuilder.add("name", String.class);
      simpleFeatureTypeBuilder.add("describe", String.class);
      SimpleFeatureType simpleFeatureType = simpleFeatureTypeBuilder.buildFeatureType();
      SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(simpleFeatureType);
      FeatureJSON fjson = new FeatureJSON(new GeometryJSON(15));
      List<SimpleFeature> features = new ArrayList<>();
      SimpleFeatureCollection collection = new ListFeatureCollection(simpleFeatureType, features);
      if ("point".equals(type.toLowerCase())) {
        List<cc.wanshan.gis.entity.plot.of2d.Point> pointList = layer.getPointList();
        for (cc.wanshan.gis.entity.plot.of2d.Point point : pointList) {
          String string = (String) point.getGeom();
          Point point1 = pointGeoJsonStringToText(string);
          featureBuilder.add(point1);
          featureBuilder.add(point.getFeatureName());
          featureBuilder.add(point.getDescribe());
          SimpleFeature feature = featureBuilder.buildFeature(null);
          logger.info("featureBuilder.buildFeature(null)" + feature);
          features.add(feature);
        }
      }
      if ("linestring".equals(type.toLowerCase())) {
        List<cc.wanshan.gis.entity.plot.of2d.LineString> lineStringList = layer.getLineStringList();
        for (cc.wanshan.gis.entity.plot.of2d.LineString lineString : lineStringList) {
          String string = (String) lineString.getGeom();
          LineString lineString1 = lineStringGeoJsonStringToText(string);
          featureBuilder.add(lineString1);
          featureBuilder.add(lineString.getFeatureName());
          featureBuilder.add(lineString.getDescribe());
          SimpleFeature feature = featureBuilder.buildFeature(null);
          logger.info("featureBuilder.buildFeature(null)" + feature);
          features.add(feature);
        }
      }
      if ("polygon".equals(type.toLowerCase())) {
        List<cc.wanshan.gis.entity.plot.of2d.Polygon> polygonList = layer.getPolygonList();
        for (cc.wanshan.gis.entity.plot.of2d.Polygon polygon : polygonList) {
          Polygon polygon1;
          if (polygon.getGeom() != null) {
            String string = (String) polygon.getGeom();
            polygon1 = polygonGeoJsonStringToText(string);
          } else {
            String circle = polygon.getCircle();
            polygon1 = circleStringToText(circle);
          }
          featureBuilder.add(polygon1);
          featureBuilder.add(polygon.getFeatureName());
          featureBuilder.add(polygon.getDescribe());
          SimpleFeature feature = featureBuilder.buildFeature(null);
          logger.info("featureBuilder.buildFeature(null)" + feature);
          features.add(feature);
        }
      }
      StringWriter writer = new StringWriter();
      fjson.writeFeatureCollection(collection, writer);
      logger.info("fjson.writeFeatureCollection(collection, writer)" + writer);
      StringBuffer buffer = writer.getBuffer();
      bufferedWriter.write(buffer.toString());
      bufferedWriter.flush();
      bufferedWriter.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Boolean writeKML(Layer layer) {
    String path = "./download/" + layer.getLayerNameZH() + layer.getLayerName() + ".kml";
    File file = new File(path);
    try {
      //out = new FileWriter(path);
      file.createNewFile();
      if (!file.exists()) {
        file.createNewFile();
      }
      String type = layer.getType();
      Document doc = DocumentHelper.createDocument();
      Element kml = doc.addElement("kml");
      Element document = kml.addElement("Document");
      Element schema = document.addElement("Schema");
      schema.addAttribute("name", "test_1");
      schema.addAttribute("id", "test_1");
      Element simpleField = schema.addElement("SimpleField");
      simpleField.addAttribute("type", "string");
      simpleField.addAttribute("name", "name");
      /*Element simpleField1 = schema.addElement("SimpleField");
      simpleField1.addAttribute("type","string");
      simpleField1.addAttribute("name","describe");*/
      Element folder = document.addElement("Folder");
      if ("point".equals(type.toLowerCase())) {
        List<cc.wanshan.gis.entity.plot.of2d.Point> pointList = layer.getPointList();
        for (cc.wanshan.gis.entity.plot.of2d.Point point : pointList) {
          String string = (String) point.getGeom();
          Point point1 = pointGeoJsonStringToText(string);
          Element name = folder.addElement("name");
          name.setText("Test");
          Element placemark = folder.addElement("Placemark");
          placemark.addAttribute("id", point.getFeatureId());
          Element extendedData = placemark.addElement("ExtendedData");
          Element schemaData = extendedData.addElement("SchemaData");
          schemaData.addAttribute("schemaUrl", "test_1");
          Element simpleData = schemaData.addElement("SimpleData");
          simpleData.addAttribute("name", "name");
          simpleData.setText(point.getFeatureName());
          /*Element simpleData1 = schemaData.addElement("SimpleData");
          simpleData1.addAttribute("name","describe");
          simpleData1.setText(point.getDescribe());*/
          Element point2 = placemark.addElement("Point");
          Element coordinates = point2.addElement("coordinates");
          coordinates.setText(point1.getCoordinate().x + "," + point1.getCoordinate().y);
        }
      }
      if ("linestring".equals(type.toLowerCase())) {
        List<cc.wanshan.gis.entity.plot.of2d.LineString> lineStringList = layer.getLineStringList();
        for (cc.wanshan.gis.entity.plot.of2d.LineString lineString : lineStringList) {
          String string = (String) lineString.getGeom();
          LineString lineString1 = lineStringGeoJsonStringToText(string);
          Element name = folder.addElement("name");
          name.setText("Test");
          Element placemark = folder.addElement("Placemark");
          placemark.addAttribute("id", lineString.getFeatureId());
          Element extendedData = placemark.addElement("ExtendedData");
          Element schemaData = extendedData.addElement("SchemaData");
          schemaData.addAttribute("schemaUrl", "test_1");
          Element simpleData = schemaData.addElement("SimpleData");
          simpleData.addAttribute("name", "name");
          simpleData.setText(lineString.getFeatureName());
          /*Element simpleData1 = schemaData.addElement("SimpleData");
          simpleData1.addAttribute("name","describe");
          simpleData1.setText(lineString.getDescribe());*/
          Element point2 = placemark.addElement("LineString");
          Element coordinates = point2.addElement("coordinates");
          StringBuilder stringBuilder = new StringBuilder();
          for (Coordinate coordinate : lineString1.getCoordinates()) {
            stringBuilder.append(coordinate.x);
            stringBuilder.append(",");
            stringBuilder.append(coordinate.y);
            stringBuilder.append(" ");
          }
          coordinates.setText(stringBuilder.toString());
        }
      }
      if ("polygon".equals(type.toLowerCase())) {
        List<cc.wanshan.gis.entity.plot.of2d.Polygon> polygonList = layer.getPolygonList();
        for (cc.wanshan.gis.entity.plot.of2d.Polygon polygon : polygonList) {
          Polygon polygon1;
          if (polygon.getGeom() != null) {
            String string = (String) polygon.getGeom();
            polygon1 = polygonGeoJsonStringToText(string);
          } else {
            String circle = polygon.getCircle();
            polygon1 = circleStringToText(circle);
          }
          Element name = folder.addElement("name");
          name.setText("Test");
          Element placemark = folder.addElement("Placemark");
          placemark.addAttribute("id", polygon.getFeatureId());
          Element extendedData = placemark.addElement("ExtendedData");
          Element schemaData = extendedData.addElement("SchemaData");
          schemaData.addAttribute("schemaUrl", "test_1");
          Element simpleData = schemaData.addElement("SimpleData");
          simpleData.addAttribute("name", "name");
          simpleData.setText(polygon.getFeatureName());
          /*Element simpleData1 = schemaData.addElement("SimpleData");
          simpleData1.addAttribute("name","describe");
          simpleData1.setText(polygon.getDescribe());*/
          Element polygon2 = placemark.addElement("Polygon");
          Element outerBoundaryIs = polygon2.addElement("outerBoundaryIs");
          Element linearRing = outerBoundaryIs.addElement("LinearRing");
          Element tessellate = linearRing.addElement("tessellate");
          tessellate.setText("1");
          Element coordinates = linearRing.addElement("coordinates");
          StringBuilder stringBuilder = new StringBuilder();
          for (Coordinate coordinate : polygon1.getCoordinates()) {
            stringBuilder.append(coordinate.x);
            stringBuilder.append(",");
            stringBuilder.append(coordinate.y);
            stringBuilder.append(" ");
          }
          coordinates.setText(stringBuilder.toString());
        }
      }
      OutputFormat format = OutputFormat.createPrettyPrint();
      format.setEncoding("GBK");
      XMLWriter xmlWriter = new XMLWriter(
          new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath())), format);
      //fjson.writeFeatureCollection(collection, writer);
      logger.info("fjson.writeFeatureCollection(collection, writer)" + doc.toString());
      xmlWriter.write(doc);
      xmlWriter.flush();
      xmlWriter.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Boolean writeGPS(Layer layer) {
    return null;
  }

  private static Point pointGeoJsonStringToText(String pointString)
      throws Exception {
    logger.info("pointGeoJsonStringToText::pointString = [{}]", pointString);
    String transform = GeoToolsUtils.transform(pointString, "EPSG:3857", "EPSG:4326");
    logger
        .info("GeoToolsUtils.transform(string, \"EPSG:3857\", \"EPSG:4326\")" + transform);
    GeometryJSON geometryJSON = new GeometryJSON();
    StringReader stringReader = new StringReader(transform);
    Geometry read = geometryJSON.read(stringReader);
    Coordinate coordinate = read.getCoordinate();
    logger.info("geometryJSON.read(stringReader)" + read.toString());
    return GeometryCreator.createPoint(coordinate.y, coordinate.x);

  }

  private static LineString lineStringGeoJsonStringToText(String lineStringString)
      throws Exception {
    logger.info("lineStringGeoJsonStringToText::lineStringString = [{}]", lineStringString);
    String transform = GeoToolsUtils.transform(lineStringString, "EPSG:3857", "EPSG:4326");
    logger
        .info("GeoToolsUtils.transform(string, \"EPSG:3857\", \"EPSG:4326\")" + transform);
    GeometryJSON geometryJSON = new GeometryJSON();
    StringReader stringReader = new StringReader(transform);
    Geometry read = geometryJSON.read(stringReader);
    Coordinate[] coordinates = read.getCoordinates();
    List<Coordinate> newCoordinates = new ArrayList<>();
    for (Coordinate coordinate : coordinates) {
      Coordinate coordinate1 = new Coordinate(coordinate.y, coordinate.x);
      logger.info("new Coordinate(coordinate.y, coordinate.x)" + coordinate1.toString());
      newCoordinates.add(coordinate1);
    }
    Coordinate[] objects = newCoordinates.toArray(new Coordinate[newCoordinates.size()]);
    return GeometryCreator.createLine(objects);
  }

  private static Polygon polygonGeoJsonStringToText(String polygonString)
      throws Exception {
    logger.info("polygonGeoJsonStringToText::polygonString = [{}]", polygonString);
    String transform = GeoToolsUtils.transform(polygonString, "EPSG:3857", "EPSG:4326");
    logger.info(
        "GeoToolsUtils.transform(string, \"EPSG:3857\", \"EPSG:4326\")" + transform);
    GeometryJSON geometryJSON = new GeometryJSON();
    StringReader stringReader = new StringReader(transform);
    Geometry read = geometryJSON.read(stringReader);
    Coordinate[] coordinates = read.getCoordinates();
    List<Coordinate> newCoordinates = new ArrayList<>();
    for (Coordinate coordinate : coordinates) {
      Coordinate coordinate1 = new Coordinate(coordinate.y, coordinate.x);
      logger.info("new Coordinate(coordinate.y, coordinate.x)" + coordinate1.toString());
      newCoordinates.add(coordinate1);
    }
    Coordinate[] objects = newCoordinates.toArray(new Coordinate[newCoordinates.size()]);
    return GeometryCreator.createPolygon(objects);
  }

  private static Polygon circleStringToText(String circleString)
      throws Exception {
    JSONObject jsonObject = JSONObject.parseObject(circleString);
    double radius = Double.parseDouble(jsonObject.getString("radius"));
    JSONArray coordinates = jsonObject.getJSONArray("coordinates");
    Double x = coordinates.getDouble(0);
    Double y = coordinates.getDouble(1);
    Coordinate coordinate = new Coordinate(y, x);
    Coordinate transform = GeoToolsUtils.transform(coordinate, "EPSG:3857", "EPSG:4326");
    return GeometryCreator.createCircle(transform.x, transform.y, radius);
  }
}
