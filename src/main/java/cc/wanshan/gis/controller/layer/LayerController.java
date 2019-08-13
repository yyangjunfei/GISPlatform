package cc.wanshan.gis.controller.layer;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.layer.thematic.Thematic;
import cc.wanshan.gis.entity.plot.of2d.Layer;
import cc.wanshan.gis.entity.plot.of2d.LineString;
import cc.wanshan.gis.entity.plot.of2d.Point;
import cc.wanshan.gis.entity.plot.of2d.Polygon;
import cc.wanshan.gis.service.layer.export.ExportService;
import cc.wanshan.gis.service.layer.style.StyleService;
import cc.wanshan.gis.service.layer.thematic.LayerService;
import cc.wanshan.gis.service.layer.thematic.ThematicService;
import cc.wanshan.gis.service.layer.thematic.ThematicUserService;
import cc.wanshan.gis.utils.base.ResultUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Li Cheng
 * @Date 14:28 2019/6/18
 **/
@Api(value = "LayerController", tags = "二维标绘功能")
@RestController
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/layer")
public class LayerController {

  private static final Logger logger = LoggerFactory.getLogger(LayerController.class);

  @Resource(name = "layerServiceImpl")
  private LayerService layerService;
  @Resource(name = "thematicServiceImpl")
  private ThematicService thematicServiceImpl;
  @Resource(name = "thematicUserServiceImpl")
  private ThematicUserService thematicUserServiceImpl;
  @Resource(name = "styleServiceImpl")
  private StyleService styleServiceImpl;
  @Resource(name = "exportServiceImpl")
  private ExportService exportServiceImpl;

  @ApiOperation(value = "保存标绘图层", notes = "新增标绘图层或者根据图层Id更新图层")
  @ApiImplicitParam(name = "layer", value = "layer实体类对象", required = true)
  @PostMapping("/savelayer")
  @ResponseBody
  public Result saveLayer(@RequestBody Layer layer) {
    logger.info("saveLayer::layer = [{}]", layer);
    return layerService.save(layer);
  }

  @ApiOperation(value = "删除标绘图层", notes = "根据图层集合批量删除图层,只需要封装layerId和type即可")
  @ApiImplicitParam(name = "layerList", value = "封装了layerId和type的layer集合", required = true)
  @DeleteMapping("/deletelayer")
  @ResponseBody
  public Result deleteLayer(@RequestBody List<Layer> layerList) {
    logger.info("deleteLayer::layerList = [{}]", layerList);
      return layerService.delete(layerList);
  }
  @ApiOperation(value = "删除标绘要素", notes = "根据featureId集合批量删除feature")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "featureIds", value = "封装了featureId的集合", required =  true),
      @ApiImplicitParam(name = "type", value = "features的类型", required =  true)
  })
  @DeleteMapping("/deletefeature")
  @ResponseBody
  public Result deleteFeature(@RequestParam(value = "featureIds") List featureIds,
      @RequestParam("type") String type) {
    logger.info("deleteFeature::featureIds = [{}], type = [{}]", featureIds, type);
    return layerService.deleteFeature(featureIds, type);
  }

  @ApiOperation(value = "查询图层", notes = "根据userId和layerName查询图层")
  @ApiImplicitParams({@ApiImplicitParam(name = "layerName", value = "图层名", required = true),
      @ApiImplicitParam(name = "userId", value = "用户Id", required = true)})
  @GetMapping(value = "/findbyuseridlayername/{userId}/{layerName}")
  @ResponseBody
  public Result findLayerCountByLayerName(@PathVariable String userId,
      @PathVariable String layerName) {
    logger.info("findLayerCountByLayerName::userId = [{}], layerName = [{}]", userId, layerName);
    return layerService.findByUserIdAndLayerName(userId, layerName);
  }

  @PostMapping("/searchlayer")
  @ResponseBody
  public Result searchLayer(@RequestParam(value = "thematicName") String thematicName,
      @RequestParam(value = "layerName") String layerName) {
    logger.info("searchLayer::thematicName = [{}], layerName = [{}]", thematicName, layerName);
    return layerService.searchLayer(thematicName, layerName);
  }

  @ApiOperation(value = "查询专题图层", notes = "根据userId查询所属专题图层")
  @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
  @GetMapping(value = "/findthematiclayers/{userId}")
  @ResponseBody
  public Result findThematicLayers(@PathVariable String userId) {
    logger.info("findThematicLayers::userId = [{}]", userId);
    if (StringUtils.isNotBlank(userId)) {
      List<Thematic> thematicList = thematicUserServiceImpl
          .findThematicLayersByUserId(userId);
      logger.info("findThematicLayers::thematicId = [{}], bindingResult = [{}]" + thematicList);
      JSONArray jsonArray = (JSONArray) JSONArray.toJSON(thematicList);
      return ResultUtil.success(jsonArray);
    }
    return ResultUtil.error("参数为null");
  }

  @ApiOperation(value = "修改图层", notes = "修改图层属性")
  @ApiImplicitParam(name = "layer", value = "实体类对象", required = true)
  @PostMapping(value = "/updatelayer")
  @ResponseBody
  public Result updateLayer(@RequestBody Layer layer) {
    logger.info("updateLayer::layer = [{}]", layer);
    return layerService.update(layer);
  }

  @ApiOperation(value = "查询图层", notes = "根据layerId查询图层信息")
  @ApiImplicitParam(name = "layerId", value = "图层 Id", required = true)
  @GetMapping("/findlayerbylayerid/{layerId}")
  @ResponseBody
  public Result findByLayerId(@PathVariable String layerId) {
    return layerService.findByLayerId(layerId);
  }

  @ApiOperation(value = "查询风格列表", notes = "根据专题Id查询风格列表")
  @ApiImplicitParam(name = "thematicId", value = "专题Id", required = true)
  @GetMapping("/findstylebythematicid/{thematicId}")
  @ResponseBody
  public Result findStyleByThematicId(@PathVariable String thematicId) {
    logger.info("findStyleByThematicId::thematicId = [{}]", thematicId);
    if (StringUtils.isNotBlank(thematicId)) {
      Thematic thematic = thematicServiceImpl
          .findByThematicId(thematicId);
      if (thematic != null) {
        JSONObject jsonObject1 = (JSONObject) JSONObject.toJSON(thematic);
        return ResultUtil.success(jsonObject1);
      }
    }
    logger.error("参数为null", thematicId);
    return ResultUtil.error(1, "参数为null");
  }

  @ApiOperation(value = "查询风格", notes = "根据风格名查询风格")
  @ApiImplicitParam(name = "styleName", value = "风格名", required = true)
  @GetMapping("/findstylebystylename/{styleName}")
  @ResponseBody
  public Result findStyleByStyleName(@PathVariable String styleName) {
    logger.info("findStyleByStyleName::styleName = [{}]", styleName);
    return styleServiceImpl.findByStyleName(styleName);
  }

  @ApiOperation(value = "查询图层列表", notes = "根据userId查询图层列表")
  @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
  @GetMapping("/findbyuserid/{userId}")
  @ResponseBody
  public Result findLayerByUserId(@PathVariable String userId) {
    logger.info("findLayerByUserId::userId = [{}]", userId);
    return layerService.findByUserId(userId);
  }

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
