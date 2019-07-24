package cc.wanshan.gis.service.layer.thematic.impl;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.layer.CreateSchemaDao;
import cc.wanshan.gis.dao.layer.thematic.ThematicDao;
import cc.wanshan.gis.entity.layer.thematic.Thematic;
import cc.wanshan.gis.service.layer.thematic.ThematicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Li Cheng
 * @date 2019/5/20 16:16
 */
@Service(value = "thematicServiceImpl")
public class ThematicServiceImpl implements ThematicService {

    private static final Logger logger = LoggerFactory.getLogger(ThematicServiceImpl.class);

    @Resource
    private ThematicDao thematicDao;

    @Resource(name = "createSchemaDaoImpl")
    private CreateSchemaDao createSchemaDaoImpl;

    @Override
    public Boolean insertThematic(Thematic thematic) {
        logger.info("insertThematic::thematic = [{}]", thematic);
        Result schema = createSchemaDaoImpl.createSchema(thematic.getThematicName());
        if (schema.getCode() == 0) {
            int i = thematicDao.insertThematic(thematic);
            if (i == 1) {
                return true;
            } else {
                logger.warn("insertThematic::thematic = [{}]新增记录失败" + i);
                return false;
            }
        }
        logger.warn("insertThematic::thematic = [{}]" + schema.getMsg());
        return false;
    }

    @Override
    public Thematic findByThematicId(String thematicId) {
        return thematicDao.findByThematicId(thematicId);
    }
}
