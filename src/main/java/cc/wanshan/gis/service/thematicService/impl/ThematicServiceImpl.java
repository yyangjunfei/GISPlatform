package cc.wanshan.gisdev.service.thematicService.impl;

import cc.wanshan.gisdev.dao.ThematicDao;
import cc.wanshan.gisdev.dao.createschemadao.CreateSchemaDao;
import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.entity.thematic.Thematic;
import cc.wanshan.gisdev.service.thematicService.ThematicService;
import javax.annotation.Resource;
import javax.naming.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Li Cheng
 * @date 2019/5/20 16:16
 */
@Service(value ="thematicServiceImpl")
public class ThematicServiceImpl implements ThematicService {
  private static final Logger logger= LoggerFactory.getLogger(ThematicServiceImpl.class);
  @Resource
  private ThematicDao thematicDao;
  @Resource(name = "createSchemaDaoImpl")
  private CreateSchemaDao createSchemaDaoImpl;
  @Override
  public Boolean insertThematic(Thematic thematic) {
    logger.info("insertThematic::thematic = [{}]",thematic);
    Result schema = createSchemaDaoImpl.createSchema(thematic.getThematicName());
    if (schema.getCode()==0){
      int i = thematicDao.insertThematic(thematic);
      if (i==1){
        return true;
      }else {
        logger.warn("insertThematic::thematic = [{}]新增记录失败"+i);
        return false;
      }
    }
    logger.warn("insertThematic::thematic = [{}]"+schema.getMsg());
    return false;
  }
}
