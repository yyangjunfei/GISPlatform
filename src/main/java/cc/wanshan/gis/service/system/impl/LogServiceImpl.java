package cc.wanshan.gis.service.system.impl;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.system.LogInfo;
import cc.wanshan.gis.mapper.system.LogInfoMapper;
import cc.wanshan.gis.service.system.LogService;
import cc.wanshan.gis.utils.base.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author renmaoyan
 * @date 2019/8/12
 * @description 日志业务逻辑层
 */
@Slf4j
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogInfoMapper logInfoMapper;

    @Override
    public Result insert(LogInfo logInfo) {
        int insert = logInfoMapper.insert(logInfo);
        return ResultUtil.success(insert);
    }
}
