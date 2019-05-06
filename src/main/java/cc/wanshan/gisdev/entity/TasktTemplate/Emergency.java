package cc.wanshan.gisdev.entity.TasktTemplate;

import cc.wanshan.gisdev.common.enums.FieldEnum;
import cc.wanshan.gisdev.common.factory.EmergencyFactory;
import cc.wanshan.gisdev.utils.GeotoolsUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Geometry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table
@Data
public class Emergency implements Serializable, EmergencyFactory<Emergency> {

    private static final long serialVersionUID = -6216790072833036579L;

    @Id
    @GeneratedValue
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "应急名称")
    private String emergencyName;

    @ApiModelProperty(value = "地理位置")
    private Geometry geometry;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Override
    public Emergency create(String jsonString) throws IOException {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Emergency emergency = JSON.parseObject(jsonString, Emergency.class);
        String geoJson = jsonObject.getString(FieldEnum.geometry.name());
        if (null == geoJson || geoJson.isEmpty()) {
            return emergency;
        }
        Geometry geometry = GeotoolsUtils.geoJson2Geometry(geoJson);
        emergency.setGeometry(geometry);
        return emergency;
    }
}
