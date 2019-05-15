package cc.wanshan.gisdev.entity.usermanagement;


import java.io.Serializable;
import java.util.List;


public class Authority implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer authorId;
    private String url;
    private String authorName;

    private List<Role> roleList;
    public Authority() {
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "authorId=" + authorId +
                ", url='" + url + '\'' +
                ", authorName='" + authorName + '\'' +
                '}';
    }
}

