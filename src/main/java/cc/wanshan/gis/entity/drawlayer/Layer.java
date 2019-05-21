package cc.wanshan.gis.entity.drawlayer;


import cc.wanshan.gisdev.entity.thematic.Thematic;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;
import org.hibernate.validator.constraints.Length;

@Data
public class Layer implements Serializable {

  private static final long serialVersionUID = 1L;
  @NotBlank(message = "图层Id不可为null")
  @Length(max = 32,message = "图层Id长度不可超过32字节")
  private String layerId;
  @NotBlank(message = "图层名不为null")
  @Length(max = 32,message = "图层名长度不可超过32字节")
  private String layerName;
  @NotBlank(message = "图层中文名不为null")
  @Length(max = 32,message = "图层中文名长度不可超过32字节")
  private String layerNameZH;
  @NotBlank(message = "存储点不可为null")
  private Store store;
  @NotBlank(message = "用户Id不可为null")
  private String userId;
  @NotNull(message = "专题不可为null")
  private Thematic thematic;
  @NotBlank(message = "保密级别不可为null")
  @Length(max = 32,message = "保密级别长度不可超过32字节")
  private String security;
  @NotBlank(message = "第一分类不可为null")
  @Length(max = 32,message = "第一分类长度不可超过32字节")
  private String firstClassification;
  @NotBlank(message = "第二分类不可为null")
  @Length(max = 32,message = "第二分类不可超过32字节")
  private String secondClassification;
  @NotNull(message = "发布时间不可为null")
  @Past(message = "修改时间必须为过去时间")
  private Date publishTime;
  @NotNull(message = "修改时间不可为null")
  @Past(message = "修改时间必须为过去时间")
  private Date updateTime;
  @NotNull(message = "上传时间不可为null")
  @Past(message = "上传时间必须为过去时间")
  private Date uploadTime;
  @NotBlank(message = "图层类型不可为null")
  @Length(max = 16,message = "图层类型长度不可超过16字节")
  private String type;
  @NotBlank(message = "坐标系不可为null")
  @Length(max = 4,message = "坐标系长度不可超过4字节")
  private String epsg;
}
