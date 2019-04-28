package cc.wanshan.gisdev.entity.TasktTemplate;

import com.vividsolutions.jts.geom.Geometry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table
@Data
public class Emergency {

  @Id
  @GeneratedValue
  @ApiModelProperty(value = "id")
  private Long id;

  @ApiModelProperty(value = "应急名称")
  private String emergencyName;

  @ApiModelProperty(value = "地理位置")
  private Geometry geometry;

  @ApiModelProperty(value = "创建时间")
  private Date createTime;
}
