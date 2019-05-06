package cc.wanshan.gisdev.entity.TasktTemplate;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Entity
@Table
@Data
public class TaskTemplate implements Serializable {

    private static final long serialVersionUID = 316298665259121990L;

    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "任务模板名称")
    private String taskTemplateName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "task_template_id")
    @ApiModelProperty(value = "任务模板节点列表")
    private List<TaskTemplateNode> taskTemplateNodeList = Lists.newArrayList();
}
