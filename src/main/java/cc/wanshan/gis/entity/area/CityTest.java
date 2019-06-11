package cc.wanshan.gis.entity.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;

@Data
@ApiModel(value = "行政区--市", description = "city")
@Table(name = "C1_shjxzq")
public class CityTest {

    @ApiModelProperty(value = "自增主键")
    private int gid;

    @ApiModelProperty(value = "空间位置")
    private Object geom;

    @ApiModelProperty(value = "中心点")
    private String centroid;

    @ApiModelProperty(value = "边界范围")
    private String envelope;

    @ApiModelProperty(value = "bsm")
    private String bsm;
    @ApiModelProperty(value = "xzqddm")
    private String xzqddm;
    @ApiModelProperty(value = "备注")
    private String bz;
    @ApiModelProperty(value = "xzqdmc")
    private String xzqdmc;

}
