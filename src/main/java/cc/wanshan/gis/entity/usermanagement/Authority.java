package cc.wanshan.gis.entity.usermanagement;


import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
/**  Authority实体类
 * @Author Li Cheng
 * @Date 14:47 2019/5/18
 **/
@Data
public class Authority implements Serializable {

  private static final long serialVersionUID = 1L;
  @NotBlank(message = "权限id不为null")
  @Length(max = 32, message = "权限id长度不可超过32字节")
  private String authorId;
  @NotBlank(message = "url不为null")
  @Length(max = 32, message = "url最大长度不可超过32位")
  private String url;
  @NotBlank(message = "权限名不为null")
  private String authorName;
  private List<Role> roleList;

}

