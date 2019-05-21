package cc.wanshan.gisdev;

import cc.wanshan.gisdev.dao.LayerDao;
import cc.wanshan.gisdev.dao.searchlayertabledao.SearchLayerTableDao;
import cc.wanshan.gisdev.dao.searchlayertabledao.impl.SearchLayerTableDaoImpl;
import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.entity.drawlayer.Layer;
import cc.wanshan.gisdev.entity.drawlayer.Store;
import cc.wanshan.gisdev.entity.thematic.Thematic;
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
 * @date 2019/5/20 11:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LayerTest {
  private static final Logger logger= LoggerFactory.getLogger(LayerTest.class);
  private Layer layer=new Layer();
  private Store store=new Store();
  @Resource
  private LayerDao layerDao;
  @Resource
  private SearchLayerTableDao searchLayerTableDao;
  @Test
  public void insertLayer(){
    logger.info("insertLayer::");
    Thematic thematic = new Thematic();
    thematic.setThematicId("434aa3f47aa111e98b1220040ff72212");
    thematic.setThematicName("nanZheng");
    thematic.setThematicNameZH("南郑");
    layer.setLayerNameZH("南郑道路");
    layer.setLayerName("nzdl");
    layer.setEpsg("4326");
    layer.setType("Point");
    layer.setFirstClassification("道路");
    layer.setSecondClassification("县道");
    layer.setSecurity("绝密");
    layer.setPublishTime(new Date());
    layer.setUpdateTime(new Date());
    layer.setUploadTime(new Date());
    store.setStoreId("cd1c16e67ab311e99b0a20040ff72212");
    layer.setStore(store);
    layer.setUserId("7ca5cf8e7ab011e98ce020040ff72212");
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
}
