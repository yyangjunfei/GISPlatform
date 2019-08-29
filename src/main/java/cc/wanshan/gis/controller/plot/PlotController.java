package cc.wanshan.gis.controller.plot;

import cc.wanshan.gis.common.annotation.SystemLog;
import cc.wanshan.gis.common.enums.LogType;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.service.plot.of3d.PlotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "PlotController", tags = "标绘接口")
@RestController
@RequestMapping("/rest/plot")
public class PlotController {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PlotService plotService;

    @SystemLog(description = "增加标绘", type = LogType.OPERATION)
    @ApiOperation(value = "增加标绘", notes = "增加标绘")
    @PostMapping
    public Result add(@RequestParam String jsonString) {

        LOG.info("增加标绘");
        return plotService.save(jsonString);
    }

    @SystemLog(description = "修改标绘", type = LogType.OPERATION)
    @ApiOperation(value = "修改标绘", notes = "修改标绘")
    @PutMapping
    public Result upload(@RequestBody String jsonString) {

        LOG.info("修改标绘");
        return plotService.update(jsonString);
    }

    @SystemLog(description = "删除标绘", type = LogType.OPERATION)
    @ApiOperation(value = "删除标绘", notes = "删除标绘")
    @DeleteMapping("/{type}/{id}")
    public Result deleteById(@PathVariable String type, @PathVariable String id) {
        LOG.info("删除标绘");
        return plotService.deleteById(type, id);
    }

    @SystemLog(description = "查询标绘", type = LogType.OPERATION)
    @ApiOperation(value = "查询标绘", notes = "查询标绘")
    @ApiImplicitParams(@ApiImplicitParam(name = "type", value = "数据类型", required = false))
    @GetMapping("/findAll")
    public Result findAll(@RequestParam(required = false) String type) {
        LOG.info("查询标绘");
        return plotService.findAll(type);
    }

}
