package cc.wanshan.gis;

import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.service.layer.LayerService;
import cc.wanshan.gis.service.export.ExportService;
import java.io.IOException;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
/**
 * @author Li Cheng
 * @date 2019/6/14 16:37
 */
public class ShapeTest {
private static final Logger logger= LoggerFactory.getLogger(ShapeTest.class);
  @Resource(name = "exportServiceImpl")
  private ExportService exportServiceImpl;
  @Resource(name = "layerServiceImpl")
  private LayerService layerServiceImpl;
  @Test
  public void writeShape() throws Exception {
    logger.info("writeShape::");
    Layer layerByLayerId = layerServiceImpl.findLayerByLayerId("3bca215a90e911e9916a20040ff72212");
    Boolean xxx = exportServiceImpl.writeSHP(layerByLayerId);
    logger.info("writeShape::"+xxx);
  }
  @Test
  public void writeJson() throws IOException {
    logger.info("writeJson::");
    Layer layerByLayerId = layerServiceImpl.findLayerByLayerId("647c8606881f11e988d220040ff72212");
    Boolean xxx = exportServiceImpl.writeJSON(layerByLayerId);
    logger.info("writeJson::"+xxx);
  }
}
