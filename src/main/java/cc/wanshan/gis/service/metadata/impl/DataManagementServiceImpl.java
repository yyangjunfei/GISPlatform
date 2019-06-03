package cc.wanshan.gis.service.metadata.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.metadata.metadata;
import cc.wanshan.gis.service.metadata.DataManagementService;
import cc.wanshan.gis.service.metadata.FileService;
import cc.wanshan.gis.utils.GeoserverUtils;
import cc.wanshan.gis.utils.ResultUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class DataManagementServiceImpl implements DataManagementService {

    private static final Logger LOG = LoggerFactory.getLogger(GeoserverUtils.class);

    @Autowired
    private FileService fileService;

    @Override
    public Result metadataImport(String jsonString, MultipartFile[] file) {

        LOG.info("metadataImport...");
        //本地文件上传服务器中
        Result uploadResult = fileService.upload(Arrays.asList(file), "test");

        List<Map<String, String>> data = (List<Map<String, String>>) uploadResult.getData();

        if (null == data || data.size() <= 0) {
            return ResultUtil.error(ResultCode.UPLOAD_FILE_NULL);
        }

        String filePath = data.get(0).get("filePath");

        metadata metadata = JSON.parseObject(jsonString, metadata.class);

        return ResultUtil.success(metadata);
    }
}
