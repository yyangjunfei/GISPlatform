package cc.wanshan.gis.entity.metadata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
@Builder
@ApiModel(value = "元数据", description = "metadata")
public class metadata implements Serializable {

    private static final long serialVersionUID = -345223785754239706L;

    @ApiModelProperty(value = "主键UUID")
    private Integer id;

    @ApiModelProperty(value = "工作区")
    private String workspaceName;

    @ApiModelProperty(value = "存储名称")
    private String storeName;

    @ApiModelProperty(value = "图层组")
    private String layerGroup;

    @ApiModelProperty(value = "数据类型")
    private String DataType;

    @ApiModelProperty(value = "图层名称")
    private String layerName;

    @ApiModelProperty(value = "保密级别")
    private String safetyLevel;

    @ApiModelProperty(value = "归属部门")
    private String attributionDepartment;

    @ApiModelProperty(value = "矢量类型")
    private String vectorTypes;

    @ApiModelProperty(value = "样式文件")
    private String styleName;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    @ApiModelProperty(value = "归属单位/创建者")
    private String createBy;

    @ApiModelProperty(value = "是否发布")
    private int release_flag;

    // 方便数据库操作

    @ApiModelProperty(value = "geo类型")
    private String geoType;

    @ApiModelProperty(value = "shp图层信息")
    private List<ShpInfo> shpInfoList;

}
