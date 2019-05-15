package cc.wanshan.gisdev.entity.usermanagement;

import java.io.Serializable;
import java.util.List;

public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer roleId;
    private String roleName;
    private String roleNameZH;
    private String  describe;

    private List<Authority> authorityList;

    private List<User> userList;
    public Role() {
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleNameZH() {
        return roleNameZH;
    }

    public void setRoleNameZH(String roleNameZH) {
        this.roleNameZH = roleNameZH;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public List<Authority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<Authority> authorityList) {
        this.authorityList = authorityList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", roleNameZH='" + roleNameZH + '\'' +
                ", describe='" + describe + '\'' +
                ", authorityList=" + authorityList +
                ", userList=" + userList +
                '}';
    }
}
