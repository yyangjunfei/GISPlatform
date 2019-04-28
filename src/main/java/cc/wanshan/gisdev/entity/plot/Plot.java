package cc.wanshan.gisdev.entity.plot;

import cc.wanshan.gisdev.common.constants.Constant;
import cc.wanshan.gisdev.common.factory.PlotFactory;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Geometry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;

@Data
public class Plot {

  @Id
  @GeneratedValue
  @ApiModelProperty(value = "主键")
  private String id;

  @ApiModelProperty(value = "类型")
  private String type;

  @ApiModelProperty(value = "图层主键")
  private String layerId;

  @ApiModelProperty(value = "状态")
  private Integer status;

  @ApiModelProperty(value = "颜色")
  private String color;

  @ApiModelProperty(value = "样式")
  private String style;

  @ApiModelProperty(value = "宽度")
  private Integer width;

  @ApiModelProperty(value = "图片")
  private String picture;

  @ApiModelProperty(value = "空间坐标")
  Geometry geometry;

  @ApiModelProperty(value = "超链接")
  private String hyperlink;

  @ApiModelProperty(value = "音频")
  private String audio;

  @ApiModelProperty(value = "视频")
  private String video;

  @ApiModelProperty(value = "符号")
  private String symbol;

  @ApiModelProperty(value = "创建者")
  private String createUser;

  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  @ApiModelProperty(value = "更新者")
  private String updateUser;

  @ApiModelProperty(value = "更新时间")
  private Date updateTime;

  @ApiModelProperty(value = "备注")
  private String remark;

  private static HashMap<String, PlotFactory<? extends Plot>> map = Maps.newHashMap();

  static {
    map.put(Constant.GEO_POINT, new PlotPoint());
    map.put(Constant.GEO_LINESTRING, new PlotLine());
    map.put(Constant.GEO_POLYGON, new PlotPolygon());
  }

  public static Plot create(JSONObject jsonObject) throws IOException {
    String type = jsonObject.getString(Constant.TYPE);
    if (null == map.get(type)) {
      return null;
    }
    Plot plot = map.get(type).create(jsonObject.toJSONString());
    plot.setType(type);
    plot.setStatus(0);
    return plot;
  }
}
