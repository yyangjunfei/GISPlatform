package cc.wanshan.gis.controller.ProjTransform;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.service.ProjTransform.ProjTransformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/***
 * @author  Yang
 * @date   2019-9-2
 * @version [v1.0]
 * @descriptionweb shp文件坐标转换
 */

@Api(value = "ProjTransformController", tags = "shp文件坐标转换")
@RestController
@RequestMapping("rest/projtransform")
@CrossOrigin
public class ProjTransformController {

    @Autowired
    private ProjTransformService projTransformService;

    @ApiOperation(value = "shp文件Wgs84ToGcj02至DB", notes = "shp文件Wgs84ToGcj02至DB")
    @PostMapping("/tranfeWgs84ToGcj02ToDB")
    public Result tranfeWgs84ToGcj02ToDB(@RequestParam MultipartFile[] shpFiles) {
       return projTransformService.tranfeWgs84ToGcj02ToDB(shpFiles);
    }


    @ApiOperation(value = "shp文件Wgs84ToGcj02至文件", notes = "shp文件Wgs84ToGcj02至文件")
    @PostMapping("/tranfeWgs84ToGcj02ToShpFile")
    public Result tranfeWgs84ToGcj02ToShpFile(@RequestParam ("shpFiles") MultipartFile[] shpFiles,@RequestParam ("destShpPath") String destShpPath){
       return projTransformService.tranfeWgs84ToGcj02ToShpFile(shpFiles,destShpPath);
    }

}
