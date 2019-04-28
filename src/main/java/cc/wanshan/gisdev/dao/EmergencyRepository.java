package cc.wanshan.gisdev.dao;

import cc.wanshan.gisdev.entity.TasktTemplate.Emergency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EmergencyRepository
    extends JpaRepository<Emergency, String>, JpaSpecificationExecutor<Emergency> {}
