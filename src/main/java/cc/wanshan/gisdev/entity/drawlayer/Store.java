package cc.wanshan.gisdev.entity.drawlayer;


import cc.wanshan.gisdev.entity.usermanagement.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

//@Data
public class Store implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "存储点id不可为null")
    @Length(max = 32, message = "长度最多32位")
    private String storeId;
    @NotBlank(message = "存储点不可为null")
    @Length(max = 32, message = "长度最多32位")
    private String storeName;
    @NotNull(message = "用户id不为null")
    private User user;
    @NotNull(message ="插入时间不可为null")
    @Past(message = "插入日期必须为过去时间")
    private Date insertTime;
    @NotNull(message = "修改时间不可为null")
    @Past(message = "修改日期必须为过去时间")
    private Date updateTime;
    private List<Layer> layerList;

    public Store() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<Layer> getLayerList() {
        return layerList;
    }

    public void setLayerList(List<Layer> layerList) {
        this.layerList = layerList;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeId='" + storeId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", user=" + user +
                ", insertTime=" + insertTime +
                ", updateTime=" + updateTime +
                ", layerList=" + layerList +
                '}';
    }
}
