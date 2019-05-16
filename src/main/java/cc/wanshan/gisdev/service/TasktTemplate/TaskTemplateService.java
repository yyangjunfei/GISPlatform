package cc.wanshan.gisdev.service.TasktTemplate;

import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.entity.TasktTemplate.TaskTemplate;

public interface TaskTemplateService {

    Result save(TaskTemplate taskTemplate);

    Result findAll();

    Result findById(String id);

    Result update(TaskTemplate taskTemplate);

    void delete(String id);
}
