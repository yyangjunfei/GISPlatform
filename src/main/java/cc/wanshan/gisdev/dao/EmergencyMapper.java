package cc.wanshan.gisdev.dao;

import cc.wanshan.gisdev.entity.TasktTemplate.Emergency;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface EmergencyMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Emergency record);

    int insertSelective(Emergency record);

    Emergency selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Emergency record);

    int updateByPrimaryKey(Emergency record);

    List<Emergency> findAll();

}