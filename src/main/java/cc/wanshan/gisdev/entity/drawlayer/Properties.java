package cc.wanshan.gisdev.entity.drawlayer;

import lombok.Data;

import java.io.Serializable;
@Data
public class Properties implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String fclass;
    private String name;
    private String osmId;

    public Properties() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFclass() {
        return fclass;
    }

    public void setFclass(String fclass) {
        this.fclass = fclass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOsmId() {
        return osmId;
    }

    public void setOsmId(String osmId) {
        this.osmId = osmId;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "code=" + code +
                ", fclass='" + fclass + '\'' +
                ", name='" + name + '\'' +
                ", osmId='" + osmId + '\'' +
                '}';
    }
}
