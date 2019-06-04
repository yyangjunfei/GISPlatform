package cc.wanshan.gis.entity.plot;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@Builder
@ApiModel(value = "标绘--正方体", description = "PlotCuboid")
public class PlotCuboid implements Serializable {

    private static final long serialVersionUID = 7231740578361428004L;

    @ApiModelProperty(value = "主键UUID")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "图层主键")
    private String layerId;

    @ApiModelProperty(value = "字体")
    private String font;

    @ApiModelProperty(value = "字体大小")
    private Integer fontSize;

    @ApiModelProperty(value = "字体颜色")
    private Integer fontColor;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "边缘线状态")
    private boolean isBorder;

    @ApiModelProperty(value = "边缘线宽度")
    private Integer borderWidth;

    @ApiModelProperty(value = "边缘线颜色")
    private String borderColor;

    @ApiModelProperty(value = "背景颜色")
    private String backgroundColor;

    @ApiModelProperty(value = "背景透明度")
    private double backgroundOpacity;

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
