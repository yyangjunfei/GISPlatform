package cc.wanshan.gis.service.search.impl;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.search.Poi;
import cc.wanshan.gis.entity.search.RegionInput;
import cc.wanshan.gis.entity.search.RegionOutput;
import cc.wanshan.gis.entity.sercher.City;
import cc.wanshan.gis.entity.sercher.JsonRootBean;
import cc.wanshan.gis.entity.sercher.Province;
import cc.wanshan.gis.service.search.ESCrudService;
import cc.wanshan.gis.utils.ResultUtil;
import cc.wanshan.gis.utils.importDB2Es;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ESCrudServiceImpl implements ESCrudService {

    private static Logger LOG = LoggerFactory.getLogger(ESCrudServiceImpl.class);

    @Autowired
    private TransportClient client;

    /****
     * 按照ID 查询
     * @param id
     * @return ResponseEntity
     */

    @Override
    public ResponseEntity searchById(String id) {
        if (id.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        //通过索引 、类型、id 向es进行查询数据
        GetResponse response = client.prepareGet("map_data", "Feature", id).get();
        if (!response.isExists()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        //返回查询到的数据
        return new ResponseEntity(response.getSource(), HttpStatus.OK);
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
    @Override
    public ResponseEntity query(String id, String provinces_name, String city_name, String area_name, String first_name, String second_name, String baidu_first_name, String baidu_second_name, String name, String addr, String phone) {
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

    /****
     * 搜索查询省数据
     * @param inputValue
     * @return
     */

    @Override
    public ResponseEntity queryDataByInputValue(String inputValue) {
        BoolQueryBuilder boolQuery = null;
        if (inputValue != null) {
            boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("properties.xzqmc.keyword", inputValue));
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
        List<Map<String, Object>> result = null;

        if (length != 0) {
            // 组装查询到的数据集
            result = new ArrayList<>();
            for (SearchHit searchHitFields : response.getHits()) {
                result.add(searchHitFields.getSourceAsMap());
            }

        } else {

        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /****
     * 搜索查询市经纬度
     * @param inputCityName
     * @return
     */
    @Override
    public String queryCityCoordinatesByInputValue(String inputCityName) {
        String coordinates = null;
        BoolQueryBuilder boolQuery = null;
        if (inputCityName != null) {
            boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("properties.xzqdmc.keyword", inputCityName));
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
        if (length != 0) {
            for (SearchHit searchHitFields : response.getHits()) {
                //获取城市经纬度坐标
                for (String key : searchHitFields.getSourceAsMap().keySet()) {
                    if (key.equals("geometry")) {
                        //System.out.println("Key="+key+"\tvalue="+searchHitFields.getSourceAsMap().get(key));
                        String subValue = searchHitFields.getSourceAsMap().get(key).toString().split(",")[0];
                        String subValue2 = subValue.substring(1, subValue.length()).split("=")[1];
                        //经纬度坐标
                        coordinates = subValue2.substring(1, subValue2.length() - 1);
                        //System.out.println("coordinates:"+coordinates);
                    }
                }
            }
        }
        return coordinates;
    }

    /***
     *
     * POI按照省市县村分组聚和查询（全国）
     */

    @Override
    public ResponseEntity queryPoiValue(String poiValue) {
        AggregationBuilder termsBuilder = AggregationBuilders.terms("by_provinces_name").field("properties.provinces_name.keyword");
        AggregationBuilder cityTermsBuilder = AggregationBuilders.terms("by_city_name").field("properties.city_name.keyword");
        AggregationBuilder areaBuilder = AggregationBuilders.terms("by_area_name").field("properties.area_name.keyword");
        termsBuilder.subAggregation(cityTermsBuilder.subAggregation(areaBuilder));

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

        for (Aggregation a : terms) {
            StringTerms stringTerms = (StringTerms) a;
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                System.out.println(bucket.getKeyAsString() + "   " + bucket.getDocCount());
                Aggregation aggs = bucket.getAggregations().getAsMap().get("by_city_name");
                StringTerms terms1 = (StringTerms) aggs;
                List<City> cityList = new ArrayList<>();
                for (StringTerms.Bucket bu : terms1.getBuckets()) {
                    System.out.println(bucket.getKeyAsString() + "  " + bu.getKeyAsString() + " " + bu.getDocCount());
                    cityList.add(new City(bu.getKeyAsString(), String.valueOf(bu.getDocCount())));
                }
                result.add(new JsonRootBean(new Province(bucket.getKeyAsString(), String.valueOf(bucket.getDocCount())), cityList));
            }
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /***
     * POI按照省分组聚和查询（省级）
     */

    @Override
    public ResponseEntity findProvinceDataByPoiValue(String poiValue, List<String> provinceListName) {
        AggregationBuilder termsBuilder = AggregationBuilders.terms("by_provinces_name").field("properties.provinces_name.keyword");

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
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();

        for (Aggregation a : terms) {
            StringTerms stringTerms = (StringTerms) a;
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                //获取传入的省名List的POI计数
                for (String name : provinceListName) {
                    if (name.equals(bucket.getKeyAsString())) {
                        // System.out.println(bucket.getKeyAsString()+"   "+bucket.getDocCount());
                        map.put(bucket.getKeyAsString(), bucket.getDocCount());
                    }
                }
            }
        }
        result.add(map);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /***
     * POI按照省分组聚和查询（省级）
     */

    @Override
    public List<RegionOutput> findProvinceByKeyword(String keyword, List<RegionInput> regionInputList) {

        LOG.info("ESCrudServiceImpl::findProvinceByKeyword keyword = [{}],regionInputList = [{}]", keyword, regionInputList);

        AggregationBuilder termsBuilder = AggregationBuilders.terms("by_province").field("properties.province.keyword");

        // 多个字段匹配某一个值
        QueryBuilder queryBuilder = null;

        if (keyword != null && keyword.length() > 0) {
            //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开
            queryBuilder = QueryBuilders.multiMatchQuery(keyword, "properties.first_class", "properties.second_class", "properties.third_class", "properties.name");
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
        Aggregations aggregations = response.getAggregations();

        // 组装查询到的数据集
        List<RegionOutput> regionOutputList = Lists.newArrayList();

        for (Aggregation aggregation : aggregations) {
            StringTerms stringTerms = (StringTerms) aggregation;
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                //获取传入的省名List的POI计数
                for (RegionInput regionInput : regionInputList) {
                    //判断名称是否相等
                    if (bucket.getKeyAsString().equals(regionInput.getName())) {

                        LOG.info("regionOutput name = [{}],count = [{}]", bucket.getKeyAsString(), bucket.getDocCount());

                        RegionOutput regionOutput = RegionOutput.builder().name(bucket.getKeyAsString()).count(bucket.getDocCount()).centroid(regionInput.getCentroid()).build();
                        regionOutputList.add(regionOutput);

                    }
                }
            }
        }
        return regionOutputList;
    }

    @Override
    public List<RegionOutput> findCityByKeyword(String keyword, List<RegionInput> regionInputList) {

        LOG.info("ESCrudServiceImpl::findProvinceByKeyword keyword = [{}],keyword = [{}]", keyword, regionInputList);

        AggregationBuilder termsBuilder = AggregationBuilders.terms("by_province").field("properties.province.keyword");
        AggregationBuilder cityTermsBuilder = AggregationBuilders.terms("by_city").field("properties.city.keyword");
        termsBuilder.subAggregation(cityTermsBuilder);

        // 多个字段匹配某一个值
        QueryBuilder queryBuilder = null;

        if (keyword != null) {
            //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开
            queryBuilder = QueryBuilders.multiMatchQuery(keyword, "properties.first_class", "properties.second_class", "properties.third_class", "properties.name");
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
        Aggregations aggregations = response.getAggregations();

        // 组装查询到的数据集
        List<RegionOutput> regionOutputList = Lists.newArrayList();

        for (Aggregation aggregation : aggregations) {
            StringTerms stringTerms = (StringTerms) aggregation;
            //获取省
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                Aggregation aggs = bucket.getAggregations().getAsMap().get("by_city");
                StringTerms terms1 = (StringTerms) aggs;
                //获取市
                for (StringTerms.Bucket bu : terms1.getBuckets()) {
                    //获取传入的市名的POI计数
                    for (RegionInput regionInput : regionInputList) {
                        //判断名称是否相等
                        if (bu.getKeyAsString().equals(regionInput.getName())) {

                            LOG.info("regionOutput name = [{}],count = [{}]", bu.getKeyAsString(), bu.getDocCount());
                            RegionOutput regionOutput = RegionOutput.builder().name(bu.getKeyAsString()).count(bu.getDocCount()).centroid(regionInput.getCentroid()).build();
                            regionOutputList.add(regionOutput);
                        }
                    }
                }
            }
        }
        return regionOutputList;
    }

    @Override
    public List<RegionOutput> findTownByKeyword(String keyword, List<RegionInput> regionInputList) {

        // 组装查询到的数据集
        List<RegionOutput> regionOutputList = Lists.newArrayList();

        //获取传入的区县名的POI数据
        for (RegionInput regionInput : regionInputList) {

            // 组装查询条件
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

            //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开
            boolQuery.must(QueryBuilders.multiMatchQuery(keyword, "properties.first_class", "properties.second_class", "properties.third_class", "properties.name"))
                    .must(QueryBuilders.matchQuery("properties.county.keyword", regionInput.getName()));

            // 组装查询请求
            SearchRequestBuilder requestBuilder = client.prepareSearch("map_data")
                    .setTypes("Feature")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(boolQuery)
                    .setFrom(0)
                    .setSize(20);

            // 发送查询请求
            SearchResponse response = requestBuilder.get();
            RegionOutput regionOutput = RegionOutput.builder().name(regionInput.getName()).build();
            List<Poi> poiList = Lists.newArrayList();
            for (SearchHit searchHitFields : response.getHits()) {
                Poi poi = JSON.parseObject(searchHitFields.getSourceAsString(), Poi.class);
                poiList.add(poi);
            }
            regionOutput.setPoiList(poiList);
            regionOutputList.add(regionOutput);

        }
        return regionOutputList;
    }

    /***
     * POI按照市分组聚和查询（市级）
     */

    @Override
    public ResponseEntity findCityDataByPoiValue(String poiValue, List<String> cityListName) {
        AggregationBuilder termsBuilder = AggregationBuilders.terms("by_province").field("properties.province.keyword");
        AggregationBuilder cityTermsBuilder = AggregationBuilders.terms("by_city").field("properties.city.keyword");
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
        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> map = new HashMap<String, Object>();
        for (Aggregation a : terms) {
            StringTerms stringTerms = (StringTerms) a;
            //获取省
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                Aggregation aggs = bucket.getAggregations().getAsMap().get("by_city_name");
                StringTerms terms1 = (StringTerms) aggs;
                //获取市
                for (StringTerms.Bucket bu : terms1.getBuckets()) {
                    //获取传入的市名List的POI计数
                    for (String name : cityListName) {
                        if (name.equals(bu.getKeyAsString())) {
                            //查询城市经纬度
                            //String coordinates= queryCityCoordinatesByInputValue(name);
                            //  System.out.println(bu.getKeyAsString()+"   "+bu.getDocCount()+"  "+coordinates);
                            map.put(bu.getKeyAsString(), bu.getDocCount());
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

    @Override
    public ResponseEntity findCountyDataByPoiValue(String poiValue, List<String> countyListName) {

        AggregationBuilder termsBuilder = AggregationBuilders.terms("by_provinces_name").field("properties.provinces_name.keyword");
        AggregationBuilder cityTermsBuilder = AggregationBuilders.terms("by_city_name").field("properties.city_name.keyword");
        AggregationBuilder areaBuilder = AggregationBuilders.terms("by_area_name").field("properties.area_name.keyword");
        termsBuilder.subAggregation(cityTermsBuilder.subAggregation(areaBuilder));

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
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();

        for (Aggregation a : terms) {
            StringTerms stringTerms = (StringTerms) a;
            //获取省
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                Aggregation aggs = bucket.getAggregations().getAsMap().get("by_city_name");
                StringTerms terms1 = (StringTerms) aggs;
                //获取市
                for (StringTerms.Bucket buc : terms1.getBuckets()) {
                    Aggregation agg = buc.getAggregations().getAsMap().get("by_area_name");
                    StringTerms terms2 = (StringTerms) agg;
                    //获取县
                    for (StringTerms.Bucket bu : terms2.getBuckets()) {
                        //获取传入的县名List的POI计数
                        for (String name : countyListName) {
                            if (name.equals(bu.getKeyAsString())) {
                                // System.out.println(bucket.getKeyAsString()+"   "+bucket.getDocCount());
                                map.put(bu.getKeyAsString(), bu.getDocCount());
                            }
                        }
                    }
                }
            }
        }
        result.add(map);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /***
     * POI按照县级以下查询数据(县级以下)
     * @param poiValue
     * @param townListName
     * @return
     */

    @Override
    public ResponseEntity findTownDataByPoiValue(String poiValue, List<String> townListName) {
        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 组装查询到的数据集
        List<Map<String, Object>> result = new ArrayList<>();

        //获取传入的县名List的POI计数
        for (String townName : townListName) {
            //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开
            boolQuery.must(QueryBuilders.multiMatchQuery(poiValue, "properties.first_name", "properties.second_name", "properties.baidu_first_name", "properties.baidu_second_name"))
                    .must(QueryBuilders.matchQuery("properties.name.keyword", townName));

            // 组装查询请求
            SearchRequestBuilder requestBuilder = client.prepareSearch("map_data")
                    .setTypes("Feature")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(boolQuery)
                    .setFrom(0)
                    .setSize(200);

            // 发送查询请求
            SearchResponse response = requestBuilder.get();

            for (SearchHit searchHitFields : response.getHits()) {
                LOG.info("=====" + searchHitFields.getSourceAsMap());

                result.add(searchHitFields.getSourceAsMap());
            }
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 按id删除数据
     *
     * @param id
     * @return
     */

    @Override
    public ResponseEntity delete(String id) {
        DeleteResponse response = client.prepareDelete("map_data", "Feature", id).get();
        return new ResponseEntity(response.getResult(), HttpStatus.OK);
    }

    @Override
    public Result searchAreaGeoFromES(String name) {

        AnalyzeRequest analyzeRequest = new AnalyzeRequest("region_data").text(name).analyzer("ik_smart");

        List<Object> objectList = Lists.newArrayList();

        List<AnalyzeResponse.AnalyzeToken> tokens = client.admin().indices().analyze(analyzeRequest).actionGet().getTokens();

        for (AnalyzeResponse.AnalyzeToken token : tokens) {
            objectList.add(token.getTerm());
        }
        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开

        boolQuery.must(QueryBuilders.matchQuery("properties.name.keyword", name).analyzer("ik_smart").minimumShouldMatch("75%")).minimumShouldMatch(3);
        QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder("region_data");
        queryBuilder.analyzer("ik_smart");
        queryBuilder.field("properties.name.keyword").field(name);

        // 组装查询请求
        SearchRequestBuilder requestBuilder = client.prepareSearch("region_data")
                .setTypes("Feature")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(5);

        List<JSONObject> list = Lists.newArrayList();

        objectList.add(tokens);

        // 发送查询请求
        SearchResponse response = requestBuilder.get();
        for (SearchHit searchHitFields : response.getHits()) {

            JSONObject jsonObject = JSON.parseObject(searchHitFields.getSourceAsString());
            String properties = jsonObject.getString("properties");

            JSONObject jsonObject1 = JSON.parseObject(properties);
            String name1 = jsonObject1.getString("name");
//            list.add(jsonObject);
//            list.add(JSON.parseObject(properties));
//            objectList.add(name1);

        }


        objectList.add(list);
        return ResultUtil.success(objectList);
    }

    /**
     * 删除elasticsearch索引库
     *
     * @return
     */

    @Override
    public ResponseEntity deleteElasticsearchIndex(String indexName) {
        ResponseEntity responseEntity = null;
        //判断索引是否存在
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);
        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();

        if (!inExistsResponse.isExists()) {
            LOG.info(indexName + " not exists");
        } else {
            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName).execute().actionGet();
            // isAcknowledged()方法判断删除是否成功
            if (dResponse.isAcknowledged()) {
                responseEntity = new ResponseEntity("delete index " + indexName + "  successfully!", HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity("Fail to delete index " + indexName, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return responseEntity;
    }

    /***
     * 导入postgis 数据库到Elasticsearch
     * @return ResponseEntity
     */

    @Override
    public ResponseEntity postGisDb2es(String dbURL, String dbUserName, String dbPassword, String
            driverClassName, String sql, String esindexName, String esTypeName) {
        ResponseEntity responseEntity = null;
        importDB2Es.transportClient = client;
        long count;
        try {
            count = importDB2Es.importData(dbURL, dbUserName, dbPassword, driverClassName, sql, esindexName, esTypeName);
            if (count == 0) {
                responseEntity = new ResponseEntity("导入postGisDb到Elasticsearch成功", HttpStatus.OK);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseEntity;
    }

}
