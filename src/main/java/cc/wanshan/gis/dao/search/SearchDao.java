package cc.wanshan.gis.dao.search;

import cc.wanshan.gis.entity.search.City;
import cc.wanshan.gis.entity.search.Country;
import cc.wanshan.gis.entity.search.Province;
import cc.wanshan.gis.entity.search.Town;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SearchDao {

    /**
     * 查询所有（参数 name envelope,centroid）
     */
    @Select("select c.gid,c.name,c.centroid,c.envelope from search_country c")
    List<Country> findAllCountry();

    @Select("select p.gid,p.name,p.centroid,p.envelope from search_province p")
    List<Province> findAllProvince();

    @Select("select c.gid,c.name,c.centroid,c.envelope from search_city c")
    List<City> findAllCity();

    @Select("select t.gid,t.name,t.centroid,t.envelope from search_town t")
    List<Town> findAllTown();

    /**
     * 查询所有（关键参数）用于生成关键字段数据
     */
    @Select("select p.gid,p.name,st_xmin(p.geom) min_x,st_ymin(p.geom) min_y,st_xmax(p.geom) max_x,st_ymax(p.geom) max_y,ST_AsGeoJSON(ST_Envelope(p.geom)) envelope,ST_AsGeoJSON(ST_Centroid(p.geom)) centroid from search_country p")
    List<Country> queryAllCountry();

    @Select("select p.gid,p.name,st_xmin(p.geom) min_x,st_ymin(p.geom) min_y,st_xmax(p.geom) max_x,st_ymax(p.geom) max_y,ST_AsGeoJSON(ST_Envelope(p.geom)) envelope,ST_AsGeoJSON(ST_Centroid(p.geom)) centroid from search_province p")
    List<Province> queryAllProvince();

    @Select("select p.gid,p.name,st_xmin(p.geom) min_x,st_ymin(p.geom) min_y,st_xmax(p.geom) max_x,st_ymax(p.geom) max_y,ST_AsGeoJSON(ST_Envelope(p.geom)) envelope,ST_AsGeoJSON(ST_Centroid(p.geom)) centroid from search_city p")
    List<City> queryAllCity();

    @Select("select p.gid,p.name,st_xmin(p.geom) min_x,st_ymin(p.geom) min_y,st_xmax(p.geom) max_x,st_ymax(p.geom) max_y,ST_AsGeoJSON(ST_Envelope(p.geom)) envelope,ST_AsGeoJSON(ST_Centroid(p.geom)) centroid from search_town p")
    List<Town> queryAllTown();

    /**
     * 根据行政区名称查询位置
     */
    @Select({"SELECT c.gid,c.name,c.rectangle,c.geometry FROM search_country c WHERE c.name LIKE CONCAT(#{name},'%')"})
    List<Country> findAllCountryGeo(String name);

    @Select({"SELECT p.gid,p.name,p.rectangle,p.geometry FROM search_province p WHERE p.name LIKE CONCAT(#{name},'%')"})
    List<Province> findAllProvinceGeo(String name);

    @Select({"SELECT c.gid,c.name,c.rectangle,c.geometry FROM search_city c WHERE c.name LIKE CONCAT(#{name},'%')"})
    List<City> findAllCityGeo(String name);

    @Select({"SELECT t.gid,t.name,t.rectangle,t.geometry FROM search_town t WHERE t.name LIKE CONCAT(#{name},'%')"})
    List<Town> findAllTownGeo(String name);

    /**
     * 单个查询
     */
    @Select({"SELECT c.gid,c.name,c.rectangle,c.geometry FROM search_country c WHERE c.gid = #{gid}"})
    Country findOneCountry(int gid);

    @Select({"SELECT p.gid,p.name,p.rectangle,p.geometry FROM search_province p WHERE p.gid = #{gid}"})
    Province findOneProvince(int gid);

    @Select({"SELECT c.gid,c.name,c.rectangle,c.geometry FROM search_city c WHERE c.gid = #{gid}"})
    City findOneCity(int gid);

    @Select({"SELECT t.gid,t.name,t.rectangle,t.geometry FROM search_town t WHERE t.gid = #{gid}"})
    Town findOneTown(int gid);

}
