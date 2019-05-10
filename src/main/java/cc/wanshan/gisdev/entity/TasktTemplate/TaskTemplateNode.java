package cc.wanshan.gisdev.entity.TasktTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table
@Data
@ApiModel(value = "任务模板节点", description = "TaskTemplateNode")
public class TaskTemplateNode implements Serializable {

    private static final long serialVersionUID = -786012220672885609L;

    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "任务模板节点名称")
    private String taskTemplateNodeName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "task_template_id")
    private TaskTemplate taskTemplate;
}
