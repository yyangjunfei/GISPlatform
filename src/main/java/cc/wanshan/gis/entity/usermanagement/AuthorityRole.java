package cc.wanshan.gis.entity.usermanagement;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * AuthorityRole实体类
 *
 * @Author Li Cheng
 * @Date 14:46 2019/5/18
 **/
@Data
public class AuthorityRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "权限角色中间表id不为null")
    @Length(max = 32, message = "权限角色中间表id长度最长为32字节")
    private String authorRoleId;
    @NotBlank(message = "权限id不为null")
    @Length(max = 32, message = "权限id长度最长为32字节")
    private String authorId;
    @NotBlank(message = "角色id不可为null")
    @Length(max = 32, message = "角色id长度最大为32字节")
    private String roleId;
}
