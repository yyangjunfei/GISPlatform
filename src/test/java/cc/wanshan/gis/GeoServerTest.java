package cc.wanshan.gis;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.service.geoserver.GeoServerService;
import cc.wanshan.gis.utils.GeoServerUtils;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.decoder.RESTWorkspaceList;
import it.geosolutions.geoserver.rest.decoder.RESTWorkspaceList.RESTShortWorkspace;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GISPlatformApplication.class)
@WebAppConfiguration
public class GeoServerTest {
    private static final Logger logger = LoggerFactory.getLogger(GeoServerTest.class);
    @Resource(name ="geoServerServiceImpl")
    private GeoServerService geoServerServiceImpl;
    @Test
    public void getLayers() {
        logger.info("getLayers::");
        RESTLayer layer = GeoServerUtils.manager.getReader().getLayer("ceshi", "bd36ade41559030291105");
        logger.info("结果为" + layer.toString());
    }

    @Test
    public void getStore() {
        logger.info("getStore::");
        RESTWorkspaceList layer = GeoServerUtils.manager.getReader().getWorkspaces();
        for (RESTShortWorkspace restShortWorkspace : layer) {
            logger.info("结果为" + restShortWorkspace.toString());
        }
    }

    @Test
    public void searchLayer() {
        logger.info("searchLayer::");
        boolean layer = GeoServerUtils.manager.getReader()
                .existsLayer("ceshi", "bd36ade41559034275078", true);
        logger.info("searchLayer::" + layer);
    }

    @Test
    public void publishLayer() {
        logger.info("publishLayer::");
        Result result = geoServerServiceImpl.publishLayer("shpdb", "shpdb", "hanzhong");
        logger.info("searchLayer::" + result.toString());
    }
}
