package cc.wanshan.gis.service.search;

import cc.wanshan.gis.entity.search.RegionInput;
import cc.wanshan.gis.entity.search.RegionOutput;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ESCrudService {
    /****
     * 按照ID 查询
     * @param id
     * @return ResponseEntity
     */
    ResponseEntity searchById(String id);

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
    ResponseEntity query(String id, String provinces_name, String city_name, String area_name, String first_name,
                         String second_name, String baidu_first_name, String baidu_second_name, String name,
                         String addr, String phone);

    /****
     * 搜索查询省数据
     * @param inputValue
     * @return
     */
    ResponseEntity queryDataByInputValue(String inputValue);

    /****
     * 搜索查询市经纬度
     * @param inputCityName
     * @return
     */
    String queryCityCoordinatesByInputValue(String inputCityName);

    /***
     * POI按照省市县村分组聚和查询（全国）
     */
    ResponseEntity queryPoiValue(String poiValue);

    /***
     * POI按照省分组聚和查询（省级）
     */
    ResponseEntity findProvinceDataByPoiValue(String poiValue, List<String> provinceListName);

    /***
     * POI按照省分组聚和查询（查询--省级）
     */
    List<RegionOutput> findProvinceByKeyword(String keyword, List<RegionInput> regionInputList);

    /***
     * POI按照省分组聚和查询（查询--市级）
     */
    List<RegionOutput> findCityByKeyword(String keyword, List<RegionInput> regionInputList);

    /***
     * POI按照省分组聚和查询（查询--区县级）
     */
    List<RegionOutput> findTownByKeyword(String keyword, List<RegionInput> regionInputList);

    /***
     * POI按照市分组聚和查询（市级）
     */

    ResponseEntity findCityDataByPoiValue(String poiValue, List<String> cityListName);

    /***
     * POI按照县/区分组聚和查询（县/区级）
     */

    ResponseEntity findCountyDataByPoiValue(String poiValue, List<String> countyListName);

    /***
     * POI按照县级以下查询数据(县级以下)
     * @param poiValue
     * @param townListName
     * @return
     */
    ResponseEntity findTownDataByPoiValue(String poiValue, List<String> townListName);

    /**
     * 按id删除数据
     *
     * @param id
     * @return
     */
    ResponseEntity delete(String id);

}
