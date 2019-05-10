package cc.wanshan.gisdev.controller.task; // package cc.wanshan.wsgisdev.controller.task;

import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.entity.TasktTemplate.TaskTemplate;
import cc.wanshan.gisdev.service.TasktTemplate.TaskTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "TaskTemplateController", tags = "任务模板接口")
@RestController
@EnableAutoConfiguration
@RequestMapping("/rest/task/template")
public class TaskTemplateController {

    private static Logger LOG = LoggerFactory.getLogger(TaskTemplateController.class);

    @Autowired
    private TaskTemplateService taskTemplateService;

    @ApiOperation(value = "任务模板创建", notes = "任务模板创建")
    @PostMapping
    public Result create(@RequestBody TaskTemplate taskTemplate) {

        LOG.info("任务模板创建");

        return taskTemplateService.save(taskTemplate);
    }

    @ApiOperation(value = "任务模板列表查询全部", notes = "任务模板列表查询全部")
    @GetMapping
    public Result findAll() {

        LOG.info("任务模板列表查询全部");

        return taskTemplateService.findAll();
    }

    @ApiOperation(value = "任务模板根据id查询", notes = "任务模板根据id查询")
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id) {

        LOG.info("任务模板根据id查询");

        return taskTemplateService.findById(id);
    }

    @ApiOperation(value = "任务模板修改", notes = "任务模板修改 ")
    @PutMapping
    public Result update(@RequestBody TaskTemplate taskTemplate) {

        LOG.info("任务模板修改");

        return taskTemplateService.update(taskTemplate);
    }

    @ApiOperation(value = "任务模板列表根据ID删除", notes = "任务模板列表根据ID删除 ")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {

        LOG.info("任务模板列表根据ID删除");

        taskTemplateService.delete(id);
    }
}
