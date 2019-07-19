package cc.wanshan.gis.service.thematic;

import cc.wanshan.gis.entity.thematic.SecondClassification;
import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/5/25 14:38
 */
public interface SecondClassificationService {

  /**
   * description: 新增第二分类记录数
   *
   * @param secondClassification 第二分类对象
   * @return int
   **/
  Boolean insertClassification(SecondClassification secondClassification);

  /**
   * description: 查询第二分类
   *
   * @param firstClassificationId 第一分类Id
   * @return java.util.List<cc.wanshan.gis.entity.thematic.SecondClassification>
   **/
  List<SecondClassification> findByFirstClass(String firstClassificationId);
}
