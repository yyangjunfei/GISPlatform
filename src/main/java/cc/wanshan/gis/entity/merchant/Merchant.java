package cc.wanshan.gis.entity.merchant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/***
 * @author  Yang
 * @date  2019-8-12
 * @version [v1.0]
 * @descriptionweb TODO
 */

@Data
public class Merchant implements Serializable {

    private static final long serialVersionUID = -3452237825L;

    @ApiModelProperty(value = "商户Id")
    private Long Id;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "geoType")
    private String geoType;

    @ApiModelProperty(value = "geometry")
    private String geometry;

    @ApiModelProperty(value = "商户经度")
    private String longitude;

    @ApiModelProperty(value = "商户纬度")
    private String latitude;

    @ApiModelProperty(value = "商户位置")
    private String address;

    @ApiModelProperty(value = "商户电话")
    private String telephone;

    @ApiModelProperty(value = "商户名称描述")
    private String PositionDescription;
}
