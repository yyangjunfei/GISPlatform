package cc.wanshan.gis.entity.style;

import cc.wanshan.gis.entity.drawlayer.Layer;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;


@Data
public class Style implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "风格id不为null")
    @Length(max = 32,message = "风格id长度不可超过32字节")
    private String styleId;
    @NotBlank(message = "风格名不为null")
    @Length(max = 32,message = "风格名长度不可超过32字节")
    private String styleName;
    @NotNull(message ="插入时间不可为null")
    @Past(message = "插入日期必须为过去时间")
    private Date insertTime;
    @NotNull(message = "修改时间不可为null")
    @Past(message = "修改日期必须为过去时间")
    private Date updateTime;
    private List<RuleName> ruleNameList;
}
