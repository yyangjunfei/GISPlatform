package cc.wanshan.gisdev.entity.drawlayer;

import java.io.Serializable;

public class Properties implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String fclass;
    private String name;
    private String osmId;

    public Properties() {
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

    public void setOsmID(String osmID) {
        this.osmId = osmId;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "code=" + code +
                ", fclass='" + fclass + '\'' +
                ", name='" + name + '\'' +
                ", osmID='" + osmId + '\'' +
                '}';
    }
}
