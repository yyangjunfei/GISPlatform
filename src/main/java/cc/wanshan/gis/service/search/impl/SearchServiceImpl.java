package cc.wanshan.gis.service.search.impl;
import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.dao.search.SearchDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.area.City;
import cc.wanshan.gis.entity.area.Country;
import cc.wanshan.gis.entity.area.Province;
import cc.wanshan.gis.entity.area.Town;
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
import java.io.IOException;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private static Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class);

    private static List<Country> countryList;
    private static List<Province> provinceList;
    private static List<City> cityList;

    @Autowired
    private SearchDao searchDao;

/**
     * 初始化方法
     */

    @PostConstruct
    public void init() {
        countryList = searchDao.findAllCountry();
        provinceList = searchDao.findAllProvince();
        cityList = searchDao.findAllCity();
    }

    @Override
    public Result searchAreaName(double longitude, double latitude, double level) {

        Point point = GeometryCreator.createPoint(longitude, latitude);
        List<Country> countries = Lists.newArrayList();
        List<Province> provinces = Lists.newArrayList();
        List<City> cities = Lists.newArrayList();

        //级别[8, 22]查询省市，其他级别查询国家，查不到返回未找到
        try {
            if (level > 8 && level <= 11) {
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
            } else if (level > 11 && level <= 22) {
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
            }

            //级别[8, 22]查询省市，查不到再查询国家；查不到返回未找到
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
            return ResultUtil.error(ResultCode.DATA_NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(ResultCode.GEOMETRY_TRANSFORM_FAIL);
        }
    }

    @Override
    public Result searchAreaGeo(String name) {

        // 判空
        if (name == null || name.length() <= 0) {
            return ResultUtil.error(ResultCode.PARAM_IS_NULL);
        }

        List<Country> allCountryGeo = searchDao.findAllCountryGeo(name);
        if (allCountryGeo != null && allCountryGeo.size() > 0) {
            LOG.info("[searchAreaGeo] size;;[{}],allCountryGeo;;[{}]", allCountryGeo.size(), allCountryGeo);

            return ResultUtil.success(allCountryGeo);
        }
        List<Province> allProvinceGeo = searchDao.findAllProvinceGeo(name);

        if (allProvinceGeo != null && allProvinceGeo.size() > 0) {
            LOG.info("[searchAreaGeo] size;;[{}],allProvinceGeo;;[{}]", allProvinceGeo.size(), allProvinceGeo);

            return ResultUtil.success(allProvinceGeo);
        }

        List<City> allCityGeo = searchDao.findAllCityGeo(name);

        if (allCityGeo != null && allCityGeo.size() > 0) {
            LOG.info("[searchAreaGeo] size;;[{}],allCityGeo;;[{}]", allCityGeo.size(), allCityGeo);

            return ResultUtil.success(allCityGeo);
        }

        List<Town> allTownGeo = searchDao.findAllTownGeo(name);

        if (allTownGeo != null && allTownGeo.size() > 0) {
            LOG.info("[searchAreaGeo] size;;[{}],allTownGeo;;[{}]", allTownGeo.size(), allTownGeo);

            return ResultUtil.success(allTownGeo);
        }

        return ResultUtil.error(ResultCode.FIND_NULL);
    }

}

