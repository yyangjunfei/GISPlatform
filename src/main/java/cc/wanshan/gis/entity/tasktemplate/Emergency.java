package cc.wanshan.gis.entity.tasktemplate;

import cc.wanshan.gis.common.enums.FieldEnum;
import cc.wanshan.gis.common.factory.EmergencyFactory;
import cc.wanshan.gis.utils.GeotoolsUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Geometry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;

@Data
@ApiModel(value = "应急", description = "Emergency")
public class Emergency implements Serializable, EmergencyFactory<Emergency> {

    private static final long serialVersionUID = 6289834641092510857L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "应急名称")
    private String emergencyName;

    @ApiModelProperty(value = "空间位置")
    private Object geometry;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    @ApiModelProperty(value = "创建用户")
    private String createUser;

    @ApiModelProperty(value = "更新日期")
    private Date updateTime;

    @ApiModelProperty(value = "更新用户")
    private String updateUser;

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