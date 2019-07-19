package cc.wanshan.gis.service.thematic.impl;

import cc.wanshan.gis.dao.layer.FirstClassificationDao;
import cc.wanshan.gis.entity.thematic.FirstClassification;
import cc.wanshan.gis.service.thematic.FirstClassificationService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author Li Cheng
 * @date 2019/5/25 14:35
 */
@Service(value = "firstClassificationServiceImpl")
public class FirstClassificationServiceImpl implements FirstClassificationService {
@Resource
private FirstClassificationDao firstClassificationDao;
  @Override
  public Boolean insertFirstClassification(FirstClassification firstClassification) {
    int i = firstClassificationDao.insertFirstClassification(firstClassification);
    return i>0;
  }


}
