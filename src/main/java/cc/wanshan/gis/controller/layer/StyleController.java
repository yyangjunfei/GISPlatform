package cc.wanshan.gis.controller.layer;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.service.layer.style.StyleService;
import cc.wanshan.gis.utils.base.ResultUtil;
import cc.wanshan.gis.utils.geo.GeoServerUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.geosolutions.geoserver.rest.decoder.RESTStyleList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/***
 * @author ：Yang
 * @date ：2019-7-23
 */

@Api(value = "StyleController", tags = "风格文件接口")
@Controller
@ResponseBody
@RequestMapping(value = "/rest/style")
public class StyleController {

    private static Logger LOG = LoggerFactory.getLogger(StyleController.class);

    @Autowired
    private StyleService styleService;

    //创建风格样式
    @ApiOperation(value = "创建风格样式来自SLD文件", httpMethod = "POST", notes = "创建风格样式来自SLD文件")
    @RequestMapping(value = "/createByFile", method = RequestMethod.POST)
    public Result createStyleBySldFile(@RequestParam String workspaceName, @RequestParam String sldName, @RequestParam MultipartFile styleFile) {

        return styleService.createStyleBySldFile(workspaceName, sldName, styleFile);
    }

    @ApiOperation(value = "创建风格样式来自整个SLD文档字符串", httpMethod = "POST", notes = "创建风格样式来自整个SLD文档字符串")
    @RequestMapping(value = "/createByDoc", method = RequestMethod.POST)
    public Result createStyleBySldDocument(@RequestParam String workspaceName, @RequestParam String sldBody, @RequestParam String sldName) {

        return styleService.createStyleBySldDocument(workspaceName, sldBody, sldName);
    }


    //移除样式
    @ApiOperation(value = "移除风格样式", httpMethod = "DELETE", notes = "移除风格样式")
    @RequestMapping(value = "/delete/{styleName}", method = RequestMethod.DELETE)
    public Result deleteStyle(@RequestParam String workspaceName, @PathVariable String styleName) {
        LOG.info("style Name:" + styleName);
        boolean removed = GeoServerUtils.publisher.removeStyleInWorkspace(workspaceName, styleName);
        if (removed) {
            return ResultUtil.success("移除风格样式成功！");
        } else {
            return ResultUtil.error("移除风格样式失败！");
        }
    }

    //样式列表
    @ApiOperation(value = "查询风格类型列表", httpMethod = "GET", notes = "查询风格类型列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result listStyle(@RequestParam String workspaceName) {
        List<String> styleNames = Lists.newArrayList();
        RESTStyleList styleList = GeoServerUtils.reader.getStyles(workspaceName);
        for (int i = 0; i < styleList.size(); i++) {
            styleNames.add(styleList.get(i).getName());
        }
        return ResultUtil.success(styleNames);
    }

    //获取样式内容
    @ApiOperation(value = "获取样式内容", httpMethod = "GET", notes = "获取样式内容")
    @RequestMapping(value = "/get/{styleName}", method = RequestMethod.GET)
    public Result getStyleSld(@RequestParam String workspaceName, @PathVariable String styleName) {
        String sld = GeoServerUtils.reader.getSLD(workspaceName, styleName);
        return ResultUtil.success(sld);
    }

    //修改样式内容
    @ApiOperation(value = "更新样式内容来自SLD文件", httpMethod = "PUT", notes = "更新样式内容来自SLD文件")
    @RequestMapping(value = "/updateStyleByFile", method = RequestMethod.PUT)
    public Result updateStyleByFile(@RequestParam String workspaceName, @RequestParam String sldFilePath, @RequestParam String styleName) {
        Boolean updated = GeoServerUtils.publisher.updateStyleInWorkspace(workspaceName, new File(sldFilePath), styleName);
        if (updated) {
            return ResultUtil.success("修改风格样式成功!");
        } else {
            return ResultUtil.error("修改风格样式失败！");
        }
    }

    //修改样式内容
    @ApiOperation(value = "更新样式内容来自SLD文档字符串", httpMethod = "PUT", notes = "更新样式内容来自SLD文档字符串")
    @RequestMapping(value = "/updateStyleByDoc", method = RequestMethod.PUT)
    public Result updateStyleByDoc(@RequestParam String workspaceName, @RequestParam String sldBody, @RequestParam String styleName) {
        Boolean updated = GeoServerUtils.publisher.updateStyleInWorkspace(workspaceName, sldBody, styleName);
        if (updated) {
            return ResultUtil.success("修改风格样式成功!");
        } else {
            return ResultUtil.error("修改风格样式失败！");
        }
    }

}
