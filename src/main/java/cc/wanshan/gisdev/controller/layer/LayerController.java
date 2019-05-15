package cc.wanshan.gisdev.controller.layer;

import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.entity.drawlayer.Layer;
import cc.wanshan.gisdev.service.layerservice.LayerService;
import cc.wanshan.gisdev.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.core.instrument.util.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;

@Controller
@MapperScan("cc.wanshan.demo.entity")
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/layer")
public class LayerController {
    private static final Logger logger= LoggerFactory.getLogger(LayerController.class);
    @Resource(name ="layerServiceImpl" )
    private LayerService layerService;
    @RequestMapping("/newlayer")
    @ResponseBody
    public Result newLayer(@RequestBody JSONObject jsonObject) {
        logger.info("newLayer::jsonObject = [{}]",jsonObject);
        Layer layer = new Layer();
        if (jsonObject.getInteger("storeId")!=null&&jsonObject.getInteger("storeId")!=0&& StringUtils.isNotBlank(jsonObject.getString("layerName"))&& StringUtils.isNotBlank(jsonObject.getString("type"))&& StringUtils.isNotBlank(jsonObject.getString("EPSG"))&& StringUtils.isNotBlank(jsonObject.getString("workspace"))) {
            String workspace = jsonObject.getString("workspace");
            layer.setLayerName(jsonObject.getString("layerName"));
            layer.setStoreId(jsonObject.getInteger("storeId"));
            layer.setType(jsonObject.getString("type"));
            layer.setEpsg(jsonObject.getString("EPSG"));
            return layerService.insertLayer(layer,workspace);
        } else {
            logger.warn("图层参数为null:"+jsonObject.toString());
            return ResultUtil.error(2, "jsonObject为空");
        }
    }
    @RequestMapping("/savelayer")
    @ResponseBody
    public Result saveLayer (@RequestBody JSONObject jsonObject) throws IOException {
        logger.info("saveLayer::jsonObject = [{}]",jsonObject);
        Result result= layerService.insertFeatures(jsonObject);
        assert result != null;
        if (result.getCode()==0){
            return ResultUtil.success();
        }else {
            logger.warn("警告:"+result.getMsg());
            return ResultUtil.error(1,result.getMsg());
        }
    }
    @RequestMapping("/deletelayer")
    @ResponseBody
    public Result deleteLayer(@RequestBody JSONObject jsonObject){
        return layerService.deleteLayer(jsonObject);
    }
    @RequestMapping("/findLayerCountByLayerName")
    @ResponseBody
    public Result findLayerCountByLayerName(@RequestParam String workspace ,String layerName){
        logger.info("findLayerCountByLayerName::workspace = [{}], layerName = [{}]",workspace, layerName);
        if (StringUtils.isNotBlank(workspace)&& StringUtils.isNotBlank(layerName)){
            Result result = layerService.findLayerCountByUsernameAndLayerName(workspace, layerName);
            return result;
        }else {
            logger.warn("警告！传入参数存在null值");
            return ResultUtil.error(1,"传入参数存在null值");
        }
    }
    @RequestMapping("/searchLayer")
    @ResponseBody
    public Result searchLayer(@RequestBody JSONObject jsonObject){
        logger.info("searchLayer::jsonObject = [{}]",jsonObject);
        Result result = layerService.searchLayer(jsonObject);
        if (result.getCode()==0){
            return result;
        }else {
            logger.warn("警告!"+result.getMsg());
            return result;
        }
    }
}
