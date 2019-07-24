package cc.wanshan.gis.entity.layer.thematic;

import cc.wanshan.gis.entity.authorize.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/5/18 14:30
 */
@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)    //注解控制null不序列化
public class Thematic<layer> implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "专题id不可为null")
    @Length(max = 32, message = "专题id长度不可超过32字节")
    private String thematicId;
    @NotBlank(message = "专题名不可为null")
    @Length(max = 20, message = "专题名长度不可超过24字节")
    private String thematicName;
    @NotBlank(message = "专题中文名不可为null")
    @Length(max = 32, message = "专题中文名长度不可超过32字节")
    private String thematicNameZH;
    @NotNull(message = "插入时间不可为null")
    @Past(message = "插入日期必须为过去时间")
    private Date insertTime;
    @NotNull(message = "修改时间不可为null")
    @Past(message = "修改日期必须为过去时间")
    private Date updateTime;
    @Length(max = 32, message = "描述长度不可超过100字节")
    private String describe;
    private List<User> userList;
    private List<FirstClassification> firstClassificationList;
}
