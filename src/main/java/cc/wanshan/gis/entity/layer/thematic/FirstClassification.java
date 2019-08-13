package cc.wanshan.gis.entity.layer.thematic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.List;


@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)    //注解控制null不序列化
@ApiModel(value = "FirstClassification类",description = "描述图层第一分类")
/**
 * @Description 图层第一分类
 * @author Li Cheng
 * @date 2019/5/23 16:04
 */
public class FirstClassification {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "Id")
    private String firstClassificationId;
    @ApiModelProperty(value = "名称",required = true)
    private String firstClassificationName;
    @ApiModelProperty(value ="归属专题",required = true)
    private Thematic thematic;
    @ApiModelProperty(value = "下属第二分类集合",required = true)
    private List<SecondClassification> secondClassificationList;
    @ApiModelProperty(value = "描述")
    private String describe;
    @ApiModelProperty(value ="插入时间",required = true)
    private Date insertTime;
    @ApiModelProperty(value = "更新时间",required = true)
    private Date updateTime;
}