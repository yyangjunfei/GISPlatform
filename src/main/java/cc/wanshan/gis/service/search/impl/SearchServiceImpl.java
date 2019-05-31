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
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private static Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class);

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

    @Override
    public Result searchAreaName(double longitude, double latitude, double level) {

        Point point = GeometryCreator.createPoint(longitude, latitude);
        try {
            if (level <= 8) {
                LOG.info("国家::level==[{}] ", level);
                List<Country> countryList = searchDao.findAllCountry();

                for (Country country : countryList) {
                    String envelope = country.getEnvelope();
                    Geometry geometry = GeotoolsUtils.geoJson2Geometry(envelope);
                    if (geometry.contains(point)) {
                        LOG.info("====国家=== longitude::[{}],latitude::[{}],longitude::[{}],level::[{}],name::[{}]", longitude, latitude, level, country.getName());
                        return ResultUtil.success(country);
                    }
                }
            } else if (level <= 11) {
                LOG.info("省::level==[{}] ", level);
                List<Province> provinceList = searchDao.findAllProvince();
                for (Province province : provinceList) {
                    String envelope = province.getEnvelope();
                    Geometry geometry = GeotoolsUtils.geoJson2Geometry(envelope);
                    if (geometry.contains(point)) {
                        LOG.info("====省=== longitude::[{}],latitude::[{}],longitude::[{}],level::[{}],name::[{}]", longitude, latitude, level, province.getName());

                        return ResultUtil.success(province);
                    }
                }
            } else if (level <= 14) {
                LOG.info("市::level==[{}] ", level);
                List<City> cityList = searchDao.findAllCity();
                for (City city : cityList) {
                    String envelope = city.getEnvelope();
                    Geometry geometry = GeotoolsUtils.geoJson2Geometry(envelope);
                    if (geometry.contains(point)) {
                        LOG.info("====市=== longitude::[{}],latitude::[{}],longitude::[{}],level::[{}],name::[{}]", longitude, latitude, level, city.getName());

                        return ResultUtil.success(city);
                    }
                }
            } else {
                LOG.info("县::level==[{}] ", level);
                List<Town> townList = searchDao.findAllTown();
                for (Town town : townList) {
                    String envelope = town.getEnvelope();
                    Geometry geometry = GeotoolsUtils.geoJson2Geometry(envelope);
                    if (geometry.contains(point)) {
                        LOG.info("====县=== longitude::[{}],latitude::[{}],longitude::[{}],level::[{}],name::[{}]", longitude, latitude, level, town.getName());
                        return ResultUtil.success(town);
                    }
                }
            }
            return ResultUtil.error(ResultCode.DATA_NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(ResultCode.BUSINESS_ERROR);
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
        return ResultUtil.success();
    }

}
