package cc.wanshan.gis.service.search.impl;

import cc.wanshan.gis.common.constants.Constant;
import cc.wanshan.gis.entity.search.Poi;
import cc.wanshan.gis.entity.search.Region;
import cc.wanshan.gis.entity.search.RegionOutput;
import cc.wanshan.gis.service.search.ElasticsearchService;
import cc.wanshan.gis.utils.importDB2Es;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
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
    public List<RegionOutput> findCityByKeyword(String keyword, List<String> regionList) {

        LOG.info("ElasticsearchServiceImpl::findCityByKeyword keyword = [{}],regionInputList = [{}]", keyword, regionList);

        AggregationBuilder termsBuilder = AggregationBuilders.terms("by_province").field("properties.province.keyword");
        termsBuilder.subAggregation(AggregationBuilders.terms("by_city").field("properties.city.keyword"));

        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery
                .should(QueryBuilders.termQuery("properties.name", keyword))
                .should(QueryBuilders.prefixQuery("properties.name", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.name", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.first_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.second_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.third_class", keyword))
                .minimumShouldMatch(1);

        // 组装查询,发送查询请求
        SearchResponse response = client.prepareSearch(Constant.INDEX_ES_POI)
                .setTypes(Constant.TYPE_ES_POI)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(Constant.CHINA_CITY_SIZE)
                .addAggregation(termsBuilder)
                .execute()
                .actionGet();

        // 组装查询到的数据集
        List<RegionOutput> regionOutputList = Lists.newArrayList();

        for (Aggregation aggregation : response.getAggregations()) {
            StringTerms stringTerms = (StringTerms) aggregation;
            //获取省
            for (StringTerms.Bucket provinceBucket : stringTerms.getBuckets()) {
                Terms cityTerms = provinceBucket.getAggregations().get("by_city");
                //获取市
                for (Terms.Bucket cityBucket : cityTerms.getBuckets()) {
                    //获取传入的市名的POI计数
                    for (String region : regionList) {
                        //判断名称是否相等
                        if (cityBucket.getKeyAsString().trim().equals(region.trim())) {
                            // 通过关键字和区域名称获取POI匹配最高的数据
                            Poi poi = findFirstPoi(keyword, region);
                            LOG.info("regionOutput name = [{}],count = [{}]", cityBucket.getKeyAsString(), cityBucket.getDocCount());
                            RegionOutput regionOutput = RegionOutput.builder().name(cityBucket.getKeyAsString()).count(cityBucket.getDocCount()).type(Constant.SEARCH_REGION_TERMS).centroid(poi.getGeometry()).build();
                            regionOutputList.add(regionOutput);
                        }
                    }
                }
            }
        }
        return regionOutputList;
    }

    @Override
    public RegionOutput findTownByKeyword(String keyword, List<String> regionList) {

        LOG.info("ElasticsearchServiceImpl::findTownByKeyword keyword = [{}],regionInputList = [{}]", keyword, regionList);

        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery
                .should(QueryBuilders.termQuery("properties.name.keyword", keyword).boost(10f))
                .should(QueryBuilders.prefixQuery("properties.name.keyword", keyword).boost(8f))
                .should(QueryBuilders.matchPhraseQuery("properties.name", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.first_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.second_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.third_class", keyword))
                .minimumShouldMatch(1)
                .must(QueryBuilders.termsQuery("properties.county.keyword", regionList.toArray(new String[regionList.size()])));

        // 组装查询,发送查询请求
        SearchResponse response = client.prepareSearch(Constant.INDEX_ES_POI)
                .setTypes(Constant.TYPE_ES_POI)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setExplain(true)
                .setFrom(0)
                .setSize(Constant.SEARCH_POI_SIZE)
                .execute()
                .actionGet();


        List<Poi> poiList = Lists.newArrayList();
        for (SearchHit searchHitFields : response.getHits()) {
            poiList.add(JSON.parseObject(searchHitFields.getSourceAsString(), Poi.class));
        }

        return RegionOutput.builder()
                .type(Constant.SEARCH_EXACT_POI)
                .poiList(poiList)
                .build();
    }

    @Override
    public RegionOutput findTownByKeywordAndAnalyzer(String keyword, List<String> regionList) {

        LOG.info("ElasticsearchServiceImpl::findTownByKeywordAndAnalyzer keyword = [{}],regionInputList = [{}]", keyword, regionList);

        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        //增加分词筛选
        List<String> termList = listTerms(keyword, Constant.INDEX_ES_POI, Constant.ik_smart);
        int termSize = termList.size();
        if (termSize > 1) {
            String lastButOneTerm = termList.get(termSize - 2);

            boolQuery
                    .should(QueryBuilders.termQuery("properties.name.keyword", lastButOneTerm).boost(10f))
                    .should(QueryBuilders.prefixQuery("properties.name.keyword", lastButOneTerm).boost(5f))
                    .should(QueryBuilders.matchPhraseQuery("properties.name", lastButOneTerm))
                    .should(QueryBuilders.matchPhraseQuery("properties.third_class", lastButOneTerm))
                    .should(QueryBuilders.matchPhraseQuery("properties.first_class", lastButOneTerm))
                    .should(QueryBuilders.matchPhraseQuery("properties.second_class", lastButOneTerm))
                    .minimumShouldMatch(1)
                    .must(QueryBuilders.matchPhraseQuery("properties.name", termList.get(termSize - 1)));
        }

        if (regionList != null && regionList.size() > 0) {
            boolQuery.must(QueryBuilders.termsQuery("properties.county.keyword", regionList.toArray(new String[regionList.size()])));
        }

        // 组装查询,发送查询请求
        SearchResponse response = client.prepareSearch(Constant.INDEX_ES_POI)
                .setTypes(Constant.TYPE_ES_POI)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(Constant.SEARCH_POI_SIZE)
                .setExplain(true)
                .execute()
                .actionGet();

        List<Poi> poiList = Lists.newArrayList();
        for (SearchHit searchHitFields : response.getHits()) {
            poiList.add(JSON.parseObject(searchHitFields.getSourceAsString(), Poi.class));
        }
        Integer poiSize = poiList.size();

        return RegionOutput.builder()
                .type(Constant.SEARCH_EXACT_POI)
                .poiList(poiList)
                .count(poiSize.longValue())
                .build();
    }

    @Override
    public List<RegionOutput> findRegionByKeyword(String keyword) {

        LOG.info("ElasticsearchServiceImpl::findRegionByKeyword keyword = [{}]", keyword);

        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery
                .should(QueryBuilders.termQuery("properties.name.keyword", keyword).boost(10f))
                .should(QueryBuilders.prefixQuery("properties.name.keyword", keyword).boost(8f))
                .should(QueryBuilders.matchPhraseQuery("properties.name", keyword))
                .minimumShouldMatch(1);

        //增加分词筛选
        List<String> termList = listTerms(keyword, Constant.INDEX_ES_REGION, Constant.ik_smart);
        if (termList.size() > 1) {
            for (String term : termList) {
                boolQuery.should(QueryBuilders.termQuery("properties.name.keyword", term));
                boolQuery.should(QueryBuilders.prefixQuery("properties.name.keyword", term));
            }
        }
        // 组装查询,发送查询请求
        SearchResponse response = client.prepareSearch(Constant.INDEX_ES_REGION)
                .setTypes(Constant.TYPE_ES_REGION)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(5)
                .execute()
                .actionGet();

        List<RegionOutput> regionOutputList = Lists.newArrayList();
        for (SearchHit searchHitFields : response.getHits()) {

            Region region = JSON.parseObject(searchHitFields.getSourceAsString(), Region.class);
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

    @Override
    public RegionOutput findPoiByKeyword(String keyword) {

        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery
                .should(QueryBuilders.termQuery("properties.name.keyword", keyword).boost(10f))
                .should(QueryBuilders.prefixQuery("properties.name.keyword", keyword).boost(8f))
                .should(QueryBuilders.matchPhraseQuery("properties.name", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.first_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.second_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.third_class", keyword))
                .minimumShouldMatch(1);

        // 组装查询,发送查询请求
        SearchResponse response = client.prepareSearch(Constant.INDEX_ES_POI)
                .setTypes(Constant.TYPE_ES_POI)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setExplain(true)
                .setFrom(0)
                .setSize(Constant.SEARCH_POI_SIZE)
                .execute()
                .actionGet();

        List<Poi> poiList = Lists.newArrayList();
        for (SearchHit searchHitFields : response.getHits()) {
            poiList.add(JSON.parseObject(searchHitFields.getSourceAsString(), Poi.class));
        }
        Integer poiSize = poiList.size();
        return RegionOutput.builder()
                .type(Constant.SEARCH_VAGUE_POI)
                .count(poiSize.longValue())
                .poiList(poiList)
                .build();
    }

    @Override
    public List<RegionOutput> findByKeyword(String keyword) {

        List<RegionOutput> regionOutputList = Lists.newArrayList();
        //增加行政区数据
        List<RegionOutput> regionByKeyword = findRegionByKeyword(keyword);
        if (regionByKeyword != null && regionByKeyword.size() >= 1) {
            regionOutputList.addAll(regionByKeyword);
        }
        //增加POI数据
        RegionOutput poiByKeyword = findPoiByKeyword(keyword);
        if (poiByKeyword != null) {
            regionOutputList.add(poiByKeyword);
        }
        if (poiByKeyword.getPoiList().size() < 10) {
            regionOutputList.add(findTownByKeywordAndAnalyzer(keyword, null));
        }
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
    public Set<String> getSuggestSearch(String keyword) {

        CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders.completionSuggestion("properties.name.suggest")
                .prefix(keyword).size(10);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("name_suggest", completionSuggestionBuilder);

        SearchResponse suggestResponse = client.prepareSearch(Constant.INDEX_ES_REGION)
                .setTypes(Constant.TYPE_ES_POI)
                .suggest(suggestBuilder)
                .execute()
                .actionGet();

        Suggest suggest = suggestResponse.getSuggest();

        Set<String> suggestSet = Sets.newHashSet();

        if (suggest != null) {
            Suggest.Suggestion suggestion = suggest.getSuggestion("name_suggest");
            for (Object term : suggestion.getEntries()) {
                if (term instanceof CompletionSuggestion.Entry) {
                    CompletionSuggestion.Entry entry = (CompletionSuggestion.Entry) term;
                    if (!entry.getOptions().isEmpty()) {
                        for (CompletionSuggestion.Entry.Option option : entry.getOptions()) {
                            suggestSet.add(option.getText().string());
                        }
                    }
                }
            }
        }
        return suggestSet;
    }

    /**
     * 通过关键字和区域名称获取POI匹配最高的一个数据
     *
     * @param keyword    关键字
     * @param regionName 区域名称
     * @return
     */
    private Poi findFirstPoi(String keyword, String regionName) {

        // 组装查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery
                .should(QueryBuilders.termQuery("properties.name.keyword", keyword).boost(10f))
                .should(QueryBuilders.prefixQuery("properties.name.keyword", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.name", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.first_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.second_class", keyword))
                .should(QueryBuilders.matchPhraseQuery("properties.third_class", keyword))
                .minimumShouldMatch(1)
                .must(QueryBuilders.matchQuery("properties.city.keyword", regionName));

        // 组装查询,发送查询请求
        SearchResponse response = client.prepareSearch(Constant.INDEX_ES_POI)
                .setTypes(Constant.TYPE_ES_POI)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setExplain(true)
                .setFrom(0)
                .setSize(2)
                .execute()
                .actionGet();

        Poi poi = null;
        for (SearchHit searchHitFields : response.getHits()) {
            poi = JSON.parseObject(searchHitFields.getSourceAsString(), Poi.class);
            break;
        }
        return poi;
    }

    /**
     * 对关键字分词处理
     *
     * @param keyword  关键字
     * @param index    es搜索索引
     * @param analyzer 分词类型
     * @return
     */
    private List<String> listTerms(String keyword, String index, String analyzer) {

        //ik类型 ik_max_word ik_smart
        AnalyzeRequest analyzeRequest = new AnalyzeRequest(index)
                .text(keyword)
                .analyzer(analyzer);
        List<AnalyzeResponse.AnalyzeToken> tokens = client.admin()
                .indices()
                .analyze(analyzeRequest)
                .actionGet()
                .getTokens();

        List<String> termList = Lists.newArrayList();
        for (AnalyzeResponse.AnalyzeToken token : tokens) {
            termList.add(token.getTerm());
        }
        return termList;
    }
}
