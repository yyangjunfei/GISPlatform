package cc.wanshan.gis.service.firstclassification.impl;

import cc.wanshan.gis.dao.FirstClassificationDao;
import cc.wanshan.gis.entity.thematic.FirstClassification;
import cc.wanshan.gis.entity.thematic.SecondClassification;
import cc.wanshan.gis.service.firstclassification.FirstClassificationService;
import java.util.List;
import javax.annotation.Resource;
import javax.naming.Name;
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
