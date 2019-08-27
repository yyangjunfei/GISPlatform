package cc.wanshan.gis.controller.metadata;
import cc.wanshan.gis.common.annotation.SystemLog;
import cc.wanshan.gis.common.enums.LogType;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.common.PageBean;
import cc.wanshan.gis.entity.metadata.metadata;
import cc.wanshan.gis.service.layer.geoserver.GeoServerService;
import cc.wanshan.gis.service.metadata.DataManagementService;
import cc.wanshan.gis.utils.LanguageUtils;
import cc.wanshan.gis.utils.base.ResultUtil;
import cc.wanshan.gis.utils.geo.GeoServerUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Api(value = "DataManagementController", tags = "shp数据发布接口")
@RestController
@RequestMapping("/rest/publish")
@CrossOrigin
public class DataManagementController {

    private static Logger LOG = LoggerFactory.getLogger(DataManagementController.class);

    @Autowired
    private DataManagementService dataManagementService;

    @Autowired
    private GeoServerService geoServerService;

    @SystemLog(description = "存储shp数据到DB", type = LogType.OPERATION)
    @ApiOperation(value = "存储shp数据到DB", notes = "metadataImport,jsonString字段（type类型）")
    @PostMapping("/import")
    @ApiImplicitParams(@ApiImplicitParam(name = "jsonString", value = "页面输入的属性数据", required = false))
    public Result metadataImport(String jsonString, @RequestParam MultipartFile[] file) {

        if (file.length == 0) {
            LOG.error("文件为空");
            return ResultUtil.error("文件为空，请重新上传");
        }

        return dataManagementService.metadataImportPublication(jsonString, file);
    }

    @SystemLog(description = "查询显示存储数据", type = LogType.OPERATION)
    @ApiOperation(value = "查询显示存储数据", notes = "查询显示存储数据")
    @GetMapping("/findLayerProperties")
    public Result findLayerProperties(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "3") Integer pageSize) {
        //查询存储的数据
        PageBean<metadata> pageBean = dataManagementService.findLayerProperties(pageNum,pageSize);
        if (pageBean!=null){
            return ResultUtil.success(pageBean);
        }else {
            return ResultUtil.error("查询错误!");
        }
    }

    @SystemLog(description = "根据id删除存储数据", type = LogType.OPERATION)
    @ApiOperation(value = "根据id删除存储数据", notes = "根据id删除存储数据")
    @DeleteMapping("/deleteLayerPropertiesData")
    public Result deleteLayerPropertiesData(@RequestParam("id") int id) {
        LOG.info("查询显示存储数据");
        return dataManagementService.deleteLayerPropertiesData(id);
    }

    @SystemLog(description = "根据id编辑存储数据", type = LogType.OPERATION)
    @ApiOperation(value = "根据id编辑存储数据", notes = "根据id编辑存储数据")
    @PutMapping("/editLayerPropertiesData")
    public Result editLayerPropertiesData(@RequestParam("metadataJson") String metadataJson) {
        LOG.info("根据id编辑存储数据");
        // 构建实体
        metadata metadata = JSONObject.parseObject(metadataJson, cc.wanshan.gis.entity.metadata.metadata.class);

        return dataManagementService.editLayerPropertiesData(metadata);
    }

    @SystemLog(description = "根据id查询存储数据", type = LogType.OPERATION)
    @ApiOperation(value = "根据id查询存储数据", notes = "根据id查询存储数据")
    @GetMapping("/findLayerPropertiesDataById")
    public Result findLayerPropertiesDataById(@RequestParam("id") int id) {
        LOG.info("根据id查询存储数据");
        metadata met = dataManagementService.shpData2Geoserver(id);
        return ResultUtil.success(met);
    }

    /***
     * 封装参数 : workspaceName,storeName,DataType,layerName,safetyLevel,vectorTypes,styleName,createBy
     * @param metadataJson
     * @return
     */

    @SystemLog(description = "复合查询数据", type = LogType.OPERATION)
    @ApiOperation(value = "复合查询数据", notes = "复合查询数据")
    @GetMapping("/findLayerPropertiesData")
    public Result findLayerPropertiesData(String metadataJson) {
        LOG.info("复合查询数据");
        // 构建实体
        metadata metadata = JSONObject.parseObject(metadataJson, cc.wanshan.gis.entity.metadata.metadata.class);

        List<cc.wanshan.gis.entity.metadata.metadata> listMeta = dataManagementService.findLayerPropertiesData(metadata);

        return ResultUtil.success(listMeta);
    }


    @SystemLog(description = "根据ID将存储的shp数据发布到geoServer", type = LogType.OPERATION)
    @ApiOperation(value = "根据ID将存储的shp数据发布到geoServer", notes = "根据ID将存储的shp数据发布到geoServer")
    @PostMapping("/shpData2Geoserver")
    public Result shpData2Geoserver(@RequestParam("id") int id) {
        LOG.info("根据ID将存储的shp数据发布到geoServer");
        //1.前端传入id ，根据id 查询到要发布的,工作空间，存储名称，图层名

        metadata met = dataManagementService.shpData2Geoserver(id);

        String storeName = LanguageUtils.getPinYin(met.getStoreName());

        //获取风格
        String defaultStyle = met.getStyleName();

        Result rest = geoServerService.publishLayer("shpdb", storeName, met.getLayerName(), defaultStyle);

        //发布成功之后 更改数据库中的发布状态为1
        dataManagementService.changePublicationStatus(1,id);

        return rest;
    }

    @SystemLog(description = "删除geoserver发布的图层", type = LogType.OPERATION)
    @ApiOperation(value = "删除geoserver发布的图层", notes = "删除geoserver发布的图层")
    @DeleteMapping ("/deleteShpData2Geoserver")
    public Result deleteShpData2Geoserver(@RequestParam("id") int id){

        LOG.info("删除geoserver发布的图层");
        //1.前端传入id ，根据id 查询到已经发布的,工作空间，存储名称，图层名
        metadata met =dataManagementService.shpData2Geoserver(id);
        String storeName = LanguageUtils.getPinYin(met.getStoreName());
         boolean deleteFlag= GeoServerUtils.publisher.unpublishFeatureType("shpdb",storeName,met.getLayerName());
         if(deleteFlag){
             //删除geoserver发布的图层成功之后 更改数据库中的发布状态为0
             dataManagementService.changePublicationStatus(0,id);
             return  ResultUtil.error("删除geoserver发布的图层成功!");
         }else{
             return  ResultUtil.error("删除geoserver发布的图层失败!");
         }
    }

    @SystemLog(description = "预览显示发布geoServer图层数据", type = LogType.OPERATION)
    @ApiOperation(value = "预览显示发布geoServer图层数据", notes = "预览显示发布geoServer图层数据")
    @GetMapping("/publishPreviewLayer")
    public Result publishPreviewLayer(@RequestParam("id") int id) {

        LOG.info("预览显示发布geoServer图层数据");
        metadata met = dataManagementService.shpData2Geoserver(id);

        String layerName = met.getLayerName();

        String layerNames = "shpdb:" + layerName;

        String url = GeoServerUtils.url.toString() + "/shpdb/wms?service=WMS&version=1.1.0&request=GetMap&layers=" + layerNames + "&bbox=105.4952,32.1507,108.2567,33.8539&width=768&height=473&srs=EPSG:4326&format=application/openlayers";

        return ResultUtil.success(url);

    }

}