package cc.wanshan.gisdev.entity.plot;

import cc.wanshan.gisdev.common.enums.FieldEnum;
import cc.wanshan.gisdev.common.factory.PlotFactory;
import cc.wanshan.gisdev.utils.GeotoolsUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.IOException;
import java.io.Serializable;

@Entity
@Table
@Data
public class PlotLine extends Plot implements Serializable, PlotFactory<Plot> {

  @Id
  @GeneratedValue
  @ApiModelProperty(value = "主键")
  private String id;

  @ApiModelProperty(value = "类型")
  private String type;

  @ApiModelProperty(value = "图层主键")
  private String layerId;

  @Override
  public PlotLine create(String jsonString) throws IOException {
    JSONObject jsonObject = JSON.parseObject(jsonString);
    PlotLine plotLine = JSON.parseObject(jsonString, PlotLine.class);
    String geoJson = jsonObject.getString(FieldEnum.geometry.name());
    if (null == geoJson || geoJson.isEmpty()) {
      return plotLine;
    }
    // json转化geometry
    plotLine.setGeometry(GeotoolsUtils.geoJson2Geometry(geoJson));
    plotLine.setStatus(0);
    return plotLine;
  }
}
