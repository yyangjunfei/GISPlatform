package cc.wanshan.gis.service.search.impl;

import cc.wanshan.gis.common.constants.Constant;
import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.dao.search.SearchDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.area.City;
import cc.wanshan.gis.entity.area.Country;
import cc.wanshan.gis.entity.area.Province;
import cc.wanshan.gis.entity.area.Town;
import cc.wanshan.gis.entity.search.Poi;
import cc.wanshan.gis.entity.search.RegionInput;
import cc.wanshan.gis.entity.search.RegionOutput;
import cc.wanshan.gis.mapper.search.CityMapper;
import cc.wanshan.gis.mapper.search.CountryMapper;
import cc.wanshan.gis.mapper.search.ProvinceMapper;
import cc.wanshan.gis.mapper.search.TownMapper;
import cc.wanshan.gis.service.search.ESCrudService;
import cc.wanshan.gis.service.search.SearchService;
import cc.wanshan.gis.utils.GeoToolsUtils;
import cc.wanshan.gis.utils.GeometryCreator;
import cc.wanshan.gis.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
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

    private List<Country> countryList;
    private List<Province> provinceList;
    private List<City> cityList;
    private List<Town> townList;

    @Autowired
    private SearchDao searchDao;

    @Autowired
    private ESCrudService esCrudService;

    /**
     * 初始化方法
     */

    @Resource
    private CountryMapper countryMapper;

    @Resource
    private ProvinceMapper provinceMapper;

    @Resource
    private CityMapper cityMapper;

    @Resource
    private TownMapper townMapper;

    /**
     * 初始化
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

        //级别[8, 22]查询省市，其他级别查询国家，查不到返回未找到
        try {
            if (level > 8 && level <= 11) {
                for (Province province : provinceList) {
                    String envelope = province.getEnvelope();
                    Geometry geometry = GeoToolsUtils.geoJson2Geometry(envelope);
                    if (geometry.contains(point)) {
                        provinces.add(province);
                    }
                }
                for (Province province : provinces) {
                    Province oneProvince = searchDao.findOneProvince(province.getGid());
                    String geom = oneProvince.getGeometry();
                    Geometry geometry = GeoToolsUtils.geoJson2Geometry(geom);
                    if (geometry.contains(point)) {
                        return ResultUtil.success(province);
                    }
                }
            } else if (level > 11 && level <= 22) {
                for (City city : cityList) {
                    String envelope = city.getEnvelope();
                    Geometry geometry = GeoToolsUtils.geoJson2Geometry(envelope);
                    if (geometry.contains(point)) {
                        cities.add(city);
                    }
                }
                for (City city : cities) {
                    City oneCity = searchDao.findOneCity(city.getGid());
                    String geom = oneCity.getGeometry();
                    Geometry geometry = GeoToolsUtils.geoJson2Geometry(geom);
                    if (geometry.contains(point)) {
                        return ResultUtil.success(city);
                    }
                }
            }

            //级别[8, 22]查询省市，查不到再查询国家；查不到返回未找到
            for (Country country : countryList) {
                Geometry geometry = GeoToolsUtils.geoJson2Geometry(country.getEnvelope());
                if (geometry.contains(point)) {
                    countries.add(country);
                }
            }
            for (Country country : countries) {
                Country oneCountry = searchDao.findOneCountry(country.getGid());
                String geom = oneCountry.getGeometry();
                Geometry geometry = GeoToolsUtils.geoJson2Geometry(geom);
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

    @Override
    public Result searchAreaGeoFromES(String name) {

        return esCrudService.searchAreaGeoFromES(name);
    }

    @Override
    public Result searchPlace(JSONObject jsonObject) {

        double level = jsonObject.getDouble(Constant.SEARCH_LEVEL);
        String rectangle = jsonObject.getString(Constant.SEARCH_RECTANGLE);
        String keyword = jsonObject.getString(Constant.KEYWORD);

        LOG.info("SearchServiceImpl::searchPlace level=[{}],rectangle=[{}],keyword=[{}]", level, rectangle, keyword);

        //拆分包围形字符串
        String[] arr = rectangle.split(Constant.split);
        double minX = Double.parseDouble(arr[0]);
        double minY = Double.parseDouble(arr[1]);
        double maxX = Double.parseDouble(arr[2]);
        double maxY = Double.parseDouble(arr[3]);


        List<RegionInput> regionInputList = Lists.newArrayList();
        Geometry polygon = null;
        try {
            //根据包围形封装成geometry格式的面数据
            polygon = GeoToolsUtils.polygon2Geometry(minX, minY, maxX, maxY);

            //级别[8, 22]查询省市，其他级别查询国家，查不到返回未找到
            if (level <= 8) {
                //级别低于8，（国家级别）数据不显示
                return ResultUtil.error(ResultCode.DATA_NOT_SUPPORT);
            } else if (level > 8 && level <= 11) {
                for (Province province : provinceList) {
                    String envelope = province.getEnvelope();
                    Geometry geometry = GeoToolsUtils.geoJson2Geometry(envelope);
                    if (geometry.intersects(polygon)) {
                        //es入参
                        RegionInput regionInput = RegionInput.builder().name(province.getName()).centroid(province.getCentroid()).build();
                        regionInputList.add(regionInput);
                    }
                }
                if (regionInputList != null) {

                    //从es查询省份的聚合数据
                    List<RegionOutput> regionOutputList = esCrudService.findProvinceByKeyword(keyword, regionInputList);

                    return ResultUtil.success(regionOutputList);
                }

            } else if (level > 11 && level <= 14) {
                for (City city : cityList) {
                    String envelope = city.getEnvelope();
                    Geometry geometry = GeoToolsUtils.geoJson2Geometry(envelope);
                    if (polygon.intersects(geometry)) {
                        //es入参
                        RegionInput regionInput = RegionInput.builder().name(city.getName()).centroid(city.getCentroid()).build();
                        regionInputList.add(regionInput);
                    }
                }
                if (regionInputList != null) {
                    //从es查询市份的聚合数据
                    List<RegionOutput> regionOutputList = esCrudService.findCityByKeyword(keyword, regionInputList);
                    return ResultUtil.success(regionOutputList);
                }
            } else if (level > 14 && level <= 22) {
                for (Town town : townList) {
                    String envelope = town.getEnvelope();
                    Geometry geometry = GeoToolsUtils.geoJson2Geometry(envelope);
                    if (polygon.intersects(geometry)) {
                        //es入参
                        RegionInput regionInput = RegionInput.builder().name(town.getName()).centroid(town.getCentroid()).build();
                        regionInputList.add(regionInput);
                    }
                }

                if (regionInputList != null) {

                    //从es查询区县份的聚合数据
                    List<RegionOutput> regionOutputList = esCrudService.findTownByKeyword(keyword, regionInputList);
                    List<RegionOutput> regionOutputs = Lists.newArrayList();

                    for (RegionOutput regionOutput : regionOutputList) {

                        List<Poi> poiList = regionOutput.getPoiList();

                        //判断点是否在包围形中
                        List<Poi> pois = Lists.newArrayList();
                        for (Poi poi : poiList) {
                            Geometry geometry = GeoToolsUtils.geoJson2Geometry(poi.getGeometry());
                            if (polygon.contains(geometry)) {
                                pois.add(poi);
                            }
                        }
                        regionOutput.setPoiList(null);
                        regionOutput.setPoiList(pois);
                        regionOutputs.add(regionOutput);
                    }
                    return ResultUtil.success(regionOutputList);
                }
            }
            return ResultUtil.error(ResultCode.DATA_NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(ResultCode.GEOMETRY_TRANSFORM_FAIL);
        }
    }


    @Override
    public Result test() {

        /**
         * 国家数据更新
         */
        List<Country> countryList = searchDao.queryAllCountry();
        for (Country country : countryList) {
            country.setRectangle(country.getMinX() + "," + country.getMinY() + "," + country.getMaxX() + "," + country.getMaxY());
            countryMapper.updateByPrimaryKeySelective(country);
        }
        /**
         * 省级数据更新
         */
        List<Province> provinceList = searchDao.queryAllProvince();
        for (Province province : provinceList) {
            province.setRectangle(province.getMinX() + "," + province.getMinY() + "," + province.getMaxX() + "," + province.getMaxY());
            provinceMapper.updateByPrimaryKeySelective(province);
        }
        /**
         * 城市数据更新
         */
        List<City> cityList = searchDao.queryAllCity();
        for (City city : cityList) {
            city.setRectangle(city.getMinX() + "," + city.getMinY() + "," + city.getMaxX() + "," + city.getMaxY());
            cityMapper.updateByPrimaryKeySelective(city);
        }
        /**
         * 县区数据更新
         */
        List<Town> townList = searchDao.queryAllTown();
        for (Town town : townList) {
            town.setRectangle(town.getMinX() + "," + town.getMinY() + "," + town.getMaxX() + "," + town.getMaxY());
            townMapper.updateByPrimaryKeySelective(town);
        }
        return ResultUtil.success();
    }

}

