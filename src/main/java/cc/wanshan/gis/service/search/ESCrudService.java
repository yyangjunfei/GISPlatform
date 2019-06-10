package cc.wanshan.gis.service.search;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ESCrudService {

    ResponseEntity searchById(String id);

    ResponseEntity query(String id,String provinces_name,String city_name,String area_name,String first_name,
                              String second_name,String baidu_first_name,String baidu_second_name,String name,
                              String addr,String phone);

    ResponseEntity queryDataByInputValue(String inputValue);

    String queryCityCoordinatesByInputValue(String inputCityName);

    ResponseEntity queryPoiValue( String poiValue);

    ResponseEntity findProvinceDataByPoiValue(String poiValue, List<String> provinceListName);

    ResponseEntity findCityDataByPoiValue(String poiValue,List<String> cityListName);

    ResponseEntity findCountyDataByPoiValue(String poiValue, List<String> countyListName);

    ResponseEntity findTownDataByPoiValue(String poiValue,List<String> townListName);

    ResponseEntity delete( String id);

}
