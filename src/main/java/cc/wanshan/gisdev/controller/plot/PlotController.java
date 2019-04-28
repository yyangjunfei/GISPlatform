package cc.wanshan.gisdev.controller.plot;

import cc.wanshan.gisdev.common.enums.ResultCode;
import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.utils.JsonUtils;
import cc.wanshan.gisdev.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "PlotController", tags = "标绘管理接口")
@RestController
@RequestMapping("/rest/plot")
@EnableAutoConfiguration
public class PlotController {

  private static Logger LOG = LoggerFactory.getLogger(PlotController.class);

  @ApiOperation(value = "增加标绘", notes = "增加标绘")
  @PostMapping
  public Result add(@RequestBody String jsonString) {

    LOG.info("增加标绘");
    if (!JsonUtils.validate(jsonString)) {
      return ResultUtil.error(ResultCode.PARAM_NOT_JSON);
    }
    JSONObject jsonStr = JSONObject.parseObject(jsonString);

    return null;
  }
}
