package cc.wanshan.gis.controller.search;
import cc.wanshan.gis.service.search.ElasticsearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "ESCrudController", tags = "ES搜索接口")
@RestController
@RequestMapping("/rest/search/es")
public class ESCrudController {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ElasticsearchService elasticsearchService;


    @ApiOperation(value = "ID搜索查询", notes = "ID搜索查询")
    @GetMapping("/findDataByID")
    public ResponseEntity searchById(@RequestParam("id") String id) {

        return elasticsearchService.searchById(id);
    }

    @ApiOperation(value = "根据ID删除数据", notes = "根据ID删除数据")
    @DeleteMapping("deleteDataByID")
    public ResponseEntity delete(@RequestParam(value = "id") String id) {

        return elasticsearchService.delete(id);
    }

    @ApiOperation(value = "删除elasticsearch索引库", notes = "删除elasticsearch索引库")
    @DeleteMapping("deleteElasticsearchIndex")
    public ResponseEntity deleteElasticsearchIndex(@RequestParam(value = "indexName") String indexName) {

        return elasticsearchService.deleteElasticsearchIndex(indexName);
    }

    @ApiOperation(value = "导入postGis数据到ES", notes = "导入postGis数据到ES")
    @GetMapping("postGisDb2es")
    public ResponseEntity postGisDb2es(@RequestParam(value = "dbURL") String dbURL, @RequestParam(value = "dbUserName") String dbUserName, @RequestParam(value = "dbPassword") String dbPassword, @RequestParam(value = "driverClassName") String driverClassName, @RequestParam(value = "sql") String sql, @RequestParam(value = "esindexName") String esindexName, @RequestParam(value = "esTypeName") String esTypeName) {

        return elasticsearchService.postGisDb2es(dbURL, dbUserName, dbPassword, driverClassName, sql, esindexName, esTypeName);
    }
}
