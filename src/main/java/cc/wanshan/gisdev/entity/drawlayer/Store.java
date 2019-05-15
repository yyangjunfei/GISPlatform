package cc.wanshan.gisdev.entity.drawlayer;


import cc.wanshan.gisdev.entity.usermanagement.User;

import java.io.Serializable;
import java.util.List;


public class Store implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer storeId;

    private String storeName;

    private User user;

    private List<Layer> layerList;
    public Store() {
    }


    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<Layer> getLayerList() {
        return layerList;
    }

    public void setLayerList(List<Layer> layerList) {
        this.layerList = layerList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", userPage=" + user +
                ", layerList=" + layerList +
                '}';
    }
}
