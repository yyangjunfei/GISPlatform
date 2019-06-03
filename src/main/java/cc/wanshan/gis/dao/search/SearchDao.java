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

    //    @Select("select p.gid,p.name,st_xmin(p.geom) minX,st_ymin(p.geom) minY,st_xmax(p.geom) maxX,st_ymax(p.geom) maxY,ST_AsGeoJSON(ST_Envelope(p.geom)) envelope,ST_AsGeoJSON(p.geom) boundary from country p")
    @Select("select p.gid,ST_AsGeoJSON(ST_Boundary(p.geom)) boundary from country p")
    public List<Country> findCountryAll();

    @Select("select p.gid,ST_AsGeoJSON(ST_Boundary(p.geom)) boundary from province p")
    public List<Province> findProvinceAll();

    @Select("select p.gid,ST_AsGeoJSON(ST_Boundary(p.geom)) boundary  from city p")
    public List<City> findCityAll();

    @Select("select p.gid,ST_AsGeoJSON(ST_Boundary(p.geom)) boundary  from town p")
    public List<Town> findTownAll();

    @Select("select c.gid,c.name,c.envelope,c.rectangle from country c")
    public List<Country> findAllCountry();

    @Select("select p.gid,p.name,p.envelope,p.rectangle,p.boundary from province p")
    public List<Province> findAllProvince();

    @Select("select c.gid,c.name,c.envelope,c.rectangle from city c")
    public List<City> findAllCity();

    @Select("select t.gid,t.name,t.envelope,t.rectangle from town t")
    public List<Town> findAllTown();

    @Select({"SELECT c.gid,c.name,c.envelope,c.rectangle,c.geometry FROM country c WHERE c.gid = #{gid}"})
    public Country findOneCountry(int gid);

    @Select({"SELECT p.gid,p.name,p.envelope,p.rectangle,p.geometry FROM province p WHERE p.gid = #{gid}"})
    public Province findOneProvince(int gid);

    @Select({"SELECT c.gid,c.name,c.envelope,c.rectangle,c.geometry FROM city c WHERE c.gid = #{gid}"})
    public City findOneCity(int gid);

    @Select({"SELECT t.gid,t.name,t.envelope,t.rectangle,t.geometry FROM town t WHERE t.gid = #{gid}"})
    public Town findOneTown(int gid);

}
