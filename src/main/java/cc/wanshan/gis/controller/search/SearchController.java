package cc.wanshan.gis.controller.search;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.service.search.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "SearchController", tags = "搜索接口")
@RestController
@RequestMapping("/rest/search")
public class SearchController {

    private static Logger LOG = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private SearchService searchService;

    @ApiOperation(value = "根据位置查询地区名称", notes = "根据位置查询地区名称")
    @GetMapping("/location")
    public Result searchAreaName(@RequestParam double longitude, @RequestParam double latitude, @RequestParam double level) {

        LOG.info("SearchController::searchAreaName longitude = [{}],latitude = [{}],level = [{}]", longitude, latitude, level);

        return searchService.searchAreaName(longitude, latitude, level);
    }

    @ApiOperation(value = "根据名称查询地区位置", notes = "根据名称查询地区位置")
    @GetMapping("/name")
    public Result searchAreaGeo(@RequestParam String name) {

        LOG.info("SearchController::searchAreaGeo name = [{}]", name);

        return searchService.searchAreaGeo(name);
    }

}
