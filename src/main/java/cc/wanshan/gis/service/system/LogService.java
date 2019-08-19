package cc.wanshan.gis.service.system;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.system.LogInfo;

/**
 * @author renmaoyan
 * @date 2019/8/12
 * @description TODO
 */

public interface LogService {

    /**
     * 保存日志
     *
     * @param log
     * @return
     */
    Result insert(LogInfo log);
}
