package cc.wanshan.gisdev.utils;

import cc.wanshan.gisdev.common.enums.ResultCode;
import cc.wanshan.gisdev.entity.Result;
import com.google.common.collect.Lists;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.decoder.RESTLayerGroup;
import it.geosolutions.geoserver.rest.decoder.RESTLayerList;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class GeoserverUtils {
  private static final Logger log = LoggerFactory.getLogger(GeoserverUtils.class);

  public static URL url;
  public static GeoServerRESTManager manager;
  public static GeoServerRESTReader reader;
  public static GeoServerRESTPublisher publisher;

  public static final String path = "http://gs.xianyushichuang.com";
  public static final String username = "admin";
  public static final String passwd = "geoserver";

  /** 初始化，发布时进行身份认证 */
  static {
    try {
      url = new URL(path);
      manager = new GeoServerRESTManager(url, username, passwd);
      reader = manager.getReader();
      publisher = manager.getPublisher();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 创建工作空间
   *
   * @param workSpaceName 工作空间名称
   * @param uri 工作空间uri，可为空
   * @return
   * @throws URISyntaxException
   */
  public static boolean createWorkSpace(String workSpaceName, String uri)
      throws URISyntaxException {
    if (uri == null || uri.isEmpty()) {
      return publisher.createWorkspace(workSpaceName);
    } else {
      return publisher.createWorkspace(workSpaceName, new URI(uri));
    }
  }

  /**
   * 发布shp服务
   *
   * @param zipFilePath 文件路径
   * @param workspace 工作空间名称
   * @param storeName 存储源名称
   * @param layerName 图层名称
   * @param srs 参考系
   * @param defaultStyle 图层样式
   * @return
   * @throws Exception
   */
  public static Result publishShp(
      String zipFilePath,
      String workspace,
      String storeName,
      String layerName,
      String srs,
      String defaultStyle)
      throws Exception {
    // 坐标系,判断是否为空
    if (srs == null || srs.isEmpty()) {
      srs = GeoServerRESTPublisher.DEFAULT_CRS;
    }

    File zipFile = new File(zipFilePath);
    boolean published;
    if (defaultStyle == null) {
      published = publisher.publishShp(workspace, storeName, layerName, zipFile, srs);
    } else {
      published = publisher.publishShp(workspace, storeName, layerName, zipFile, srs, defaultStyle);
    }
    if (published) {
      return ResultUtil.success();
    }
    return ResultUtil.error(ResultCode.SHP_PUBLISH_FAIL);
  }

  /**
   * 发布tif数据服务
   *
   * @param fileName 文件路径
   * @param workspace 工作空间名称
   * @param storeName 存储源名称
   * @param layerName 图层名称
   * @param srs 参考系
   * @param defaultStyle 图层样式
   * @return
   * @throws Exception
   */
  public static boolean publishGeoTiff(
      String fileName,
      String workspace,
      String storeName,
      String layerName,
      String srs,
      String defaultStyle,
      double[] bbox)
      throws Exception {
    File geotiff = new File(fileName);
    if (defaultStyle == null || defaultStyle.isEmpty()) {
      return publisher.publishGeoTIFF(workspace, storeName, layerName, geotiff);
    } else {
      return publisher.publishGeoTIFF(
          workspace,
          storeName,
          layerName,
          geotiff,
          srs,
          GSResourceEncoder.ProjectionPolicy.FORCE_DECLARED,
          defaultStyle,
          bbox);
    }
  }

  /** 显示所有发布图层 */
  public List<String> getAllLayers() {
    RESTLayerList layers = reader.getLayers();
    List<String> layerNames = Lists.newArrayList();
    for (int i = 0; i < layers.size(); i++) {
      String layer = layers.get(i).getName();
      layerNames.add(layer);
    }
    return layerNames;
  }

  /** 查找图层 */
  public boolean findLayer(String workspace, String layerName) {
    RESTLayer layer = reader.getLayer(workspace, layerName);
    return (layer == null) ? false : true;
  }

  public boolean findLayerGroup(String layerGroupName) {
    RESTLayerGroup layerGroup = reader.getLayerGroup(layerGroupName);
    return (layerGroup == null) ? false : true;
  }

  public List<String> getLayerNames(String layerGroupName) {
    RESTLayerGroup layerGroup = reader.getLayerGroup(layerGroupName);
    return layerGroup.getPublishedList().getNames();
  }
}
