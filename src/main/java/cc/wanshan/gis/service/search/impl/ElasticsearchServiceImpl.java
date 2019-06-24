package cc.wanshan.gis.service.search.impl;

import cc.wanshan.gis.common.constants.Constant;
import cc.wanshan.gis.entity.search.Poi;
import cc.wanshan.gis.entity.search.Region;
import cc.wanshan.gis.entity.search.RegionInput;
import cc.wanshan.gis.entity.search.RegionOutput;
import cc.wanshan.gis.service.search.ElasticsearchService;
import cc.wanshan.gis.utils.importDB2Es;
import com.alibaba.fastjson.JSON;
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
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private static Logger LOG = LoggerFactory.getLogger(ElasticsearchServiceImpl.class);

    @Autowired
    private TransportClient client;

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

    @Override
    public ResponseEntity delete(String id) {
        DeleteResponse response = client.prepareDelete("map_data", "Feature", id).get();
        return new ResponseEntity(response.getResult(), HttpStatus.OK);
    }

    @Override
    public List<RegionOutput> findCityByKeyword(String keyword, List<RegionInput> regionInputList) {

        LOG.info("ElasticsearchServiceImpl::findCityByKeyword keyword = [{}],regionInputList = [{}]", keyword, regionInputList);

        AggregationBuilder termsBuilder = AggregationBuilders.terms("by_province").field("properties.province.keyword");
        AggregationBuilder cityTermsBuilder = AggregationBuilders.terms("by_city").field("properties.city.keyword");
        termsBuilder.subAggregation(cityTermsBuilder);

        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (keyword != null) {
            boolQuery
                    .should(QueryBuilders.matchPhraseQuery("properties.first_class", keyword))
                    .should(QueryBuilders.matchPhraseQuery("properties.second_class", keyword))
                    .should(QueryBuilders.matchPhraseQuery("properties.third_class", keyword))
                    .should(QueryBuilders.matchPhraseQuery("properties.name", keyword))
                    .minimumShouldMatch(1);
        }

        // 组装查询请求
        SearchRequestBuilder requestBuilder = client.prepareSearch(Constant.INDEX_ES_POI)
                .setTypes(Constant.TYPE_ES_POI)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(334)
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

                            // 通过关键字和区域名称获取POI匹配最高的数据
                            Poi poi = findFirstPoi(keyword, regionInput.getName());

                            LOG.info("regionOutput name = [{}],count = [{}]", bu.getKeyAsString(), bu.getDocCount());
                            RegionOutput regionOutput = RegionOutput.builder().name(bu.getKeyAsString()).count(bu.getDocCount()).type(Constant.SEARCH_REGION_TERMS).centroid(poi.getGeometry()).build();
                            regionOutputList.add(regionOutput);
                        }
                    }
                }
            }
        }
        return regionOutputList;
    }

    /**
     * 通过关键字和区域名称获取POI匹配最高的一个数据
     *
     * @param keyword    关键字
     * @param regionName 区域名称
     * @return
     */
    public Poi findFirstPoi(String keyword, String regionName) {

        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开
        boolQuery
                .should(QueryBuilders.matchPhraseQuery("properties.first_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.second_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.third_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.name", keyword).boost(3))
                .should(QueryBuilders.prefixQuery("properties.name", keyword).boost(4))
                .should(QueryBuilders.termQuery("properties.name", keyword).boost(5))
                .minimumShouldMatch(1)
                .must(QueryBuilders.matchQuery("properties.city.keyword", regionName));

        // 组装查询请求
        SearchRequestBuilder requestBuilder = client.prepareSearch(Constant.INDEX_ES_POI)
                .setTypes(Constant.TYPE_ES_POI)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(2);

        // 发送查询请求
        SearchResponse response = requestBuilder.get();
        Poi poi = null;
        for (SearchHit searchHitFields : response.getHits()) {
            poi = JSON.parseObject(searchHitFields.getSourceAsString(), Poi.class);
            break;
        }
        return poi;
    }

    @Override
    public List<RegionOutput> findTownByKeyword(String keyword, List<RegionInput> regionInputList) {

        LOG.info("ElasticsearchServiceImpl::findTownByKeyword keyword = [{}],regionInputList = [{}]", keyword, regionInputList);

        // 组装查询到的数据集
        List<RegionOutput> regionOutputList = Lists.newArrayList();

        //获取传入的区县名的POI数据
        for (RegionInput regionInput : regionInputList) {

            // 组装查询条件
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

            //第一个参数是查询的值，后面的参数是字段名，可以跟多个字段，用逗号隔开
            boolQuery
                    .should(QueryBuilders.termQuery("properties.name", keyword).boost(10f))
                    .should(QueryBuilders.prefixQuery("properties.name", keyword).boost(4f))
                    .should(QueryBuilders.matchPhraseQuery("properties.name", keyword).boost(3f))
                    .should(QueryBuilders.matchPhraseQuery("properties.first_class", keyword))
                    .should(QueryBuilders.matchPhraseQuery("properties.second_class", keyword))
                    .should(QueryBuilders.matchPhraseQuery("properties.third_class", keyword))
                    .minimumShouldMatch(1)
                    .must(QueryBuilders.matchQuery("properties.county.keyword", regionInput.getName()));

            //增加分词筛选
            List<String> termList = listTerms(keyword, Constant.INDEX_ES_POI, Constant.ik_smart);
            int minimumShouldMatch = 1;
            if (termList.size() > 1) {
                minimumShouldMatch = 2;
            }

            for (String term : termList) {
                boolQuery
                        .should(QueryBuilders.matchPhraseQuery("properties.name", term)).boost(10f)
                        .should(QueryBuilders.matchPhraseQuery("properties.first_class", term))
                        .should(QueryBuilders.matchPhraseQuery("properties.second_class", term))
                        .should(QueryBuilders.matchPhraseQuery("properties.third_class", term))
                        .minimumShouldMatch(minimumShouldMatch);
            }

            // 组装查询请求
            SearchRequestBuilder requestBuilder = client.prepareSearch(Constant.INDEX_ES_POI)
                    .setTypes(Constant.TYPE_ES_POI)
                    .setSearchType(SearchType.QUERY_THEN_FETCH)
                    .setQuery(boolQuery)
                    .setFrom(0)
                    .setSize(1000);

            // 发送查询请求
            SearchResponse response = requestBuilder.get();
            RegionOutput regionOutput = RegionOutput.builder().name(regionInput.getName()).type(Constant.SEARCH_EXACT_POI).build();
            List<Poi> poiList = Lists.newArrayList();
            for (SearchHit searchHitFields : response.getHits()) {
                poiList.add(JSON.parseObject(searchHitFields.getSourceAsString(), Poi.class));
            }
            regionOutput.setPoiList(poiList);
            regionOutputList.add(regionOutput);
        }

        return regionOutputList;
    }

    @Override
    public List<RegionOutput> findRegionByKeyword(String keyword) {

        LOG.info("ElasticsearchServiceImpl::findRegionByKeyword keyword = [{}]", keyword);

        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery
                .should(QueryBuilders.termQuery("properties.name", keyword)).boost(10f)
                .should(QueryBuilders.prefixQuery("properties.name", keyword)).boost(9f)
                .should(QueryBuilders.matchPhraseQuery("properties.name", keyword)).boost(4f)
                .minimumShouldMatch(1);

        //增加分词筛选
        List<String> termList = listTerms(keyword, Constant.INDEX_ES_REGION, Constant.ik_smart);
        for (String term : termList) {
            boolQuery.should(QueryBuilders.matchPhraseQuery("properties.name", term));
        }

        // 组装查询请求
        SearchRequestBuilder requestBuilder = client.prepareSearch(Constant.INDEX_ES_REGION)
                .setTypes(Constant.TYPE_ES_REGION)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(10);

        // 发送查询请求
        SearchResponse response = requestBuilder.get();
        List<RegionOutput> regionOutputList = Lists.newArrayList();
        for (SearchHit searchHitFields : response.getHits()) {

            Region region = JSON.parseObject(searchHitFields.getSourceAsString(), Region.class);
//            region.setGeometry(null);
            RegionOutput regionOutput = RegionOutput.builder()
                    .name(region.getProperties().getName())
                    .type(Constant.SEARCH_REGION)
                    .geometry(region.getGeometry())
                    .centroid(region.getProperties().getRectangle())
                    .build();
            regionOutputList.add(regionOutput);
        }

        return regionOutputList;
    }


    /**
     * 对关键字分词处理
     *
     * @param keyword  关键字
     * @param index    es搜索索引
     * @param analyzer 分词类型
     * @return
     */
    public List<String> listTerms(String keyword, String index, String analyzer) {

        //ik类型 ik_max_word ik_smart
        AnalyzeRequest analyzeRequest = new AnalyzeRequest(index)
                .text(keyword)
                .analyzer(analyzer);
        List<AnalyzeResponse.AnalyzeToken> tokens = client.admin().indices()
                .analyze(analyzeRequest)
                .actionGet()
                .getTokens();

        List<String> termList = Lists.newArrayList();

        for (AnalyzeResponse.AnalyzeToken token : tokens) {
            termList.add(token.getTerm());
        }
        return termList;
    }

    @Override
    public List<RegionOutput> findPoiByKeyword(String keyword) {

        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery
                .should(QueryBuilders.termQuery("properties.name", keyword)).boost(10f)
                .should(QueryBuilders.prefixQuery("properties.name.keyword", keyword)).boost(9f)
                .should(QueryBuilders.matchPhraseQuery("properties.name", keyword)).boost(8f)
                .should(QueryBuilders.matchPhraseQuery("properties.first_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.second_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.third_class", keyword))
                .minimumShouldMatch(1);
        //增加分词筛选
        List<String> termList = listTerms(keyword, Constant.INDEX_ES_REGION, Constant.ik_smart);
        for (String term : termList) {
            boolQuery
                    .should(QueryBuilders.matchPhraseQuery("properties.name", term)).boost(10f)
                    .should(QueryBuilders.matchPhraseQuery("properties.first_class", term))
                    .should(QueryBuilders.matchPhraseQuery("properties.second_class", term))
                    .should(QueryBuilders.matchPhraseQuery("properties.third_class", term))
                    .minimumShouldMatch(2);
        }
        int termSize = termList.size();
        if (termList.size() > 1) {
            boolQuery
                    .should(QueryBuilders.matchPhraseQuery("properties.name", termList.get(termSize - 2))).boost(4f)
                    .must(QueryBuilders.matchPhraseQuery("properties.name", termList.get(termSize - 1)));
        }

        // 组装查询请求
        SearchRequestBuilder requestBuilder = client.prepareSearch(Constant.INDEX_ES_POI)
                .setTypes(Constant.TYPE_ES_POI)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(500);

        // 发送查询请求
        SearchResponse response = requestBuilder.get();

        List<RegionOutput> regionOutputList = Lists.newArrayList();
        List<Poi> poiList = Lists.newArrayList();

        for (SearchHit searchHitFields : response.getHits()) {
            poiList.add(JSON.parseObject(searchHitFields.getSourceAsString(), Poi.class));
        }

        RegionOutput regionOutput = RegionOutput.builder()
                .type(Constant.SEARCH_VAGUE_POI)
                .poiList(poiList)
                .build();
        regionOutputList.add(regionOutput);
        return regionOutputList;
    }

    @Override
    public List<RegionOutput> findByKeyword(String keyword) {

        List<RegionOutput> regionOutputList = Lists.newArrayList();

        regionOutputList.addAll(findRegionByKeyword(keyword));
        regionOutputList.addAll(findPoiByKeyword(keyword));

        return regionOutputList;
    }

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

    @Override
    public List<String> getSuggestSearch(String keyword) {
        //field的名字,前缀(输入的text),以及大小size
        CompletionSuggestionBuilder suggestionBuilderDistrict = SuggestBuilders.completionSuggestion("keyword")
                .prefix(keyword).size(3);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("properties.name", suggestionBuilderDistrict);//添加suggest

        //设置查询builder的index,type,以及建议
        SearchRequestBuilder requestBuilder = client.prepareSearch("map_data").setTypes("Feature").suggest(suggestBuilder);
        LOG.info(requestBuilder.toString());

        SearchResponse response = requestBuilder.get();
        Suggest suggest = response.getSuggest();//suggest实体

        Set<String> suggestSet = new HashSet<>();//set
        int maxSuggest = 0;
        if (suggest != null) {
            Suggest.Suggestion result = suggest.getSuggestion("properties.name");//获取suggest,name任意string
            for (Object term : result.getEntries()) {

                if (term instanceof CompletionSuggestion.Entry) {
                    CompletionSuggestion.Entry item = (CompletionSuggestion.Entry) term;
                    if (!item.getOptions().isEmpty()) {
                        //若item的option不为空,循环遍历
                        for (CompletionSuggestion.Entry.Option option : item.getOptions()) {
                            String tip = option.getText().toString();
                            if (!suggestSet.contains(tip)) {
                                suggestSet.add(tip);
                                ++maxSuggest;
                            }
                        }
                    }
                }
                if (maxSuggest >= 5) {
                    break;
                }
            }
        }

        List<String> suggests = Arrays.asList(suggestSet.toArray(new String[]{}));

        return suggests;
    }
}
