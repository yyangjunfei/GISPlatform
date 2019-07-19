package cc.wanshan.gis.service.style.impl;

import cc.wanshan.gis.dao.style.RuleValueDao;
import cc.wanshan.gis.entity.style.RuleValue;
import cc.wanshan.gis.service.style.RuleValueService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Li Cheng
 * @date 2019/5/24 11:32
 */
@Service(value = "ruleValueServiceImpl")
public class RuleValueServiceImpl implements RuleValueService {
  private static final Logger logger= LoggerFactory.getLogger(RuleValueServiceImpl.class);
  @Resource
  private RuleValueDao ruleValueDao;

  @Override
  public Boolean insertRuleValue(RuleValue ruleValue) {
    logger.info("insertRuleValue::ruleValue = [{}]",ruleValue);
    if (ruleValue!=null){
      return ruleValueDao.insertRuleValue(ruleValue)>0;
    }
    return null;
  }
  @Override
  public RuleValue findRuleValueByRuleNameId(String ruleNameId) {
    logger.info("findRuleValueByRuleNameId::ruleNameId = [{}]",ruleNameId);
    return ruleValueDao.findRuleValuesByRuleNameId(ruleNameId);
  }
}
