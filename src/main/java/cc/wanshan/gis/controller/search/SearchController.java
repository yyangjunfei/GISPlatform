package cc.wanshan.gis.controller.search;

import cc.wanshan.gis.common.annotation.SystemLog;
import cc.wanshan.gis.common.enums.LogType;
import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.service.search.SearchService;
import cc.wanshan.gis.utils.base.ResultUtil;
import cc.wanshan.gis.utils.token.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "SearchController", tags = "搜索接口")
@RestController
@RequestMapping("/rest/search")
public class SearchController {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private SearchService searchService;

    @Autowired
    private SecurityUtils securityUtils;

    @SystemLog(description = "位置搜索", type = LogType.OPERATION)
    @ApiOperation(value = "位置搜索", notes = "位置搜索")
    @GetMapping("/location")
    public Result searchByLocation(@RequestParam double longitude, @RequestParam double latitude, @RequestParam double level) {

        LOG.info("SearchController::searchByLocation longitude = [{}],latitude = [{}],level = [{}]", longitude, latitude, level);

        return searchService.searchByLocation(longitude, latitude, level);
    }

    @SystemLog(description = "名称搜索", type = LogType.OPERATION)
    @ApiOperation(value = "名称搜索", notes = "名称搜索")
    @GetMapping("/name")
    public Result searchByName(@RequestParam String name) {

        LOG.info("SearchController::searchByName name = [{}]", name);

        // 判空
        if (name == null || name.length() <= 0) {
            return ResultUtil.error(ResultCode.PARAM_IS_NULL);
        }

        return searchService.searchByName(name);
    }

    @SystemLog(description = "搜索POI", type = LogType.OPERATION)
    @ApiOperation(value = "搜索POI", notes = "搜索POI")
    @PostMapping("/place")
    public Result searchByPlace(@RequestBody JSONObject jsonObject) {

        LOG.info("SearchController::searchByPlace jsonObject = [{}]", jsonObject);

        return searchService.searchByPlace(jsonObject);
    }

    @SystemLog(description = "联想搜索", type = LogType.OPERATION)
    @ApiOperation(value = "联想suggest", notes = "搜索联想suggest自动补全")
    @GetMapping("/suggest")
    public Result suggest(@RequestParam String keyword) {

        LOG.info("SearchController::Suggest keyword = [{}]", keyword);

        // 判空
        if (keyword == null || keyword.length() <= 0) {
            return ResultUtil.error(ResultCode.PARAM_IS_NULL);
        }

        return searchService.getSuggestSearch(keyword);
    }
}
