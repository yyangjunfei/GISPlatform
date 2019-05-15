package cc.wanshan.gisdev;

import cc.wanshan.gisdev.utils.GeoserverUtils;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GisDevApplication.class)
@WebAppConfiguration
public class GeoserverTest {
    private static final Logger logger= LoggerFactory.getLogger(GeoserverTest.class);
    @Test
    public void getLayers(){
        logger.info("getLayers::");
        RESTLayer layer = GeoserverUtils.manager.getReader().getLayer("NanZheng", "NZ_sd");
        logger.info("结果为"+layer.toString());
    }
}
