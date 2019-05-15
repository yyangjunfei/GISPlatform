package cc.wanshan.gisdev.entity.usermanagement;
import cc.wanshan.gisdev.entity.drawlayer.Store;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String username;
    private String password;
    private Date insertTime;
    private Date updateTime;
    private Integer status;
    private Integer delete;
    private Role role;
    private List<Store> storeList;
    public User() {
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelete() {
        return delete;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", insertTime=" + insertTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                ", delete=" + delete +
                ", role=" + role +
                ", storeList=" + storeList +
                '}';
    }
}
