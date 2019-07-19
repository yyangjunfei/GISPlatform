package cc.wanshan.gis.service.style;

import cc.wanshan.gis.entity.style.RuleValue;
import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/5/24 9:39
 */
public interface RuleValueService {

  /**
   * description: 插入ruleValue
   *
   * @param ruleValue 规则值的对象
   * @return java.lang.Boolean
   **/
  Boolean insertRuleValue(RuleValue ruleValue);

  /**
   * description:根据ruleNameId 查询所有ruleValue
   *
   * @param ruleNameId 规则名ID
   * @return java.util.List<cc.wanshan.gis.service.rulevalue.RuleValue>
   **/
  RuleValue findRuleValueByRuleNameId(String ruleNameId);
}
