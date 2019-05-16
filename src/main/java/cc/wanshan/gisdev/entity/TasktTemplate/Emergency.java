package cc.wanshan.gisdev.entity.TasktTemplate;

import cc.wanshan.gisdev.common.enums.FieldEnum;
import cc.wanshan.gisdev.common.factory.EmergencyFactory;
import cc.wanshan.gisdev.utils.GeotoolsUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Geometry;
import lombok.Data;

import java.io.IOException;
import java.util.Date;

@Data
public class Emergency implements EmergencyFactory<Emergency> {

    private Long id;

    private Date createTime;

    private String emergencyName;

    private Object geometry;

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