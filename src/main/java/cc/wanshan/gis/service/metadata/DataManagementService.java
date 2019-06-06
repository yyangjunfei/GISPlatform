package cc.wanshan.gis.service.metadata;

import cc.wanshan.gis.entity.Result;
import org.springframework.web.multipart.MultipartFile;

public interface DataManagementService {

    Result metadataImport(String jsonString, MultipartFile[] file);
}
