package cc.wanshan.gis.service.layer.style;

import cc.wanshan.gis.entity.layer.style.RuleName;

import java.util.List;

/**
 * @Author Li Cheng
 * @Date 11:17 2019/5/24
 **/
public interface RuleNameService {

    /**
     * description: 插入ruleName对象
     *
     * @return Boolean
     * @Param ruleName 规则名对象
     */
    Boolean insertRuleName(RuleName ruleName);


    /**
     * description: 根据layerId查询RuleName
     *
     * @return java.util.List<cc.wanshan.gis.entity.layer.style.RuleName>
     **/
    List<RuleName> findRuleNameByStyleId(String styleId);

    /**
     * description: 根据图层名查询style集合
     *
     * @param layerName 图层名
     * @return java.util.List<cc.wanshan.gis.entity.layer.style.RuleName>
     **/
    List<RuleName> findRuleNamesByLayerName(String layerName);
}
