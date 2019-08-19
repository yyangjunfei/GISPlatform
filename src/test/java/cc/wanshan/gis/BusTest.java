package cc.wanshan.gis;

import cc.wanshan.gis.dao.road.NanJingLines2Dao;
import cc.wanshan.gis.dao.road.NanJingStationsDao;
import cc.wanshan.gis.dao.road.RoadDao;
import cc.wanshan.gis.entity.road.Road;
import cc.wanshan.gis.entity.road.Stations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Li Cheng
 * @date 2019/6/12 17:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BusTest {
    private static final Logger logger = LoggerFactory.getLogger(BusTest.class);
    @Resource
    private NanJingLines2Dao nanJingLines2Dao;
    @Resource
    private NanJingStationsDao nanJingStationsDao;
    @Resource
    private RoadDao roadDao;
    @Test
    public void findSource() {
        logger.info("findSource::");
        Road source = nanJingLines2Dao.findSource("point(118.7917 32.05777)");
        logger.info("findSource::" + source.toString());
    }

    @Test
    public void findTarget() {
        logger.info("findTarget::");
        Road target = nanJingLines2Dao.findTarget("point(118.7917 32.05777)");
        logger.info("findTarget::" + target.toString());
    }

    @Test
    public void findStation() {
        logger.info("findStation::");
        Stations station = nanJingStationsDao.findStation("point(118.7917 32.05777)");
        logger.info("findStation::" + station.toString());
    }
    @Test
    public void split() {
        logger.info("split::");
        String url="/find/road/point(111 111)";
        String[] split = url.split("/");
        System.out.println(split);
    }
    @Test
    public void findRoad() {
        logger.info("findRoad::");
        String road = roadDao.findRoad();
        System.out.println("road::"+road);
    }

    @Test
    public void update() {
        logger.info("findRoad::");
        int road = roadDao.update(123, 456);
        System.out.println("road::"+road);
    }
}
