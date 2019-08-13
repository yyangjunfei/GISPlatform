package cc.wanshan.gis.controller.merchant;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.merchant.Merchant;
import cc.wanshan.gis.service.layer.geoserver.GeoServerService;
import cc.wanshan.gis.service.merchant.MerchantService;
import cc.wanshan.gis.utils.base.ResultUtil;
import cc.wanshan.gis.utils.geo.GeoServerUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/***
 * @author  Yang
 * @date  2019-8-10
 * @version [v1.0]
 * @descriptionweb TODO
 */
@Api(value = "MerchantController", tags = "商户位置信息接口")
@RestController
@RequestMapping("/rest/merchant")
@Slf4j
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private GeoServerService geoServerService;

    @ApiOperation(value = "存储商户信息到数据库", notes = "存储商户信息到数据库")
    @PostMapping("/saveMerchantInfo")
    public Result saveMerchantInfo(@RequestParam("merchantJosn") String merchantJosn) {
        log.info("存储商户信息");
        Merchant merchant = JSONObject.parseObject(merchantJosn,Merchant.class);
        int saveFlag =merchantService.saveMerchantInfo(merchant);
        if (saveFlag !=0){
            return ResultUtil.success(200,"存储商户信息成功",merchant);
        }else{
            return ResultUtil.error(500,"存储商户信息失败");
        }
    }

    @ApiOperation(value = "发布商户信息到geoServer", notes = "发布商户信息到geoServer")
    @PostMapping("/merchant2Geoserver")
    public Result merchant2Geoserver(){
        log.info("发布商户信息到geoServer");
        String defaultStyle ="gree_point";  //定义风格
        Result rlsFlag = geoServerService.publishLayer("merchant", "merchant", "merchant", defaultStyle);
        if(rlsFlag.getCode()==200){
            return  ResultUtil.success(200,"发布商户信息成功");
        }else{
            return  ResultUtil.error(500,"发布商户信息失败");
        }
    }

    @ApiOperation(value = "删除geoServer发布的商户信息", notes = "删除geoServer发布的商户信息")
    @DeleteMapping("/deleteMerchantGeoserverLayer")
    public Result deleteMerchantGeoserverLayer(){
        log.info("删除geoServer发布的商户信息");
        boolean deleteFlag= GeoServerUtils.publisher.unpublishFeatureType("merchant","merchant","merchant");
        if(deleteFlag){
            return  ResultUtil.success(200,"删除geoserver发布的图层成功!");
        }else{
            return  ResultUtil.error(500,"删除geoserver发布的图层失败!");
        }
    }
}
