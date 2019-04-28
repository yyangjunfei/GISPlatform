package cc.wanshan.gisdev.dao;

import cc.wanshan.gisdev.entity.TasktTemplate.TaskTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TaskTemplateRepository
    extends JpaRepository<TaskTemplate, String>, JpaSpecificationExecutor<TaskTemplate> {}
