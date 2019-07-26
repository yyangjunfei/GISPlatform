package cc.wanshan.gis.entity.authorize;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * Authority实体类
 *
 * @Author Li Cheng
 * @Date 14:47 2019/5/18
 **/
@Data
@JsonIgnoreProperties(value = {"handler"})//排除mybatis懒加载json序列化中的异常
@JsonInclude(JsonInclude.Include.NON_NULL)    //注解控制null不序列化
public class Authority implements Serializable {

  private static final long serialVersionUID = 1L;

  public Authority() {
  }

  public Authority(
      @NotBlank(message = "权限名不为null") String authorName,
      @NotBlank(message = "url不为null") @Length(max = 32, message = "url最大长度不可超过32位") String url,Date insertTime,Date updateTime
  ) {
    this.authorName = authorName;
    this.url = url;
    this.insertTime=insertTime;
    this.updateTime=updateTime;
  }

  public Authority(
      @NotBlank(message = "权限id不为null") @Length(max = 32, message = "权限id长度不可超过32字节") String authorId,
      @NotBlank(message = "url不为null") @Length(max = 32, message = "url最大长度不可超过32位") String url,
      @NotBlank(message = "权限名不为null") String authorName,Date insertTime,Date updateTime) {
    this.authorId = authorId;
    this.url = url;
    this.authorName = authorName;
    this.insertTime=insertTime;
    this.updateTime=updateTime;
  }

  @NotBlank(message = "权限id不为null")
  @Length(max = 32, message = "权限id长度不可超过32字节")
  private String authorId;
  @NotBlank(message = "url不为null")
  @Length(max = 32, message = "url最大长度不可超过32位")
  private String url;
  @NotBlank(message = "权限名不为null")
  private String authorName;
  private Date insertTime;
  private Date updateTime;
  private List<Role> roleList;

}

