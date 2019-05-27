package cc.wanshan.gis.service.rulename.impl;

import cc.wanshan.gis.dao.RuleNameDao;
import cc.wanshan.gis.entity.style.RuleName;
import cc.wanshan.gis.service.rulename.RuleNameService;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Li Cheng
 * @date 2019/5/24 10:41
 */
@Service(value = "ruleNameServiceImpl")
public class RuleNameServiceImpl implements RuleNameService {

  private static final Logger logger = LoggerFactory.getLogger(RuleNameServiceImpl.class);
  private RuleName ruleName = new RuleName();
  @Resource
  private RuleNameDao ruleNameDao;

  @Override
  public Boolean insertRuleName(RuleName ruleName) {
    logger.info("insertRuleName::ruleName = [{}]",ruleName);
    if (ruleName != null) {
      int i = ruleNameDao.insertRuleName(ruleName);
      if (i>0){
        return true;
      }else {
        return false;
      }
    }
    logger.error("插入失败");
    return null;
  }
  @Override
  public List<RuleName> findRuleNameByStyleId(String styleId) {
    logger.info("findRuleNameByLayerId::layerId = [{}]",styleId);
    return ruleNameDao.findRuleNamesByStyleId(styleId);
  }

  @Override
  public List<RuleName> findRuleNamesByLayerName(String layerName) {
    return ruleNameDao.findRuleNamesByLayerName(layerName);
  }
}
