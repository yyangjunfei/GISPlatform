package cc.wanshan.gis.dao.search;

import cc.wanshan.gis.entity.area.City;
import cc.wanshan.gis.entity.area.Country;
import cc.wanshan.gis.entity.area.Province;
import cc.wanshan.gis.entity.area.Town;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SearchDao {

    /**
     * 查询所有（参数 name envelope）
     */
    @Select("select c.gid,c.name,c.envelope from country c")
    public List<Country> findAllCountry();

    @Select("select p.gid,p.name,p.envelope from province p")
    public List<Province> findAllProvince();

    @Select("select c.gid,c.name,c.envelope from city c")
    public List<City> findAllCity();

    @Select("select t.gid,t.name,t.envelope from town t")
    public List<Town> findAllTown();

    /**
     * 查询所有（关键参数）用于生成关键字段数据
     */
    @Select("select p.gid,p.name,st_xmin(p.geom) min_x,st_ymin(p.geom) min_y,st_xmax(p.geom) max_x,st_ymax(p.geom) max_y,ST_AsGeoJSON(ST_Envelope(p.geom)) envelope,ST_AsGeoJSON(ST_Centroid(p.geom)) centroid from country p")
    public List<Country> queryAllCountry();

    @Select("select p.gid,p.name,st_xmin(p.geom) min_x,st_ymin(p.geom) min_y,st_xmax(p.geom) max_x,st_ymax(p.geom) max_y,ST_AsGeoJSON(ST_Envelope(p.geom)) envelope,ST_AsGeoJSON(ST_Centroid(p.geom)) centroid from province p")
    public List<Province> queryAllProvince();

    @Select("select p.gid,p.name,st_xmin(p.geom) min_x,st_ymin(p.geom) min_y,st_xmax(p.geom) max_x,st_ymax(p.geom) max_y,ST_AsGeoJSON(ST_Envelope(p.geom)) envelope,ST_AsGeoJSON(ST_Centroid(p.geom)) centroid from city p")
    public List<City> queryAllCity();

    @Select("select p.gid,p.name,st_xmin(p.geom) min_x,st_ymin(p.geom) min_y,st_xmax(p.geom) max_x,st_ymax(p.geom) max_y,ST_AsGeoJSON(ST_Envelope(p.geom)) envelope,ST_AsGeoJSON(ST_Centroid(p.geom)) centroid from town p")
    public List<Town> queryAllTown();

    /**
     * 根据行政区名称查询位置
     */
    @Select({"SELECT c.gid,c.name,c.rectangle,c.geometry FROM country c WHERE c.name LIKE CONCAT(#{name},'%')"})
    public List<Country> findAllCountryGeo(String name);

    @Select({"SELECT p.gid,p.name,p.rectangle,p.geometry FROM province p WHERE p.name LIKE CONCAT(#{name},'%')"})
    public List<Province> findAllProvinceGeo(String name);

    @Select({"SELECT c.gid,c.name,c.rectangle,c.geometry FROM city c WHERE c.name LIKE CONCAT(#{name},'%')"})
    public List<City> findAllCityGeo(String name);

    @Select({"SELECT t.gid,t.name,t.rectangle,t.geometry FROM town t WHERE t.name LIKE CONCAT(#{name},'%')"})
    public List<Town> findAllTownGeo(String name);

    /**
     * 单个查询
     */
    @Select({"SELECT c.gid,c.name,c.rectangle,c.geometry FROM country c WHERE c.gid = #{gid}"})
    public Country findOneCountry(int gid);

    @Select({"SELECT p.gid,p.name,p.rectangle,p.geometry FROM province p WHERE p.gid = #{gid}"})
    public Province findOneProvince(int gid);

    @Select({"SELECT c.gid,c.name,c.rectangle,c.geometry FROM city c WHERE c.gid = #{gid}"})
    public City findOneCity(int gid);

    @Select({"SELECT t.gid,t.name,t.rectangle,t.geometry FROM town t WHERE t.gid = #{gid}"})
    public Town findOneTown(int gid);

}
