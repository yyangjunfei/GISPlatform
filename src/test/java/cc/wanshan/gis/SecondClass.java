package cc.wanshan.gis;

import cc.wanshan.gis.entity.thematic.FirstClassification;
import cc.wanshan.gis.entity.thematic.SecondClassification;
import cc.wanshan.gis.service.thematic.SecondClassificationService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Li Cheng
 * @date 2019/5/25 14:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SecondClass {
private static final Logger logger= LoggerFactory.getLogger(SecondClass.class);
  private FirstClassification firstClassification = new FirstClassification();
  private SecondClassification secondClassification = new SecondClassification();
  @Resource
  private SecondClassificationService secondClassificationService;
  @Test
  public void insertSecondClass(){
    logger.info("insertSecondClass::");
    firstClassification.setFirstClassificationId("3daacf3e7ebe11e9831f20040ff72212");
    secondClassification.setSecondClassificationName("镇级行政区");
    secondClassification.setInsertTime(new Date());
    secondClassification.setUpdateTime(new Date());
    secondClassification.setFirstClassification(firstClassification);
    Boolean aBoolean = secondClassificationService.insertClassification(secondClassification);
    logger.info("insertSecondClass::"+aBoolean);
  }
  @Test
  public void findByFirstClass(){
    logger.info("findByFirstClass::");
    List<SecondClassification> byFirstClass = secondClassificationService.findByFirstClass("4194542c7e0411e9b9dc20040ff72212");
    for (SecondClassification firstClass : byFirstClass) {
      logger.info("findByFirstClass::"+firstClass.toString());
    }
  }
}
