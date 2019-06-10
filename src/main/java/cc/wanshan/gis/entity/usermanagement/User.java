
package cc.wanshan.gis.entity.usermanagement;
import cc.wanshan.gis.entity.drawlayer.Store;

import cc.wanshan.gis.entity.thematic.Thematic;
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
 * user实体类
 *
 * @Author Li Cheng
 * @Date 14:43 2019/5/18
 **/
@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)	//注解控制null不序列化
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  @NotBlank(message = "用户id不可为null")
  @Length(max = 32, message = "长度最多32位")
  private String userId;
  @NotBlank(message = "用户名不可为null")
  @Length(max = 24, message = "长度最多24位")
  private String username;
  @NotBlank(message = "密码不可为null")
  @Length(min = 6, message = "长度最少6位")
  private String password;
  @Length(max = 32, message = "号码长度不可超过32字节")
  private String phoneNum;
  @Length(max = 32, message = "email长度不可超过32字节")
  private String email;
  @Length(max = 32, message = "部门名称长度不可超过32字节")
  private String department;
  private Thematic thematic;
  @NotBlank(message = "保密级别不可为null")
  @Length(max = 32, message = "保密级别长度不可超过32字节")
  private String security;
  @NotNull(message = "插入时间不可为null")
  @Past(message = "插入日期必须为过去时间")
  private Date insertTime;
  @NotNull(message = "修改时间不可为null")
  @Past(message = "修改日期必须为过去时间")
  private Date updateTime;
  private Integer status;
  private Integer delete;
  private Role role;
  private List<Store> storeList;


}
