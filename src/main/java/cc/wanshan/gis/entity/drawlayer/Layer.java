package cc.wanshan.gis.entity.drawlayer;


import java.io.Serializable;


public class Layer implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer layerId;
    private String layerName;
    private Integer storeId;
    private String type;
    private String epsg;
    public Layer() {
    }


    public Integer getLayerId() {
        return layerId;
    }

    public void setLayerId(Integer layerId) {
        this.layerId = layerId;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEpsg() {
        return epsg;
    }

    public void setEpsg(String epsg) {
        this.epsg = epsg;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "layerId=" + layerId +
                ", layerName='" + layerName + '\'' +
                ", storeId=" + storeId +
                ", type='" + type + '\'' +
                ", epsg='" + epsg + '\'' +
                '}';
    }
}
