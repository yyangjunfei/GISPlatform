package cc.wanshan.gis.service.rulename;

import cc.wanshan.gis.entity.style.RuleName;
import java.util.List;


/**
 * @Author Li Cheng
 * @Date 11:17 2019/5/24
 **/
public interface RuleNameService {

   /**
    * description: 插入ruleName对象
    *
    * @Param ruleName 规则名对象
    * @return Boolean
    */
  public Boolean insertRuleName(RuleName ruleName);


  /**
   * description: 根据layerId查询RuleName
   *
   * @param styleId
   * @return java.util.List<cc.wanshan.gis.entity.style.RuleName>
   **/
  public List<RuleName>findRuleNameByStyleId(String styleId);
  /**
   * description: 根据图层名查询style集合
   *
   * @param layerName 图层名
   * @return java.util.List<cc.wanshan.gis.entity.style.RuleName>
   **/
  public List<RuleName> findRuleNamesByLayerName(String layerName);
}
