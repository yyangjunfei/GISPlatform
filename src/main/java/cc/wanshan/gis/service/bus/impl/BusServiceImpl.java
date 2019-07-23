package cc.wanshan.gis.service.bus.impl;

import cc.wanshan.gis.dao.bus.NanJingLines2Dao;
import cc.wanshan.gis.dao.bus.NanJingStationsDao;
import cc.wanshan.gis.entity.bus.NanJingLines2;
import cc.wanshan.gis.entity.bus.NanJingStations;
import cc.wanshan.gis.service.bus.BusService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "busServiceImpl")
/**
 * @author Li Cheng
 * @date 2019/6/12 17:42
 */
public class BusServiceImpl implements BusService {

    @Resource
    private NanJingLines2Dao nanJingLines2Dao;
    @Resource
    private NanJingStationsDao nanJingStationsDao;

    @Override
    public Integer findSource(String sourcePoint) {
        NanJingLines2 source = nanJingLines2Dao.findSource(sourcePoint);
        if (source != null) {
            return source.getSource();
        }
        return null;
    }

    @Override
    public Integer findTarget(String targetPoint) {
        NanJingLines2 target = nanJingLines2Dao.findTarget(targetPoint);
        if (target != null) {
            return target.getTarget();
        }
        return null;
    }

    @Override
    public NanJingStations findStation(String point) {
        NanJingStations station = nanJingStationsDao.findStation(point);
        if (station != null) {
            return station;
        }
        return null;
    }
}
