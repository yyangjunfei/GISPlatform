package cc.wanshan.gis.controller.search;

import cc.wanshan.gis.service.search.ESCrudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/***
 *@program: Elasticsearch
 *@description: es增删查改
 *@author: Yang
 *@create: 2019-05-31 13:35
 */

@Api(value = "ESCrudController", tags = "ES搜索接口")
@RestController
@RequestMapping("/rest/search/es")
public class ESCrudController {

    private static Logger LOG = LoggerFactory.getLogger(ESCrudController.class);

    @Autowired
    private ESCrudService esCrudService;

    /****
     * 按照ID 查询
     * @param id
     * @return ResponseEntity
     */
    @ApiOperation(value = "ID搜索查询", notes = "ID搜索查询")
    @GetMapping("/findDataByID")
    public ResponseEntity searchById(@RequestParam("id") String id) {

        //返回查询到的数据
        return esCrudService.searchById(id);
    }

    /***
     *
     * 复合查询接口
     * @param id
     * @param provinces_name
     * @param city_name
     * @param area_name
     * @param first_name
     * @param second_name
     * @param baidu_first_name
     * @param baidu_second_name
     * @param name
     * @param addr
     * @param phone
     */

    @ApiOperation(value = "根据属性字段搜索查询", notes = "根据属性字段搜索查询")
    @PostMapping("/findDataByValue")
    public ResponseEntity query(@RequestParam(value = "id", required = false) String id,
                                @RequestParam(value = "provinces_name", required = false) String provinces_name,
                                @RequestParam(value = "city_name", required = false) String city_name,
                                @RequestParam(value = "area_name", required = false) String area_name,
                                @RequestParam(value = "first_name", required = false) String first_name,
                                @RequestParam(value = "second_name", required = false) String second_name,
                                @RequestParam(value = "baidu_first_name", required = false) String baidu_first_name,
                                @RequestParam(value = "baidu_second_name", required = false) String baidu_second_name,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "addr", required = false) String addr,
                                @RequestParam(value = "phone", required = false) String phone) {

        return esCrudService.query(id, provinces_name, city_name, area_name, first_name, second_name, baidu_first_name, baidu_second_name, name, addr, phone);
    }

    /****
     * 搜索查询省数据
     * @param inputValue
     * @return
     */

    @ApiOperation(value = "搜索查询省数据", notes = "搜索查询省数据")
    @PostMapping("/queryDataByInputValue")
    public ResponseEntity queryDataByInputValue(@RequestParam(value = "inputValue", required = false) String inputValue) {

        return esCrudService.queryDataByInputValue(inputValue);
    }

    /****
     * 搜索查询市经纬度
     * @param inputCityName
     * @return
     */

    @ApiOperation(value = "搜索查询市经纬度", notes = "搜索查询市经纬度")
    @PostMapping("/queryCityCoordinatesByInputValue")
    public String queryCityCoordinatesByInputValue(@RequestParam(value = "inputCityName", required = false) String inputCityName) {

        return esCrudService.queryCityCoordinatesByInputValue(inputCityName);
    }


    /***
     * POI按照省市县村分组聚和查询（全国）
     */
    @ApiOperation(value = "POI按照省市县村分组聚和查询(全国)", notes = "POI按照省市县村分组聚和查询(全国)")
    @PostMapping("/findDataByPoi")
    public ResponseEntity queryPoiValue(@RequestParam(value = "poiValue", required = false) String poiValue) {

        return esCrudService.queryPoiValue(poiValue);
    }

    /***
     * POI按照省分组聚和查询（省级）
     */

    @ApiOperation(value = "POI按照省分组聚和查询(省级)", notes = "POI按照省分组聚和查询(省级)")
    @PostMapping("/findProvinceDataByPoiValue")
    @ApiImplicitParam(name = "provinceListName", value = "provinceListName", allowMultiple = true, dataType = "String", paramType = "query")
    public ResponseEntity findProvinceDataByPoiValue(@RequestParam(value = "poiValue", required = false) String poiValue, @RequestParam List<String> provinceListName) {

        return esCrudService.findProvinceDataByPoiValue(poiValue, provinceListName);
    }

    /***
     * POI按照市分组聚和查询（市级）
     */
    @ApiOperation(value = "POI按照市分组聚和查询(市级)", notes = "POI按照市分组聚和查询(市级)")
    @PostMapping("/findCityDataByPoiValue")
    @ApiImplicitParam(name = "cityListName", value = "cityListName", allowMultiple = true, dataType = "String", paramType = "query")
    public ResponseEntity findCityDataByPoiValue(@RequestParam(value = "poiValue", required = false) String poiValue, @RequestParam List<String> cityListName) {

        return esCrudService.findCityDataByPoiValue(poiValue, cityListName);

    }

    /***
     * POI按照县/区分组聚和查询（县/区级）
     */
    @ApiOperation(value = "POI按照县/区分组聚和查询（县/区级）", notes = "POI按照县/区分组聚和查询（县/区级）")
    @PostMapping("/findCountyDataByPoiValue")
    @ApiImplicitParam(name = "countyListName", value = "countyListName", allowMultiple = true, dataType = "String", paramType = "query")
    public ResponseEntity findCountyDataByPoiValue(@RequestParam(value = "poiValue", required = false) String poiValue, @RequestParam List<String> countyListName) {

        return esCrudService.findCountyDataByPoiValue(poiValue, countyListName);
    }

    /***
     * POI按照县级以下查询数据(县级以下)
     * @param poiValue
     * @param townListName
     * @return
     */

    @ApiOperation(value = "POI按照县级以下查询数据(县级以下)", notes = "POI按照县级以下查询数据(县级以下)")
    @PostMapping("/findTownDataByPoiValue")
    @ApiImplicitParam(name = "townListName", value = "townListName", allowMultiple = true, dataType = "String", paramType = "query")
    public ResponseEntity findTownDataByPoiValue(@RequestParam(value = "poiValue", required = false) String poiValue, @RequestParam List<String> townListName) {

        return esCrudService.findTownDataByPoiValue(poiValue, townListName);
    }


    /**
     * 按id删除数据
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID删除数据", notes = "根据ID删除数据")
    @DeleteMapping("deleteDataByID")
    public ResponseEntity delete(@RequestParam(value = "id") String id) {

        return esCrudService.delete(id);
    }

    /**
     * 删除elasticsearch索引库
     *
     * @return
     */

    @ApiOperation(value = "删除elasticsearch索引库", notes = "删除elasticsearch索引库")
    @DeleteMapping("deleteElasticsearchIndex")
    public ResponseEntity deleteElasticsearchIndex(@RequestParam(value = "indexName") String indexName) {

        return esCrudService.deleteElasticsearchIndex(indexName);
    }

    /***
     * 导入postgis 数据库到Elasticsearch
     * @return ResponseEntity
     */

    @ApiOperation(value = "导入postGis数据到ES", notes = "导入postGis数据到ES")
    @GetMapping("postGisDb2es")
    public ResponseEntity postGisDb2es(@RequestParam(value = "dbURL") String dbURL, @RequestParam(value = "dbUserName") String dbUserName, @RequestParam(value = "dbPassword") String dbPassword, @RequestParam(value = "driverClassName") String driverClassName, @RequestParam(value = "sql") String sql, @RequestParam(value = "esindexName") String esindexName, @RequestParam(value = "esTypeName") String esTypeName) {

        return esCrudService.postGisDb2es(dbURL, dbUserName, dbPassword, driverClassName, sql, esindexName, esTypeName);
    }
}
