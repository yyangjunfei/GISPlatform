package cc.wanshan.gis.service.layer.thematic.impl;

import cc.wanshan.gis.dao.layer.thematic.FirstClassificationDao;
import cc.wanshan.gis.entity.layer.thematic.FirstClassification;
import cc.wanshan.gis.service.layer.thematic.FirstClassificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
        return i > 0;
    }


}
