package cc.wanshan.gis;

import cc.wanshan.gis.dao.RuleNameDao;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.style.RuleName;
import cc.wanshan.gis.entity.style.RuleValue;
import cc.wanshan.gis.entity.style.Style;
import cc.wanshan.gis.service.rulename.RuleNameService;
import cc.wanshan.gis.service.rulevalue.RuleValueService;
import com.alibaba.fastjson.JSONArray;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author Li Cheng
 * @Date 9:25 2019/5/24
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleNameTest {

  private static final Logger logger = LoggerFactory.getLogger(RuleNameTest.class);
  private Style style=new Style();
  private Layer layer=new Layer();
  @Resource
  private RuleNameService ruleNameService;
  @Resource
  private RuleValueService ruleValueService;
  private RuleValue ruleValue = new RuleValue();
  private RuleName ruleName = new RuleName();
  private RuleName ruleName1 = new RuleName();

  @Test
  public void insertRuleName() {
    layer.setLayerName("C1_zjxzq");
    ruleName.setLayer(layer);
    logger.info("insetRuleName::");
    ruleName.setFillEnv("C1_zjxzq_fill");
    ruleName.setFontFamilyEnv("C1_zjxzq_font_family");
    ruleName.setFontSizeEnv("C1_zjxzq_font_size");
    //ruleName.setOpacityEnv("C1_sk_opacity");
  //ruleName.setStrokeEnv("C1_xd_stroke");
    //ruleName.setWidthEnv("C1_jmd_stroke_width");
    style.setStyleId("9517dce2804b11e9abfa20040ff72212");
    ruleName.setStyle(style);
    ruleName.setInsertTime(new Date());
    ruleName.setUpdateTime(new Date());
    Boolean aBoolean1 = ruleNameService.insertRuleName(ruleName);
    if (aBoolean1){
      logger.info("insertRuleValue::" + aBoolean1);
      logger.info("insertRuleValue::");
      ruleName1.setRuleNameId(ruleName.getRuleNameId());
      ruleValue.setRuleValue("暗夜精灵");
      ruleValue.setFillValue("#0d1b2d");
      ruleValue.setFontFamilyValue("微软雅黑");
      ruleValue.setFontSizeValue("11.5");
      //ruleValue.setOpacityValue("1");
      //ruleValue.setStrokeValue("#415a77");
      //ruleValue.setWidthValue("0.5");
      ruleValue.setInsertTime(new Date());
      ruleValue.setUpdateTime(new Date());
      ruleValue.setRuleName(ruleName);
      Boolean aBoolean = ruleValueService.insertRuleValue(ruleValue);
      logger.info("insetRuleName::"+aBoolean+ ruleName.toString());
    }
  }
  @Test
  public void findRuleName(){
    logger.info("findRuleName::");
    List<RuleName> ruleNames = ruleNameService
        .findRuleNameByStyleId("23a4aa2e7dc911e98bc120040ff72212");
    JSONArray jsonArray= (JSONArray) JSONArray.toJSON(ruleNames);
    logger.info("findRuleName::"+jsonArray);
  }
  @Test
  public void findRuleNameByLayerName(){
    logger.info("findRuleName::");
    List<RuleName> ruleNameList = ruleNameService.findRuleNamesByLayerName("C1_hy");
    JSONArray jsonArray= (JSONArray) JSONArray.toJSON(ruleNameList);
    logger.info("findRuleName::"+jsonArray);
  }
}
