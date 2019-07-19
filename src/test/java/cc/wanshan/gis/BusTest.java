package cc.wanshan.gis;

import cc.wanshan.gis.dao.bus.NanJingLines2Dao;
import cc.wanshan.gis.dao.bus.NanJingStationsDao;
import cc.wanshan.gis.entity.bus.NanJingLines2;
import cc.wanshan.gis.entity.bus.NanJingStations;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Li Cheng
 * @date 2019/6/12 17:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BusTest {
private static final Logger logger= LoggerFactory.getLogger(BusTest.class);
  @Resource
  private NanJingLines2Dao nanJingLines2Dao;
  @Resource
  private NanJingStationsDao nanJingStationsDao;
  @Test
  public void findSource(){
    logger.info("findSource::");
    NanJingLines2 source = nanJingLines2Dao.findSource("point(118.7917 32.05777)");
    logger.info("findSource::"+source.toString());
  }
  @Test
  public void findTarget(){
    logger.info("findTarget::");
    NanJingLines2 target = nanJingLines2Dao.findTarget("point(118.7917 32.05777)");
    logger.info("findTarget::"+target.toString());
  }
  @Test
  public void findStation(){
    logger.info("findStation::");
    NanJingStations station = nanJingStationsDao.findStation("point(118.7917 32.05777)");
    logger.info("findStation::"+station.toString());
  }
}
