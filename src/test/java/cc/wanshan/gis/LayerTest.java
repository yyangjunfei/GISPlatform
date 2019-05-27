package cc.wanshan.gis;

import cc.wanshan.gis.dao.LayerDao;
import cc.wanshan.gis.dao.searchlayertable.SearchLayerTableDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.drawlayer.Store;
import cc.wanshan.gis.entity.thematic.FirstClassification;
import cc.wanshan.gis.entity.thematic.Thematic;
import cc.wanshan.gis.service.layer.LayerService;
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
 * @Author Li Cheng
 * @Date 15:07 2019/5/23
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class LayerTest {
  private static final Logger logger= LoggerFactory.getLogger(LayerTest.class);
  private Layer layer=new Layer();
  private Store store=new Store();
  @Resource
  private LayerDao layerDao;
  @Resource
  private LayerService layerService;
  @Resource
  private SearchLayerTableDao searchLayerTableDao;
  @Test
  public void insertLayer(){
    logger.info("insertLayer::");
    Thematic thematic = new Thematic();
    thematic.setThematicId("4194542c7e0411e9b9dc20040ff72212");
    thematic.setThematicName("NanZheng1");
    thematic.setThematicNameZH("南郑专题地图");
    layer.setLayerNameZH("县道 ");
    layer.setLayerName("nz1_xjxzq");
    layer.setEpsg("3857");
    //layer.setType("Point");
    layer.setFirstClassificationName("境界与政区");
    layer.setFirstClassificationId("3daacf3e7ebe11e9831f20040ff72212");
    layer.setSecondClassificationName("县级行政区");
    layer.setSecondClassificationId("292a1974802211e9a58e20040ff72212");
    layer.setSecurity("公开");
    layer.setPublishTime(new Date());
    layer.setUpdateTime(new Date());
    layer.setUploadTime(new Date());
    //store.setStoreId("cd1c16e67ab311e99b0a20040ff72212");
    layer.setStore(store);
    //layer.setUserId("7ca5cf8e7ab011e98ce020040ff72212");
    layer.setThematic(thematic);
    int i = layerDao.insertLayer(layer);
    logger.info("insertLayer::"+layer.toString());
  }
  @Test
  public void findLayerByStoreId(){
    logger.info("findLayerByStoreId::");
    List<Layer> layersByStoreId = layerDao.findLayersByStoreId("cd1c16e67ab311e99b0a20040ff72212");
    for (Layer layer1 : layersByStoreId) {
      logger.info("findLayerByStoreId::"+layer1.toString());
    }
  }
  @Test
  public void searchTable(){
    logger.info("searchTable::");
    Result result = searchLayerTableDao.searchLayer("test", "nanZheng");
    logger.info("searchTable::"+result.getMsg());
  }
  @Test
  public void findLayerByThematicIdNullUserId(){
    logger.info("findLayerByThematicIdNullUserId::");
    List<FirstClassification> layers = layerService
        .findLayerByThematicIdAndNullUserId("52ffd62e7c7311e9a07b20040ff72212");
  }
}
