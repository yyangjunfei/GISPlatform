package cc.wanshan.gisdev.controller.task;

import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.service.TasktTemplate.EmergencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "EmergencyController", tags = "EmergencyController")
@RestController
@EnableAutoConfiguration
@RequestMapping("/rest/emergency")
public class EmergencyController {

    private static Logger LOG = LoggerFactory.getLogger(EmergencyController.class);

    @Autowired
    private EmergencyService emergencyService;

    @ApiOperation(value = "应急创建", notes = "create 应急创建 举例：")
    @PostMapping
    public Result create(@RequestBody String jsonStr) {

        LOG.info("应急发布");
        return emergencyService.save(jsonStr);
    }

    @ApiOperation(value = "应急查询全部", notes = "findAll 应急查询全部")
    @GetMapping
    public Result findAll() {

        LOG.info("应急查询全部");
        return emergencyService.findAll();
    }

    @ApiOperation(value = "应急修改", notes = "update 任务模板修改 ")
    @PutMapping
    public Result update(@RequestBody String jsonStr) {

        LOG.info("应急修改");
        return emergencyService.update(jsonStr);
    }
}
