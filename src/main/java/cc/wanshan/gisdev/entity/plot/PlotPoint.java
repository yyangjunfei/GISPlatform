package cc.wanshan.gisdev.entity.plot;

import cc.wanshan.gisdev.common.enums.FieldEnum;
import cc.wanshan.gisdev.common.factory.PlotFactory;
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

@Entity
@Table
@Data
public class PlotPoint extends Plot implements Serializable, PlotFactory<Plot> {

    @Id
    @GeneratedValue
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "图层主键")
    private String layerId;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "样式")
    private String style;

    @ApiModelProperty(value = "宽度")
    private Integer width;

    @ApiModelProperty(value = "透明度")
    private Integer opacity;

    @ApiModelProperty(value = "符号")
    private String symbol;

    @ApiModelProperty(value = "空间坐标")
    private Geometry geometry;

    @Override
    public Plot create(String jsonString) throws IOException {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        PlotPoint plotPoint = JSON.parseObject(jsonString, PlotPoint.class);
        String geoJson = jsonObject.getString(FieldEnum.geometry.name());
        if (null == geoJson || geoJson.isEmpty()) {
            return plotPoint;
        }
        // json转化geometry
        plotPoint.setGeometry(GeotoolsUtils.geoJson2Geometry(geoJson));
        return plotPoint;
    }
}
