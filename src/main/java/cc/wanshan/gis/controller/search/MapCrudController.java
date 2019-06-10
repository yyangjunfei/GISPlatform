package cc.wanshan.gis.controller.search;
import cc.wanshan.gis.entity.sercher.City;
import cc.wanshan.gis.entity.sercher.JsonRootBean;
import cc.wanshan.gis.entity.sercher.Province;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/***
 *@program: es-demo
 *@description: es增删查改接口demo
 *@author: Yang
 *@create: 2019-05-31 13:35
 */
@Api(value = "SercherController", tags = "搜索接口")
@RestController
@RequestMapping("/rest/sercher")
public class MapCrudController {
    private static Logger LOG = LoggerFactory.getLogger(MapCrudController.class);
    @Autowired
    private TransportClient client;
    private AggregationBuilder countBuilder;

    /***
 * 按照ID 查询
 * @param id
 * @return
 */
@ApiOperation(value = "ID搜索查询", notes = "ID搜索查询")
@GetMapping("/findDataByID")
public ResponseEntity searchById(@RequestParam("id") String id){

    if (id.isEmpty()){
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    //通过索引 、类型、id 向es进行查询数据
    GetResponse response = client.prepareGet("map_data","Feature",id).get();

    if (!response.isExists()){
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    //返回查询到的数据
    return new ResponseEntity(response.getSource(),HttpStatus.OK);
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
                            @RequestParam(value = "phone", required = false) String phone){
    // 组装查询条件
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    if (id != null) {
        boolQuery.must(QueryBuilders.matchQuery("properties.id", id));
    }
    if (provinces_name != null) {
        boolQuery.must(QueryBuilders.matchQuery("properties.provinces_name", provinces_name));
    }
    if (city_name != null) {
        boolQuery.must(QueryBuilders.matchQuery("properties.city_name", city_name));
    }
    if (area_name != null) {
        boolQuery.must(QueryBuilders.matchQuery("properties.area_name", area_name));
    }
    if (first_name != null) {
        boolQuery.must(QueryBuilders.matchQuery("properties.first_name", first_name));
    }
    if (second_name != null) {
        boolQuery.must(QueryBuilders.matchQuery("properties.second_name", second_name));
    }
    if (baidu_first_name != null) {
        boolQuery.must(QueryBuilders.matchQuery("properties.baidu_first_name", baidu_first_name));
    }
    if (baidu_second_name != null) {
        boolQuery.must(QueryBuilders.matchQuery("properties.baidu_second_name", baidu_second_name));
    }
    if (name != null) {
        boolQuery.must(QueryBuilders.matchQuery("properties.name", name));
    }
    if (addr != null) {
        boolQuery.must(QueryBuilders.matchQuery("properties.addr", addr));
    }
    if (phone != null) {
        boolQuery.must(QueryBuilders.matchQuery("properties.phone", phone));
    }

    // 以id作为条件范围
/*    RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("id").from(gtWordCount);
    if (ltWordCount != null && ltWordCount > 0) {
        rangeQuery.to(ltWordCount);
    }
    boolQuery.filter(rangeQuery);*/

    // 组装查询请求
    SearchRequestBuilder requestBuilder = client.prepareSearch("map_data")
            .setTypes("Feature")
            .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
            .setQuery(boolQuery)
            .setFrom(0)
           .setSize(100);

    // 发送查询请求
    SearchResponse response = requestBuilder.get();

    // 组装查询到的数据集
    List<Map<String, Object>> result = new ArrayList<>();
    for (SearchHit searchHitFields : response.getHits()) {
        result.add(searchHitFields.getSourceAsMap());
    }

    return new ResponseEntity(result, HttpStatus.OK);
}

@ApiOperation(value = "搜索查询省数据", notes = "搜索查询省数据")
@PostMapping("/queryDataByInputValue")
public ResponseEntity queryDataByInputValue(@RequestParam(value = "inputValue", required = false) String inputValue){

    BoolQueryBuilder boolQuery=null;
    if(inputValue !=null) {
        boolQuery= QueryBuilders.boolQuery().must(QueryBuilders.termQuery("properties.xzqmc.keyword",inputValue));
    }
    // 组装查询请求
    SearchRequestBuilder requestBuilder = client.prepareSearch("provincial_data")
            .setTypes("Feature")
            .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
            .setQuery(boolQuery)
            .setFrom(0)
            .setSize(100);

    // 发送查询请求
    SearchResponse response = requestBuilder.get();

    long length = response.getHits().totalHits;

    // 组装查询到的数据集
    List<Map<String, Object>> result=null;

    if (length!=0){
        // 组装查询到的数据集
         result = new ArrayList<>();
        for (SearchHit searchHitFields : response.getHits()) {
            result.add(searchHitFields.getSourceAsMap());
        }

    }else {

    }
    return new ResponseEntity(result, HttpStatus.OK);
}

    @ApiOperation(value = "搜索查询市经纬度", notes = "搜索查询市经纬度")
    @PostMapping("/queryCityCoordinatesByInputValue")
    public String queryCityCoordinatesByInputValue(@RequestParam(value = "inputCityName", required = false) String inputCityName){
        String coordinates =null;
        BoolQueryBuilder boolQuery=null;
        if(inputCityName !=null) {
            boolQuery= QueryBuilders.boolQuery().must(QueryBuilders.termQuery("properties.xzqdmc.keyword",inputCityName));
        }
        // 组装查询请求
        SearchRequestBuilder requestBuilder = client.prepareSearch("map_data")
                .setTypes("Feature")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(100);

        // 发送查询请求
        SearchResponse response = requestBuilder.get();
        long length = response.getHits().totalHits;
        if (length!=0){
            for (SearchHit searchHitFields : response.getHits()) {
                //获取城市经纬度坐标
                for(String key:searchHitFields.getSourceAsMap().keySet()){
                    if (key.equals("geometry")){
                        //System.out.println("Key="+key+"\tvalue="+searchHitFields.getSourceAsMap().get(key));
                        String subValue = searchHitFields.getSourceAsMap().get(key).toString().split(",")[0];
                        String subValue2= subValue.substring(1,subValue.length()).split("=")[1];
                        //经纬度坐标
                        coordinates= subValue2.substring(1,subValue2.length()-1);
                        //System.out.println("coordinates:"+coordinates);
                    }
                }
            }
        }
        return coordinates;
    }


    /***
     * POI按照省市县分组聚和查询（全国）
     */
    @ApiOperation(value = "POI按照省市县分组聚和查询(全国)", notes = "POI按照省市县分组聚和查询(全国)")
    @PostMapping("/findDataByPoi")
    public ResponseEntity queryPoiValue(@RequestParam(value = "poiValue", required = false) String poiValue) {
        AggregationBuilder termsBuilder = AggregationBuilders.terms("by_provinces_name").field("properties.provinces_name.keyword");
        AggregationBuilder cityTermsBuilder = AggregationBuilders.terms("by_city_name").field("properties.city_name.keyword");
        termsBuilder.subAggregation(cityTermsBuilder);

        // 多个字段匹配某一个值
        QueryBuilder queryBuilder = null;

        if (poiValue != null) {
            //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开
            queryBuilder = QueryBuilders.multiMatchQuery(poiValue, "properties.first_name", "properties.second_name", "properties.baidu_first_name", "properties.baidu_second_name");
        }

        // 组装查询请求
        SearchRequestBuilder requestBuilder = client.prepareSearch("map_data")
                .setTypes("Feature")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0)
                .setSize(100)
                .addAggregation(termsBuilder);

        // 发送查询请求
        SearchResponse response = requestBuilder.get();
        Aggregations terms = response.getAggregations();

        // 组装查询到的数据集
        List<JsonRootBean> result = new ArrayList<>();

        for (Aggregation a:terms){
            StringTerms stringTerms= (StringTerms)a;
            for(StringTerms.Bucket bucket:stringTerms.getBuckets()){
                System.out.println(bucket.getKeyAsString()+"   "+bucket.getDocCount());
                Aggregation aggs = bucket.getAggregations().getAsMap().get("by_city_name");
                StringTerms terms1 = (StringTerms) aggs;
                List<City> cityList = new ArrayList<>();
                for (StringTerms.Bucket bu : terms1.getBuckets()) {
                    System.out.println(bucket.getKeyAsString() + "  " + bu.getKeyAsString() + " " + bu.getDocCount());
                    cityList.add(new City(bu.getKeyAsString(),String.valueOf(bu.getDocCount())));
                }
                result.add(new JsonRootBean(new Province(bucket.getKeyAsString(),String.valueOf(bucket.getDocCount())),cityList));
            }
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /***
     * POI按照省分组聚和查询（省级）
     */
    @ApiOperation(value = "POI按照省分组聚和查询(省级)", notes = "POI按照省分组聚和查询(省级)")
    @PostMapping("/findProvinceDataByPoiValue")
    @ApiImplicitParam(name = "provinceListName", value = "provinceListName",  allowMultiple = true, dataType = "String", paramType = "query")
    public ResponseEntity findProvinceDataByPoiValue(@RequestParam(value = "poiValue", required = false) String poiValue,@RequestParam List<String> provinceListName){
        AggregationBuilder termsBuilder = AggregationBuilders.terms("by_provinces_name").field("properties.provinces_name.keyword");

        // 多个字段匹配某一个值
        QueryBuilder queryBuilder =null;

        if(poiValue != null){
            //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开
            queryBuilder = QueryBuilders.multiMatchQuery(poiValue,"properties.first_name","properties.second_name","properties.baidu_first_name","properties.baidu_second_name");
        }

        // 组装查询请求
        SearchRequestBuilder requestBuilder = client.prepareSearch("map_data")
                .setTypes("Feature")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0)
                .setSize(100)
                .addAggregation(termsBuilder);

        // 发送查询请求
        SearchResponse response = requestBuilder.get();
        Aggregations terms= response.getAggregations();

        // 组装查询到的数据集
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();

        for (Aggregation a:terms){
            StringTerms stringTerms= (StringTerms)a;
            for(StringTerms.Bucket bucket:stringTerms.getBuckets()){
                //获取传入的省名List的POI计数
                for(String name :provinceListName){
                    if (name.equals(bucket.getKeyAsString())){
                        // System.out.println(bucket.getKeyAsString()+"   "+bucket.getDocCount());
                        map.put(bucket.getKeyAsString(),bucket.getDocCount());
                    }
                }
            }
        }
        result.add(map);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /***
     * POI按照市分组聚和查询（市级）
     */
    @ApiOperation(value = "POI按照市分组聚和查询(市级)", notes = "POI按照市分组聚和查询(市级)")
    @PostMapping("/findCityDataByPoiValue")
    @ApiImplicitParam(name = "cityListName", value = "cityListName",  allowMultiple = true, dataType = "String", paramType = "query")
    public ResponseEntity findCityDataByPoiValue(@RequestParam(value = "poiValue", required = false) String poiValue,@RequestParam List<String> cityListName){
        AggregationBuilder termsBuilder = AggregationBuilders.terms("by_provinces_name").field("properties.provinces_name.keyword");
        AggregationBuilder cityTermsBuilder = AggregationBuilders.terms("by_city_name").field("properties.city_name.keyword");
        termsBuilder.subAggregation(cityTermsBuilder);

        // 多个字段匹配某一个值
        QueryBuilder queryBuilder =null;

        if(poiValue != null){
            //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开
            queryBuilder = QueryBuilders.multiMatchQuery(poiValue,"properties.first_name","properties.second_name","properties.baidu_first_name","properties.baidu_second_name");
        }

        // 组装查询请求
        SearchRequestBuilder requestBuilder = client.prepareSearch("map_data")
                .setTypes("Feature")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0)
                .setSize(100)
                .addAggregation(termsBuilder);

        // 发送查询请求
        SearchResponse response = requestBuilder.get();
        Aggregations terms= response.getAggregations();
        // 组装查询到的数据集
        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> map = new HashMap<String, Object>();
        for (Aggregation a:terms){
            StringTerms stringTerms= (StringTerms)a;
            //获取省
            for(StringTerms.Bucket bucket:stringTerms.getBuckets()){
                Aggregation aggs = bucket.getAggregations().getAsMap().get("by_city_name");
                StringTerms terms1 = (StringTerms) aggs;
                //获取市
                for (StringTerms.Bucket bu : terms1.getBuckets()) {
                    //获取传入的市名List的POI计数
                    for(String name :cityListName){
                        if (name.equals(bu.getKeyAsString())){
                            //查询城市经纬度
                           String coordinates= queryCityCoordinatesByInputValue(name);
                          //  System.out.println(bu.getKeyAsString()+"   "+bu.getDocCount()+"  "+coordinates);
                            map.put(bu.getKeyAsString(),bu.getDocCount());
                        }
                    }
                }
            }
        }

        result.add(map);
        return new ResponseEntity(result, HttpStatus.OK);
    }


    /***
     * POI按照县/区分组聚和查询（县/区级）
     */
    @ApiOperation(value = "POI按照县/区分组聚和查询（县/区级）", notes = "POI按照县/区分组聚和查询（县/区级）")
    @PostMapping("/findCountyDataByPoiValue")
    @ApiImplicitParam(name = "countyListName", value = "countyListName",  allowMultiple = true, dataType = "String", paramType = "query")
    public ResponseEntity findCountyDataByPoiValue(@RequestParam(value = "poiValue", required = false) String poiValue,@RequestParam List<String> countyListName){
        AggregationBuilder termsBuilder = AggregationBuilders.terms("by_provinces_name").field("properties.provinces_name.keyword");
        AggregationBuilder cityTermsBuilder = AggregationBuilders.terms("by_city_name").field("properties.city_name.keyword");
        AggregationBuilder areaBuilder = AggregationBuilders.terms("by_area_name").field("properties.area_name.keyword");
        termsBuilder.subAggregation(cityTermsBuilder.subAggregation(areaBuilder));

        // 多个字段匹配某一个值
        QueryBuilder queryBuilder =null;

        if(poiValue != null){
            //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开
            queryBuilder = QueryBuilders.multiMatchQuery(poiValue,"properties.first_name","properties.second_name","properties.baidu_first_name","properties.baidu_second_name");
        }

        // 组装查询请求
        SearchRequestBuilder requestBuilder = client.prepareSearch("map_data")
                .setTypes("Feature")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0)
                .setSize(100)
                .addAggregation(termsBuilder);

        // 发送查询请求
        SearchResponse response = requestBuilder.get();
        Aggregations terms= response.getAggregations();

        // 组装查询到的数据集
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();

        for (Aggregation a:terms){
            StringTerms stringTerms= (StringTerms)a;
            //获取省
            for(StringTerms.Bucket bucket:stringTerms.getBuckets()){
                Aggregation aggs = bucket.getAggregations().getAsMap().get("by_city_name");
                StringTerms terms1 = (StringTerms) aggs;
                //获取市
                for (StringTerms.Bucket buc : terms1.getBuckets()) {
                    Aggregation agg= buc.getAggregations().getAsMap().get("by_area_name");
                    StringTerms terms2 = (StringTerms) agg;
                    //获取县
                    for (StringTerms.Bucket bu : terms2.getBuckets()){
                        //获取传入的县名List的POI计数
                        for(String name :countyListName){
                            if (name.equals(bu.getKeyAsString())){
                                // System.out.println(bucket.getKeyAsString()+"   "+bucket.getDocCount());
                                map.put(bu.getKeyAsString(),bu.getDocCount());
                            }
                        }
                    }
                }
            }
        }
        result.add(map);
        return new ResponseEntity(result, HttpStatus.OK);
    }


    /**
     * 按id删除数据
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID删除数据", notes = "根据ID删除数据")
    @DeleteMapping("deleteDataByID")
    public ResponseEntity delete(@RequestParam(value ="id") String id) {
        DeleteResponse response = client.prepareDelete("map_data", "Feature", id).get();
        return new ResponseEntity(response.getResult(), HttpStatus.OK);
    }
}
