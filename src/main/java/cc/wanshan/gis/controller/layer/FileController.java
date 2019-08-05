/*
package cc.wanshan.gis.controller.layer;

import cc.wanshan.gis.entity.plot.of2d.Layer;
import cc.wanshan.gis.service.layer.export.ExportService;
import cc.wanshan.gis.service.layer.thematic.LayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

*/
/**
 * @author Li Cheng
 * @date 2019/6/18 9:28
 *//*

@Api(value = "FileController", tags = "文件处理模块")
@RestController
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/file")
public class FileController {

  private static final Logger logger = LoggerFactory.getLogger(FileController.class);

  @Resource(name = "exportServiceImpl")
  private ExportService exportServiceImpl;

  @ApiOperation(value = "导出图层", notes = "根据图层Id和文件类型导出图层数据")
  @ApiImplicitParams({@ApiImplicitParam(name = "layerId", value = "图层Id", required = true),
      @ApiImplicitParam(name = "suffix", value = "导出文件格式，当前支持json，shp，kml格式", required = true)})
  @GetMapping("/export")
  @ResponseBody
  public void export(@RequestParam(value = "layerId") String layerId,
      @RequestParam(value = "suffix") String suffix, HttpServletResponse response)
      throws Exception {
    logger.info("file::layerId = [{}], response = [{}]", layerId, response);
    exportServiceImpl.export(layerId, suffix, response);
  }

}
*/
