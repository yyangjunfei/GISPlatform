package cc.wanshan.gis.entity.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "行政区--国家", description = "country")
public class Country {

    @ApiModelProperty(value = "自增主键")
    private int gid;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "空间位置")
    private Object geom;

    @ApiModelProperty(value = "空间数据")
    private String geometry;

    @ApiModelProperty(value = "边界")
    private String boundary;

    @ApiModelProperty(value = "边界范围")
    private String envelope;

    @ApiModelProperty(value = "包围形")
    private String rectangle;

    @ApiModelProperty(value = "最小经度")
    private String minX;

    @ApiModelProperty(value = "最小纬度")
    private String minY;

    @ApiModelProperty(value = "最大经度")
    private String maxX;

    @ApiModelProperty(value = "最大纬度")
    private String maxY;

    @ApiModelProperty(value = "备注")
    private String remarks;

}
