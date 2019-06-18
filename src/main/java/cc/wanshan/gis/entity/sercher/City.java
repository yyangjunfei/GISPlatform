package cc.wanshan.gis.entity.sercher;
public class City {

    private String name;
    private String count;
    private String coordinates;
    public City() {
    }

    public City(String name, String count) {
        this.name = name;
        this.count = count;
    }

    public City(String name, String count, String coordinates) {
        this.name = name;
        this.count = count;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}