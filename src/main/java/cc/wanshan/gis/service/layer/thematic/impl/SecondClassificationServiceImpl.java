package cc.wanshan.gis.service.layer.thematic.impl;

import cc.wanshan.gis.dao.layer.SecondClassificationDao;
import cc.wanshan.gis.entity.layer.thematic.SecondClassification;
import cc.wanshan.gis.service.layer.thematic.SecondClassificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/5/25 14:39
 */
@Service(value = "SecondClassificationServiceImpl")
public class SecondClassificationServiceImpl implements SecondClassificationService {
    @Resource
    private SecondClassificationDao secondClassificationDao;

    @Override
    public Boolean insertClassification(SecondClassification secondClassification) {
        int i = secondClassificationDao.insertSecondClassification(secondClassification);
        return i > 0;
    }

    @Override
    public List<SecondClassification> findByFirstClass(String firstClassificationId) {
        return secondClassificationDao
                .findByFirstClassificationId(firstClassificationId);
    }
}
