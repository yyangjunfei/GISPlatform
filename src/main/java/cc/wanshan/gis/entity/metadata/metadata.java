package cc.wanshan.gis.entity.metadata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@Builder
@ApiModel(value = "元数据", description = "metadata")
public class metadata implements Serializable {

    private static final long serialVersionUID = -345223785754239706L;

    @ApiModelProperty(value = "主键UUID")
    private String id;

    @ApiModelProperty(value = "工作区")
    private String workspaceName;

    @ApiModelProperty(value = "存储名称")
    private String storeName;

    @ApiModelProperty(value = "图层名称")
    private String layerName;

    @ApiModelProperty(value = "样式名称")
    private String styleName;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "边界")
    private String boundingBox;

    @ApiModelProperty(value = "密级")
    private String safetyLevel;

    @ApiModelProperty(value = "源路劲")
    private String sourcePath;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新日期")
    private Date updateTime;

    @ApiModelProperty(value = "更新者")
    private String updateBy;
}
