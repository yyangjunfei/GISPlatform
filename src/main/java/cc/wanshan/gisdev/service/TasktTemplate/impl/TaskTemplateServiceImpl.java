package cc.wanshan.gisdev.service.TasktTemplate.impl;

import cc.wanshan.gisdev.common.enums.ResultCode;
import cc.wanshan.gisdev.dao.TaskTemplateRepository;
import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.entity.TasktTemplate.TaskTemplate;
import cc.wanshan.gisdev.service.TasktTemplate.TaskTemplateService;
import cc.wanshan.gisdev.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TaskTemplateServiceImpl implements TaskTemplateService {

  private static Logger LOG = LoggerFactory.getLogger(TaskTemplateServiceImpl.class);

  @Resource private TaskTemplateRepository taskTemplateRepository;

  @Override
  public Result save(TaskTemplate taskTemplate) {

    if (null == taskTemplate) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    if (taskTemplateRepository.save(taskTemplate) != null) {
      return ResultUtil.success(taskTemplate);
    } else {
      return ResultUtil.error(ResultCode.SAVE_FAIL);
    }
  }

  @Override
  public Result findAll() {
    List<TaskTemplate> taskTemplateList = taskTemplateRepository.findAll();
    if (taskTemplateList != null) {
      return ResultUtil.success(taskTemplateList);
    } else {
      return ResultUtil.error(ResultCode.FIND_NULL);
    }
  }

  @Override
  public Result findById(String id) {
    TaskTemplate taskTemplate = taskTemplateRepository.getOne(id);
    if (taskTemplate != null) {
      return ResultUtil.success(taskTemplate);
    } else {
      return ResultUtil.error(ResultCode.FIND_NULL);
    }
  }

  @Override
  public Result update(TaskTemplate taskTemplate) {
    TaskTemplate taskTemplateDb = taskTemplateRepository.saveAndFlush(taskTemplate);
    if (taskTemplateDb != null) {
      return ResultUtil.success(taskTemplateDb);
    } else {
      return ResultUtil.error(ResultCode.UPDATE_FAIL);
    }
  }

  @Override
  public void delete(String id) {
    taskTemplateRepository.deleteById(id);
  }
}
