package cc.wanshan.gisdev.entity.plot;

import cc.wanshan.gisdev.common.enums.FieldEnum;
import cc.wanshan.gisdev.common.factory.PlotFactory;
import cc.wanshan.gisdev.utils.GeotoolsUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Geometry;
import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "标绘面", description = "PlotPolygon")
public class PlotPolygon extends Plot implements Serializable, PlotFactory<Plot> {

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

    @ApiModelProperty(value = "边框颜色")
    private String borderColor;

    @ApiModelProperty(value = "边框宽度")
    private Integer borderWidth;

    @ApiModelProperty(value = "边框透明度")
    private Integer borderOpacity;

    @ApiModelProperty(value = "空间坐标")
    private Geometry geometry;

    @Override
    public PlotPolygon create(String jsonString) throws IOException {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        PlotPolygon plotPolygon = JSON.parseObject(jsonString, PlotPolygon.class);
        String geoJson = jsonObject.getString(FieldEnum.geometry.name());
        if (null == geoJson || geoJson.isEmpty()) {
            return plotPolygon;
        }
        // json转化geometry
        plotPolygon.setGeometry(GeotoolsUtils.geoJson2Geometry(geoJson));
        return plotPolygon;
    }
}
