package cc.wanshan.gis.entity.usermanagement;

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
 * 角色实体类
 *
 * @Author Li Cheng
 * @Date 14:44 2019/5/18
 **/
@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)	//注解控制null不序列化
public class Role implements Serializable {

  private static final long serialVersionUID = 1L;
  @NotBlank(message = "角色id不可为null")
  @Length(max = 32, message = "角色id可超过32字节")
  private String roleId;
  @NotBlank(message = "角色名不可为null")
  @Length(min = 8, message = "长度最短为8位")
  private String roleName;
  @NotBlank(message = "角色中文名不为null")
  @Length(max = 24, message = "角色中文名不可超过24字节")
  private String roleNameZH;
  @NotNull(message = "插入时间不可为null")
  @Past(message = "插入日期必须为过去时间")
  private Date insertTime;
  @NotNull(message = "修改时间不可为null")
  @Past(message = "修改日期必须为过去时间")
  private Date updateTime;
  @Length(max = 100, message = "描述长度不可超过100字节")
  private String describe;
  private List<Authority> authorityList;
  private List<User> userList;
}
