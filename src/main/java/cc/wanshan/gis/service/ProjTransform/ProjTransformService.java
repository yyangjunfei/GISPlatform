package cc.wanshan.gis.service.ProjTransform;
import cc.wanshan.gis.common.pojo.Result;
import org.springframework.web.multipart.MultipartFile;

/***
 * @author  Yang
 * @date    2019-9-3
 * @version [v1.0]
 * @descriptionweb shp文件坐标转换
 */
public interface ProjTransformService {

    Result tranfeWgs84ToGcj02ToDB(MultipartFile[] shpFiles);

    Result tranfeWgs84ToGcj02ToShpFile(MultipartFile[] shpFiles, String destShpPath);

    Result getUploadFilePathAndName(MultipartFile[] shpFiles);

    Result getUploadFile(MultipartFile[] shpFiles);
}
