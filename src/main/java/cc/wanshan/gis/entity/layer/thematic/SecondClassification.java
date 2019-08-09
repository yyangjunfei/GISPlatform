package cc.wanshan.gis.entity.layer.thematic;

import cc.wanshan.gis.entity.plot.of2d.Layer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Li Cheng
 * @date 2019/5/23 16:04
 */
@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)    //注解控制null不序列化
@ApiModel(value = "SecondClassification类",description = "图层第二分类")
public class SecondClassification {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id",required = true)
    private String secondClassificationId;
    @ApiModelProperty(value = "第一分类实体类",required = true)
    private FirstClassification firstClassification;
    @ApiModelProperty(value = "名称",required = true)
    private String secondClassificationName;
    @ApiModelProperty(value = "下属图层实体类",required = true)
    private Layer layer;
    @ApiModelProperty(value = "描述信息")
    private String describe;
    @ApiModelProperty(value = "插入时间",required = true)
    private Date insertTime;
    @ApiModelProperty(value = "修改时间",required = true)
    private Date updateTime;
}
