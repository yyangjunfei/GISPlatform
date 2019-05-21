package cc.wanshan.gis.entity.plot;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@Builder
@ApiModel(value = "标绘--线", description = "PlotLine")
public class PlotLine implements Serializable {

    private static final long serialVersionUID = 7231740578361428004L;

    @ApiModelProperty(value = "主键UUID")
    private String id;

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
    private String geom;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新日期")
    private Date updateTime;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

}
