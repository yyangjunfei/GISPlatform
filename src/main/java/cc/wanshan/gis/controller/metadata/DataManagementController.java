package cc.wanshan.gis.controller.metadata;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.service.metadata.DataManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "DataManagementController", tags = "数据管理接口")
@RestController
@RequestMapping("/rest/data")
public class DataManagementController {

    private static Logger LOG = LoggerFactory.getLogger(DataManagementController.class);

    @Autowired
    private DataManagementService dataManagementService;

    @ApiOperation(value = "元数据导入", notes = "metadataImport,jsonString字段（type类型）")
    @PostMapping("/import")
    @ApiImplicitParams(@ApiImplicitParam(name = "jsonString", value = "数据", required = false))

    public Result metadataImport(@RequestParam String jsonString, @RequestParam MultipartFile[] file) {
        LOG.info("metadataImport::jsonString = [{}]", jsonString);
        return dataManagementService.metadataImport(jsonString, file);
    }

}
