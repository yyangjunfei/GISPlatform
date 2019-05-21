package cc.wanshan.gis.entity.tasktemplate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "任务模板节点", description = "TaskTemplateNode")
public class TaskTemplateNode implements Serializable {

    private static final long serialVersionUID = -786012220672885609L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "任务模板节点名称")
    private String taskTemplateNodeName;

    private TaskTemplate taskTemplate;
}
