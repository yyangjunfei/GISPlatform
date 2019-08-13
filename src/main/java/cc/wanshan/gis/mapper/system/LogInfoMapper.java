package cc.wanshan.gis.mapper.system;

import cc.wanshan.gis.entity.system.LogInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author renmaoyan
 */
@Mapper
@Component
public interface LogInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(LogInfo record);

    int insertSelective(LogInfo record);

    LogInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(LogInfo record);

    int updateByPrimaryKey(LogInfo record);
}