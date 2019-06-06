package cc.wanshan.gis.entity.sercher;
import java.util.List;

public class JsonRootBean {

    private Province province;
    private List<City> city;

    public JsonRootBean(Province province, List<City> city) {
        this.province = province;
        this.city = city;
    }

    public void setProvince(Province province) {
        this.province = province;
    }
    public Province getProvince() {
        return province;
    }
    public void setCity(List<City> city) {
        this.city = city;
    }
    public List<City> getCity() {
        return city;
    }

}