package cc.wanshan.gis;

import cc.wanshan.gis.dao.layer.PointDao;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.drawlayer.Point;
import java.util.ArrayList;
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
 * @date 2019/6/3 13:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PointTest {

  private static final Logger logger = LoggerFactory.getLogger(PointTest.class);
  private Point point = new Point();
  private Layer layer = new Layer();
  @Resource
  private PointDao pointDao;

  /*@Test
  public void insertPoint() {
    logger.info("insertPoint::");
    layer.setLayerId("b3cb721282bb11e9b38720040ff72212");
    point.setLayer(layer);
    point.setEpsg("4326");
    point.setFeatureName("一个中心两个基本点");
    String geom = "{"
        + "\"type\": \"Point\","
        + "\"coordinates\": ["
        + "13057069.05732599,"
        + "3748420.40811225"
        + "]"
        + "}";
    point.setGeom(geom);
    point.setInsertTime(new Date());
    point.setUpdateTime(new Date());
    int i = pointDao.insertPoint(point);
    logger.info("insertPoint::" + i);
  }

  @Test
  public void findPoint() {
    logger.info("findPoint::");
    List<Point> pointByLayerId = pointDao.findPointByLayerId("b3cb721282bb11e9b38720040ff72212");
    for (Point point1 : pointByLayerId) {
      logger.info("findPoint::" + point1.toString());
    }
  }*/

  @Test
  public void findAllPoint() {
    logger.info("findPoint::");
    layer.setLayerId("b3cb721282bb11e9b38720040ff72212");
    point.setLayer(layer);
    point.setEpsg("4326");
    point.setFeatureName("一个中心两个基本点");
    String geom = "{"
        + "\"type\": \"Point\","
        + "\"coordinates\": ["
        + "13057069.05732599,"
        + "3748420.40811225"
        + "]"
        + "}";
    point.setGeom(geom);
    point.setInsertTime(new Date());
    point.setUpdateTime(new Date());
    List<Point> list = new ArrayList<>();
    list.add(point);
    list.add(point);
    list.add(point);
    list.add(point);
    list.add(point);
    int i = pointDao.insertAllPoint(list);
    logger.info("findAllPoint::" + i);
  }

  @Test
  public void select() {
    logger.info("select::");
    List<Point> pointByLayerId = pointDao
        .findPoint("b3cb721282bb11e9b38720040ff72212", null, null, null);
    logger.info("select::" + pointByLayerId.toString());

  }

  @Test
  public void delete() {
    logger.info("delete::");
    int delete = pointDao.delete("b3cb721282bb11e9b38720040ff72212", null, null, null);
    logger.info("delete::" + delete);
  }

  @Test
  public void deleteAll() {
    logger.info("deleteAll::");
    List<Point> list = new ArrayList<>();
    point.setFeatureId("eac27fa0866611e9a26c20040ff72212");
    list.add(point);
    Point point1 = new Point();
    Point point2 = new Point();
    point1.setFeatureId("eac27fa1866611e9a26d20040ff72212");
    list.add(point1);
    point2.setFeatureId("eac27fa2866611e9a26e20040ff72212");
    list.add(point2);
    int i = pointDao.deleteAll(list);
    logger.info("deleteAll::" + i);
  }

  @Test
  public void updateAll() {
    logger.info("updateAll::");
    layer.setLayerId("b3cb721282bb11e9b38720040ff72212");
    point.setLayer(layer);
    point.setEpsg("4326");
    point.setFeatureName("一个中心两个基本点");
    String geom = "{"
        + "\"type\": \"Point\","
        + "\"coordinates\": ["
        + "13057069,"
        + "3748420"
        + "]"
        + "}";
    point.setGeom(geom);
    point.setInsertTime(new Date());
    point.setUpdateTime(new Date());
    point.setFeatureId("eac27fa0866611e9a26c20040ff72212");
    List<Point> list = new ArrayList<>();
    list.add(point);
    Point point1 = new Point();
    point1.setLayer(layer);
    point1.setEpsg("4326");
    point1.setFeatureName("一个中心两个基本点");
    point1.setGeom(geom);
    point1.setInsertTime(new Date());
    point1.setUpdateTime(new Date());
    point1.setFeatureId("eac27fa0866611e9a26c20040ff72212");
    Point point2 = new Point();
    point2.setLayer(layer);
    point2.setEpsg("4326");
    point2.setFeatureName("一个中心两个基本点");
    point2.setGeom(geom);
    point2.setInsertTime(new Date());
    point2.setUpdateTime(new Date());
    point2.setFeatureId("eac27fa0866611e9a26c20040ff72212");
    list.add(point1);
    list.add(point2);
   // int i = pointDao.updateAll(list);
   // logger.info("updateAll::"+i);
  }
}
