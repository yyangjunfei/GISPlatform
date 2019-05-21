package cc.wanshan.gis.entity.tasktemplate;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
@ApiModel(value = "任务模板", description = "TaskTemplate")
public class TaskTemplate implements Serializable {

    private static final long serialVersionUID = 316298665259121990L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "任务模板名称")
    private String taskTemplateName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private List<TaskTemplateNode> taskTemplateNodeList = Lists.newArrayList();
}
