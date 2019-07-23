package cc.wanshan.gis.entity.plot.of2d;

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

@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)    //注解控制null不序列化
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotBlank(message = "存储点id不可为null")
    @Length(max = 32, message = "长度最多32位")
    private String storeId;
    @NotBlank(message = "存储点不可为null")
    @Length(max = 32, message = "长度最多32位")
    private String storeName;
    @NotNull(message = "用户id不为null")
    private User user;
    @NotNull(message = "插入时间不可为null")
    @Past(message = "插入日期必须为过去时间")
    private Date insertTime;
    @NotNull(message = "修改时间不可为null")
    @Past(message = "修改日期必须为过去时间")
    private Date updateTime;
    private List<Layer> layerList;

}
