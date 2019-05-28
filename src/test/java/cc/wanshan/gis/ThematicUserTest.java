package cc.wanshan.gis;

import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.thematic.Thematic;
import cc.wanshan.gis.entity.thematic.ThematicUser;
import cc.wanshan.gis.service.thematicuser.ThematicUserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
 * @date 2019/5/25 11:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ThematicUserTest {
private ThematicUser thematicUser=new ThematicUser();
private static final Logger logger= LoggerFactory.getLogger(ThematicUserTest.class);
  @Resource
  private ThematicUserService thematicUserService;
  @Test
  public void insertThematicUser() {
    logger.info("insertThematicUser::");
    thematicUser.setThematicId("4194542c7e0411e9b9dc20040ff72212");
    thematicUser.setUserId("7ca5cf8e7ab011e98ce020040ff72212");
    thematicUser.setInsertTime(new Date());
    thematicUser.setUpdateTime(new Date());
    Boolean aBoolean = thematicUserService.insertThematicUser(thematicUser);
    logger.info("insertThematicUser::"+aBoolean);
  }
  @Test
  public void findThematicLayersByUserId(){
    logger.info("findThematicLayersByUserId::");
    List<Thematic> thematics = thematicUserService
        .findThematicLayersByUserId("7ca5cf8e7ab011e98ce020040ff72212");
    JSONArray jsonObject = (JSONArray) JSONArray.toJSON(thematics);
    logger.info("findThematicLayersByUserId::"+jsonObject);
  }
}
