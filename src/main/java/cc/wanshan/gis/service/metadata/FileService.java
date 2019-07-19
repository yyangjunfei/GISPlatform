package cc.wanshan.gis.service.metadata;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.metadata.ShpInfo;
import cc.wanshan.gis.entity.metadata.metadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface FileService {

    Result upload(MultipartFile file);

    Result upload(MultipartFile file, String folderPath);

    Result upload(List<MultipartFile> fileList);

    Result upload(List<MultipartFile> fileList, String folderPath);

    void deleteFile(File file);
    boolean  delFile(String filePath);
    List<ShpInfo> readSHP(String publishPath);

    void publishShpData2DB( List<ShpInfo> shpInfoList,metadata metadata);
}
