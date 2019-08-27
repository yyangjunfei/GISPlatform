package cc.wanshan.gis.service.search.impl;

import cc.wanshan.gis.common.constant.CommonConstant;
import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.search.SearchDao;
import cc.wanshan.gis.entity.search.City;
import cc.wanshan.gis.entity.search.Country;
import cc.wanshan.gis.entity.search.Poi;
import cc.wanshan.gis.entity.search.Province;
import cc.wanshan.gis.entity.search.RegionOutput;
import cc.wanshan.gis.entity.search.Suggest;
import cc.wanshan.gis.entity.search.Town;
import cc.wanshan.gis.mapper.search.CityMapper;
import cc.wanshan.gis.mapper.search.CountryMapper;
import cc.wanshan.gis.mapper.search.ProvinceMapper;
import cc.wanshan.gis.mapper.search.TownMapper;
import cc.wanshan.gis.service.search.ElasticsearchService;
import cc.wanshan.gis.service.search.SearchService;
import cc.wanshan.gis.utils.base.ResultUtil;
import cc.wanshan.gis.utils.geo.GeoToolsUtils;
import cc.wanshan.gis.utils.geo.GeometryCreator;
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
import java.util.Set;

@Service
public class SearchServiceImpl implements SearchService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    //行政区--国家
    private List<Country> countryList;
    //行政区--省
    private List<Province> provinceList;
    //行政区--市
    private List<City> cityList;
    //行政区--县
    private List<Town> townList;

    @Autowired
    private SearchDao searchDao;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Resource
    private CountryMapper countryMapper;

    @Resource
    private ProvinceMapper provinceMapper;

    @Resource
    private CityMapper cityMapper;

    @Resource
    private TownMapper townMapper;

    /**
     * 初始化行政区域数据
     */
    @PostConstruct
    public void init() {
        countryList = searchDao.findAllCountry();
        provinceList = searchDao.findAllProvince();
        cityList = searchDao.findAllCity();
        townList = searchDao.findAllTown();
    }

    @Override
    public Result searchByLocation(double longitude, double latitude, double level) {

        Point point = GeometryCreator.createPoint(longitude, latitude);
        List<Country> countries = Lists.newArrayList();
        List<Province> provinces = Lists.newArrayList();
        List<City> cities = Lists.newArrayList();

        //级别[8, 22]查询省市，其他级别查询国家，查不到返回未找到
        try {
            if (level > 8 && level <= 11) {
                for (Province province : provinceList) {
                    Geometry geometry = GeoToolsUtils.geoJson2Geometry(province.getEnvelope());
                    if (geometry.contains(point)) {
                        provinces.add(province);
                    }
                }
                if (null != provinces && provinces.size() > 1) {
                    for (Province province : provinces) {
                        Province oneProvince = searchDao.findOneProvince(province.getGid());
                        Geometry geometry = GeoToolsUtils.geoJson2Geometry(oneProvince.getGeometry());
                        if (geometry.contains(point)) {
                            return ResultUtil.success(province);
                        }
                    }
                }
            } else if (level > 11 && level <= 22) {
                for (City city : cityList) {
                    Geometry geometry = GeoToolsUtils.geoJson2Geometry(city.getEnvelope());
                    if (geometry.contains(point)) {
                        cities.add(city);
                    }
                }
                if (null != cities && cities.size() > 1) {
                    for (City city : cities) {
                        City oneCity = searchDao.findOneCity(city.getGid());
                        Geometry geometry = GeoToolsUtils.geoJson2Geometry(oneCity.getGeometry());
                        if (geometry.contains(point)) {
                            return ResultUtil.success(city);
                        }
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
                Geometry geometry = GeoToolsUtils.geoJson2Geometry(oneCountry.getGeometry());
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
    public Result searchByName(String name) {

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
    public Result searchByPlace(JSONObject jsonObject) {

        //区分查询类型 1. 区域查询 2. 通用查询
        String type = jsonObject.getString(CommonConstant.TYPE);

        String keyword = jsonObject.getString(CommonConstant.KEYWORD);
        if (keyword == null || keyword.length() <= 0) {
            return ResultUtil.error(ResultCode.PARAM_IS_NULL);
        }

        //只传参数关键字,类型搜索匹配所有ES数据
        if (CommonConstant.SEARCH_COMMON.equals(type)) {
            List<RegionOutput> regionOutputList = elasticsearchService.findByKeyword(keyword);

            if (regionOutputList == null) {
                return ResultUtil.error(ResultCode.FIND_NULL);
            } else {
                return ResultUtil.success(regionOutputList);
            }
        }

        List<RegionOutput> regionOutputList = Lists.newArrayList();
        double level = jsonObject.getDouble(CommonConstant.SEARCH_LEVEL);
        String rectangle = jsonObject.getString(CommonConstant.SEARCH_RECTANGLE);

        List<String> regionList = Lists.newArrayList();

        //根据关键字查询行政区数据
        List<RegionOutput> regionByKeyword = elasticsearchService.findRegionByKeyword(keyword);

        if (regionByKeyword != null) {
            regionOutputList.addAll(regionByKeyword);
        }

        //拆分包围形字符串
        String[] arr = rectangle.split(CommonConstant.split);
        double minX = Double.parseDouble(arr[0]);
        double minY = Double.parseDouble(arr[1]);
        double maxX = Double.parseDouble(arr[2]);
        double maxY = Double.parseDouble(arr[3]);

        Geometry polygon = null;
        try {
            //根据包围形封装成geometry格式的面数据
            polygon = GeoToolsUtils.polygon2Geometry(minX, minY, maxX, maxY);

            //查询ES POI数据
            if (level <= 13) {
                for (City city : cityList) {
                    Geometry geometry = GeoToolsUtils.geoJson2Geometry(city.getEnvelope());
                    if (polygon.intersects(geometry)) {
                        regionList.add(city.getName());
                    }
                }
                if (regionList != null) {
                    //从es查询市区的聚合数据
                    List<RegionOutput> cityByKeyword = elasticsearchService.findCityByKeyword(keyword, regionList);
                    if (cityByKeyword != null) {
                        regionOutputList.addAll(cityByKeyword);
                    }
                }
            } else {
                for (Town town : townList) {
                    Geometry geometry = GeoToolsUtils.geoJson2Geometry(town.getEnvelope());
                    if (polygon.intersects(geometry)) {
                        regionList.add(town.getName());
                    }
                }
                if (regionList != null) {
                    //从es查询区县份的聚合数据
                    RegionOutput regionOutput = elasticsearchService.findTownByKeyword(keyword, regionList);
                    if (regionOutput != null) {
                        List<Poi> poiList = regionOutput.getPoiList();
                        //判断点是否在包围形中
                        List<Poi> pois = Lists.newArrayList();
                        for (Poi poi : poiList) {
                            Geometry geometry = GeoToolsUtils.geoJson2Geometry(poi.getGeometry());
                            if (polygon.contains(geometry)) {
                                pois.add(poi);
                            }
                        }
                        if (pois != null && !pois.isEmpty()) {
                            regionOutput.setPoiList(null);
                            regionOutput.setPoiList(pois);
                            regionOutput.setCount(Long.parseLong(String.valueOf(pois.size())));
                            regionOutputList.add(regionOutput);
                        }
                    }
                }
            }

            if (regionOutputList == null) {
                return ResultUtil.error(ResultCode.FIND_NULL);
            } else {
                return ResultUtil.success(regionOutputList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(ResultCode.GEOMETRY_TRANSFORM_FAIL);
        }
    }

    @Override
    public Result getSuggestSearch(String keyword) {

        Set<String> suggestSet = elasticsearchService.getSuggestSearch(keyword);

        Suggest suggest = Suggest.builder().type(CommonConstant.SEARCH_SUGGEST).suggestSet(suggestSet).build();

        if (suggestSet == null) {
            return ResultUtil.error(ResultCode.FIND_NULL);
        } else {
            return ResultUtil.success(suggest);
        }
    }

    @Override
    public Result test() {

        /**
         * 国家数据更新
         */
    /*    List<Country> countryList = searchDao.queryAllCountry();
        for (Country country : countryList) {
            country.setRectangle(country.getMinX() + "," + country.getMinY() + "," + country.getMaxX() + "," + country.getMaxY());
            countryMapper.updateByPrimaryKeySelective(country);
        }*/
        /**
         * 省级数据更新
         */
       /* List<Province> provinceList = searchDao.queryAllProvince();
        for (Province province : provinceList) {
            province.setRectangle(province.getMinX() + "," + province.getMinY() + "," + province.getMaxX() + "," + province.getMaxY());
            provinceMapper.updateByPrimaryKeySelective(province);
        }*/
        /**
         * 城市数据更新
         */
     /*   List<City> cityList = searchDao.queryAllCity();
        for (City city : cityList) {
            city.setRectangle(city.getMinX() + "," + city.getMinY() + "," + city.getMaxX() + "," + city.getMaxY());
            cityMapper.updateByPrimaryKeySelective(city);
        }*/
        /**
         * 县区数据更新
         */
       /* List<Town> townList = searchDao.queryAllTown();
        for (Town town : townList) {
            town.setRectangle(town.getMinX() + "," + town.getMinY() + "," + town.getMaxX() + "," + town.getMaxY());
            townMapper.updateByPrimaryKeySelective(town);
        }*/

//        List<String> list = elasticsearchService.getSuggestSearch("西安");


        return ResultUtil.success();
    }

}

