package cc.wanshan.gis.entity.drawlayer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)	//注解控制null不序列化
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
