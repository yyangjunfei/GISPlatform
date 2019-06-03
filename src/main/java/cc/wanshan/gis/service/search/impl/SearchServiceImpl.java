package cc.wanshan.gis.service.search.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.dao.search.SearchDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.area.City;
import cc.wanshan.gis.entity.area.Country;
import cc.wanshan.gis.entity.area.Province;
import cc.wanshan.gis.entity.area.Town;
import cc.wanshan.gis.mapper.search.CityMapper;
import cc.wanshan.gis.mapper.search.CountryMapper;
import cc.wanshan.gis.mapper.search.ProvinceMapper;
import cc.wanshan.gis.mapper.search.TownMapper;
import cc.wanshan.gis.service.search.SearchService;
import cc.wanshan.gis.utils.GeometryCreator;
import cc.wanshan.gis.utils.GeotoolsUtils;
import cc.wanshan.gis.utils.ResultUtil;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private static Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class);

    private static List<Country> countryList;
    private static List<Province> provinceList;
    private static List<City> cityList;
    private static List<Town> townList;

    @Autowired
    private SearchDao searchDao;

    @Resource
    private CountryMapper countryMapper;

    @Resource
    private ProvinceMapper provinceMapper;

    @Resource
    private CityMapper cityMapper;

    @Resource
    private TownMapper townMapper;

    /**
     * 初始化方法
     */
    @PostConstruct
    public void init() {
        countryList = searchDao.findAllCountry();
        provinceList = searchDao.findAllProvince();
        cityList = searchDao.findAllCity();
        townList = searchDao.findAllTown();
    }

    @Override

    public Result searchAreaName(double longitude, double latitude, double level) {

        Point point = GeometryCreator.createPoint(longitude, latitude);
        List<Country> countries = Lists.newArrayList();
        List<Province> provinces = Lists.newArrayList();
        List<City> cities = Lists.newArrayList();
        List<Town> towns = Lists.newArrayList();

        try {
            if (level <= 8) {
//                List<Country> countryList = searchDao.findAllCountry();
                for (Country country : countryList) {
                    Geometry geometry = GeotoolsUtils.geoJson2Geometry(country.getEnvelope());
                    if (geometry.contains(point)) {
                        countries.add(country);
                    }
                }
                for (Country country : countries) {
                    Country oneCountry = searchDao.findOneCountry(country.getGid());
                    String geom = oneCountry.getGeometry();
                    Geometry geometry = GeotoolsUtils.geoJson2Geometry(geom);
                    if (geometry.contains(point)) {
                        return ResultUtil.success(country);
                    }
                }
            } else if (level <= 11) {
//                List<Province> provinceList = searchDao.findAllProvince();
                for (Province province : provinceList) {
                    String envelope = province.getEnvelope();
                    Geometry geometry = GeotoolsUtils.geoJson2Geometry(envelope);
                    if (geometry.contains(point)) {
                        provinces.add(province);
                    }
                }
                for (Province province : provinces) {
                    Province oneProvince = searchDao.findOneProvince(province.getGid());
                    String geom = oneProvince.getGeometry();
                    Geometry geometry = GeotoolsUtils.geoJson2Geometry(geom);
                    if (geometry.contains(point)) {
                        return ResultUtil.success(province);
                    }
                }
            } else if (level <= 14) {
//                List<City> cityList = searchDao.findAllCity();
                for (City city : cityList) {
                    String envelope = city.getEnvelope();
                    Geometry geometry = GeotoolsUtils.geoJson2Geometry(envelope);
                    if (geometry.contains(point)) {
                        cities.add(city);
                    }
                }
                for (City city : cities) {
                    City oneCity = searchDao.findOneCity(city.getGid());
                    String geom = oneCity.getGeometry();
                    Geometry geometry = GeotoolsUtils.geoJson2Geometry(geom);
                    if (geometry.contains(point)) {
                        return ResultUtil.success(city);
                    }
                }
            } else {
//                List<Town> townList = searchDao.findAllTown();
                for (Town town : townList) {
                    String envelope = town.getEnvelope();
                    Geometry geometry = GeotoolsUtils.geoJson2Geometry(envelope);
                    if (geometry.contains(point)) {
                        towns.add(town);
                    }
                }
                for (Town town : towns) {
                    Town oneTown = searchDao.findOneTown(town.getGid());
                    String geom = oneTown.getGeometry();
                    Geometry geometry = GeotoolsUtils.geoJson2Geometry(geom);
                    if (geometry.contains(point)) {
                        return ResultUtil.success(town);
                    }
                }
            }
            return ResultUtil.error(ResultCode.DATA_NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(ResultCode.GEOMETRY_TRANSFORM_FAIL);
        }
    }

    @Override
    public Result perfectField() {
//        List<Province> provinceList = searchDao.findProvinceAll();
//        for (Province province : provinceList) {
////            province.setRectangle(province.getMinX() + "," + province.getMinY() + "," + province.getMaxX() + "," + province.getMaxY());
//            provinceMapper.updateByPrimaryKeySelective(province);
//        }
//        List<City> cityList = searchDao.findCityAll();
//        for (City city : cityList) {
////            city.setRectangle(city.getMinX() + "," + city.getMinY() + "," + city.getMaxX() + "," + city.getMaxY());
//
//            cityMapper.updateByPrimaryKeySelective(city);
//        }
//        List<Country> countryList = searchDao.findCountryAll();
//        for (Country country : countryList) {
////            country.setRectangle(country.getMinX() + "," + country.getMinY() + "," + country.getMaxX() + "," + country.getMaxY());
//
//            countryMapper.updateByPrimaryKeySelective(country);
//        }
//
//        List<Town> townList = searchDao.findTownAll();
//        for (Town town : townList) {
////            town.setRectangle(town.getMinX() + "," + town.getMinY() + "," + town.getMaxX() + "," + town.getMaxY());
//
//            townMapper.updateByPrimaryKeySelective(town);
//        }

//        Province oneProvince = searchDao.findOneProvince(13);

        return ResultUtil.success();
    }

}
