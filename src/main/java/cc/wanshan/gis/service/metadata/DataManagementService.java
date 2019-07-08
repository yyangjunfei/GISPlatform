package cc.wanshan.gis.service.metadata;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.metadata.metadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DataManagementService {

    Result metadataImport(String jsonString, MultipartFile[] file);

    metadata shpData2Geoserver(int id);

    List<metadata> findLayerProperties();

    Result deleteLayerPropertiesData(int id);

    Result editLayerPropertiesData(metadata metadata);

    List<metadata> findLayerPropertiesData(metadata metadata);

    Result changePublicationStatus(int id);

}
