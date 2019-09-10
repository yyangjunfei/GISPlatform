package cc.wanshan.gis.service.metadata;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.common.PageBean;
import cc.wanshan.gis.entity.metadata.metadata;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DataManagementService {

    Result metadataImportPublication(String jsonString, MultipartFile[] file);

    metadata shpData2Geoserver(int id);

    PageBean<metadata> findLayerProperties(Integer pageNum, Integer pageSize);

    Result deleteLayerPropertiesData(Integer [] layerIds);

    Result editLayerPropertiesData(metadata metadata);

    List<metadata> findLayerPropertiesData(metadata metadata);

    Result changePublicationStatus(int release_flag,int id);

}
