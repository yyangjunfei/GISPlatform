/*
package cc.wanshan.gis;

import cc.wanshan.gis.dao.StyleDao;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.style.Style;
import java.util.Date;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
*/
/**
 * @author Li Cheng
 * @date 2019/5/21 18:38
 *//*

@RunWith(SpringRunner.class)
@SpringBootTest
public class StyleTest {

  private static final Logger logger = LoggerFactory.getLogger(StyleTest.class);
  @Resource
  private StyleDao styleDao;
  private Style style=new Style();
  private Layer layer =new Layer();
  @Test
  public void insertStyle() {
    logger.info("insertStyle::");
    style.setStyleName("星球大战");
    style.setInsertTime(new Date());
    style.setUpdateTime(new Date());
    layer.setLayerId("5dc242ec7bb811e9a01e20040ff72212");
    style.setLayer(layer);
    int i = styleDao.insertStyle(style);
    logger.info("insertStyle::"+i);
  }
  @Test
  public void findStyle() {
    logger.info("findStyle::");
    Style byStyleId = styleDao.findByStyleId("982c62d67bb911e9800220040ff72212");
    logger.info("findStyle::"+byStyleId.toString());
  }
}
*/
