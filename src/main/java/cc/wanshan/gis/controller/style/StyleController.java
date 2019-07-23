package cc.wanshan.gis.controller.style;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.service.style.StyleService;
import cc.wanshan.gis.utils.GeoServerUtils;
import cc.wanshan.gis.utils.ResultUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.geosolutions.geoserver.rest.decoder.RESTStyleList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.List;

@Api(value = "StyleController", tags = "风格文件接口")
@Controller
@ResponseBody
@RequestMapping(value = "/style")
public class StyleController {

    private static Logger LOG = LoggerFactory.getLogger(StyleController.class);

    @Autowired
    private StyleService styleService;

    //创建风格样式
    @ApiOperation(value = "创建风格样式",httpMethod = "POST",notes = "创建风格样式")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result createStyle(@RequestParam MultipartFile styleFile) {

         return styleService.createStyle(styleFile);
    }


    //移除样式
    @ApiOperation(value = "移除风格样式",httpMethod = "DELETE",notes = "移除风格样式")
    @RequestMapping(value = "/delete/{styleName}", method = RequestMethod.DELETE)
    public Result deleteStyle(@PathVariable String styleName){
        LOG.info("style Name:"+styleName);
        boolean removed = GeoServerUtils.publisher.removeStyle(styleName, true);
        if(removed){
            return  ResultUtil.success("移除风格样式成功！");
        }else {
            return  ResultUtil.error("移除风格样式失败！");
        }
    }

    //样式列表
    @ApiOperation(value = "查询风格类型列表",httpMethod = "GET",notes = "查询风格类型列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<String> listStyle(){
        List<String> styleNames = Lists.newArrayList();
        RESTStyleList styleList = GeoServerUtils.reader.getStyles();
        for (int i=0; i<styleList.size(); i++) {
            styleNames.add(styleList.get(i).getName());
        }
        return styleNames;
    }

    //获取样式内容
    @ApiOperation(value = "获取样式内容",httpMethod = "GET",notes = "获取样式内容")
    @RequestMapping(value = "/get/{styleName}", method = RequestMethod.GET)
    public String getStyleSld(@PathVariable String styleName){
        String sld = GeoServerUtils.reader.getSLD(styleName);
        return sld;
    }

    //修改样式内容
    @ApiOperation(value = "更新样式内容",httpMethod = "PUT",notes = "更新样式内容")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result updateStyle(@RequestParam String sldFilePath,@RequestParam String styleName){
        Boolean updated = GeoServerUtils.publisher.updateStyle(new File(sldFilePath),styleName);
        if(updated){
            return  ResultUtil.success("修改风格样式成功！");
        }else {
            return  ResultUtil.error("修改风格样式失败！");
        }
    }
}
