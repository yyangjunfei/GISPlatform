package cc.wanshan.gis.entity.drawlayer;

import java.io.Serializable;

public class Feature implements Serializable {
    private static final long serialVersionUID = 1L;
    private int geoId;
    private String geometry;
    private Properties properties;

    public Feature() {
    }


    public int getgeoId() {
        return geoId;
    }

    public void setgeoId(int geoId) {
        this.geoId = geoId;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "geoId=" + geoId +
                ", geometry='" + geometry + '\'' +
                ", properties=" + properties +
                '}';
    }
}
