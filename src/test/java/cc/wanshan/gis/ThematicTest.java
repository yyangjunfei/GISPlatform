package cc.wanshan.gis;

import cc.wanshan.gis.dao.ThematicDao;
import cc.wanshan.gis.entity.thematic.Thematic;
import cc.wanshan.gis.service.thematic.ThematicService;
import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ThematicTest {
  private static final Logger logger= LoggerFactory.getLogger(ThematicTest.class);
  @Resource
  private ThematicDao thematicDao;
  @Resource
  private ThematicService thematicService;
  @Test
  public void insertThematic(){
    Thematic thematic = new Thematic();
    thematic.setThematicName("NanZheng1");
    thematic.setThematicNameZH("南郑专题地图");
    thematic.setDescribe("南郑专题地图");
    thematic.setInsertTime(new Date());
    thematic.setUpdateTime(new Date());
    Boolean i = thematicService.insertThematic(thematic);
    logger.info("insertThematic::"+i);
    /*if(i){
      logger.info("insertThematic::开始创建工作空间");
      boolean nanZheng = GeoserverUtils.manager.getPublisher().createWorkspace("ceshi");
      logger.info("insertThematic::"+nanZheng);
    }
*/
  }
  @Test
  public void findThematicByThematicName(){
    Thematic nz = thematicDao.findThematicByThematicNameZH("南郑1");
    String s = JSONObject.toJSONString(nz);
    logger.info("insertThematic::"+s);
  }
  @Test
  public void deleteThematicByThematicName(){

    int i = thematicDao.deleteThematic("c40d32307ad911e9ab0a20040ff72212");

    logger.info("deleteThematicByThematicName::"+i);
  }
  @Test
  public void findByThematicId(){
    logger.info("findByThematicId::");
    Thematic byThematicId = thematicService.findByThematicId("52ffd62e7c7311e9a07b20040ff72212");
    JSONObject jsonObject= (JSONObject) JSONObject.toJSON(byThematicId);
    logger.info("findByThematicId::"+jsonObject);
  }
}
