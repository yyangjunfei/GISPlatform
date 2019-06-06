package cc.wanshan.gis.entity.sercher;
public class City {

    private String name;
    private String count;

    public City() {
    }

    public City(String name, String count) {
        this.name = name;
        this.count = count;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setCount(String count) {
        this.count = count;
    }
    public String getCount() {
        return count;
    }

}