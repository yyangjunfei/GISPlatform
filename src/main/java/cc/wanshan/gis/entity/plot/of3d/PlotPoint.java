package cc.wanshan.gis.entity.plot.of3d;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@ApiModel(value = "标绘--点", description = "PlotPoint")
public class PlotPoint implements Serializable {

    private static final long serialVersionUID = 3795894752015309811L;

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

    @ApiModelProperty(value = "像素")
    private String pixel;

    @ApiModelProperty(value = "透明度")
    private double opacity;

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

    @ApiModelProperty(value = "备注")
    private String remarks;

}
