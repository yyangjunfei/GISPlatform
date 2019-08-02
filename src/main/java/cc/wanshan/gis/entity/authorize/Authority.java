package cc.wanshan.gis.entity.authorize;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Data;
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

  public Authority(String authorName, String url,Date insertTime,Date updateTime
  ) {
    this.authorName = authorName;
    this.url = url;
    this.insertTime=insertTime;
    this.updateTime=updateTime;
  }

  public Authority(String authorId, String url, String authorName,Date insertTime,Date updateTime) {
    this.authorId = authorId;
    this.url = url;
    this.authorName = authorName;
    this.insertTime=insertTime;
    this.updateTime=updateTime;
  }


  private String authorId;

  private String url;
  private String authorName;
  private Date insertTime;
  private Date updateTime;
  private List<Role> roleList;

}

