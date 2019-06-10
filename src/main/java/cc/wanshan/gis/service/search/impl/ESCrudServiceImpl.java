package cc.wanshan.gis.service.search.impl;
import cc.wanshan.gis.entity.sercher.City;
import cc.wanshan.gis.entity.sercher.JsonRootBean;
import cc.wanshan.gis.entity.sercher.Province;
import cc.wanshan.gis.service.search.ESCrudService;
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

    @Autowired
    private TransportClient client;

    @Override
    public ResponseEntity searchById(String id) {
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

    @Override
    public ResponseEntity queryDataByInputValue(String inputValue) {
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

    @Override
    public String queryCityCoordinatesByInputValue(String inputCityName) {
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

    @Override
    public ResponseEntity findProvinceDataByPoiValue(String poiValue, List<String> provinceListName) {
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

    @Override
    public ResponseEntity findCityDataByPoiValue(String poiValue, List<String> cityListName) {
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
                            //String coordinates= queryCityCoordinatesByInputValue(name);
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

    @Override
    public ResponseEntity findCountyDataByPoiValue(String poiValue, List<String> countyListName) {
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

    @Override
    public ResponseEntity findTownDataByPoiValue(String poiValue, List<String> townListName) {
        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 组装查询到的数据集
        List<Map<String, Object>> result = new ArrayList<>();

        //获取传入的县名List的POI计数
        for(String townName :townListName){
            //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开
            boolQuery.must(QueryBuilders.multiMatchQuery(poiValue,"properties.first_name","properties.second_name","properties.baidu_first_name","properties.baidu_second_name"))
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
                result.add(searchHitFields.getSourceAsMap());
            }
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity delete(String id) {
        DeleteResponse response = client.prepareDelete("map_data", "Feature", id).get();
        return new ResponseEntity(response.getResult(), HttpStatus.OK);
    }
}
