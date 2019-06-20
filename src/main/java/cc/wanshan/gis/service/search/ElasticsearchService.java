package cc.wanshan.gis.service.search;

import cc.wanshan.gis.entity.search.RegionInput;
import cc.wanshan.gis.entity.search.RegionOutput;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ElasticsearchService {

    /****
     * 按照ID 查询
     *
     * @param id
     * @return ResponseEntity
     */
    ResponseEntity searchById(String id);

    /**
     * 按id删除数据
     *
     * @param id
     * @return
     */
    ResponseEntity delete(String id);

    /**
     * 删除elasticsearch索引库
     *
     * @param indexName 索引库
     * @return
     */
    ResponseEntity deleteElasticsearchIndex(String indexName);

    /***
     * 导入postgis 数据库到Elasticsearch
     * @return ResponseEntity
     */
    ResponseEntity postGisDb2es(String dbURL, String dbUserName, String dbPassword, String driverClassName, String sql, String esindexName, String esTypeName);

    /**
     * POI按照省分组聚和查询（查询--市级）
     *
     * @param keyword         关键字
     * @param regionInputList 市区名称集合
     * @return
     */
    List<RegionOutput> findCityByKeyword(String keyword, List<RegionInput> regionInputList);

    /**
     * POI按照省分组聚和查询（查询--区县级）
     *
     * @param keyword         关键字
     * @param regionInputList 县区名称集合
     * @return
     */
    List<RegionOutput> findTownByKeyword(String keyword, List<RegionInput> regionInputList);

    /**
     * 根据关键字搜索行政区
     */
    List<RegionOutput> findRegionByKeyword(String keyword);

    /**
     * 根据关键字搜索POI数据
     *
     * @param keyword 关键字
     * @return
     */
    List<RegionOutput> findPoiByKeyword(String keyword);

    /**
     * 根据关键字搜索ES数据
     *
     * @param keyword 关键字
     * @return
     */
    List<RegionOutput> findByKeyword(String keyword);
}
