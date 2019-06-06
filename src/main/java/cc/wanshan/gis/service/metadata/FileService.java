package cc.wanshan.gis.service.metadata;

import cc.wanshan.gis.entity.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    Result upload(MultipartFile file);

    Result upload(MultipartFile file, String folderPath);

    Result upload(List<MultipartFile> fileList);

    Result upload(List<MultipartFile> fileList, String folderPath);

    String delFile(String path);
}
