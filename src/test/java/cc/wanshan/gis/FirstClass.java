package cc.wanshan.gis;

import cc.wanshan.gis.entity.layer.thematic.FirstClassification;
import cc.wanshan.gis.entity.layer.thematic.Thematic;
import cc.wanshan.gis.service.layer.thematic.FirstClassificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Li Cheng
 * @date 2019/5/25 14:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FirstClass {

    private static final Logger logger = LoggerFactory.getLogger(FirstClass.class);
    private FirstClassification firstClassification = new FirstClassification();
    private Thematic thematic = new Thematic();
    @Resource
    private FirstClassificationService firstClassificationService;

    @Test
    public void insertFirstClass() {
        logger.info("insertFirstClass::");
        thematic.setThematicId("52ffd62e7c7311e9a07b20040ff72212");
        firstClassification.setFirstClassificationName("植被");
        firstClassification.setThematic(thematic);
        firstClassification.setInsertTime(new Date());
        firstClassification.setUpdateTime(new Date());
        Boolean aBoolean = firstClassificationService.insertFirstClassification(firstClassification);
        logger.info("insertFirstClass::" + aBoolean);
    }
}
