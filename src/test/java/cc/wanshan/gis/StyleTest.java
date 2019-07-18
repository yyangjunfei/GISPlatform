package cc.wanshan.gis;

import cc.wanshan.gis.dao.style.StyleDao;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.style.RuleName;
import cc.wanshan.gis.entity.style.RuleValue;
import cc.wanshan.gis.entity.style.Style;
import cc.wanshan.gis.service.style.RuleNameService;
import cc.wanshan.gis.service.style.RuleValueService;
import cc.wanshan.gis.service.style.StyleService;
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


@RunWith(SpringRunner.class)
@SpringBootTest
public class StyleTest {

  private static final Logger logger = LoggerFactory.getLogger(StyleTest.class);
  @Resource
  private StyleDao styleDao;
  @Resource(name = "styleServiceImpl")
  private StyleService styleServiceImpl;
  @Resource
  private RuleNameService ruleNameService;
  @Resource
  private RuleValueService ruleValueService;
  private Style style=new Style();
  private Layer layer =new Layer();
  @Test
  public void insertStyle() {
    logger.info("insertStyle::");
    style.setStyleName("梦幻灰");
    style.setInsertTime(new Date());
    style.setUpdateTime(new Date());
    //layer.setLayerId("5dc242ec7bb811e9a01e20040ff72212");
    //style.setLayer(layer);
    int i = styleDao.insertStyle(style);
    logger.info("insertStyle::"+i);
  }
  @Test
  public void findStyle() {
    logger.info("findStyle::");
    Style byStyleId = styleDao.findByStyleId("982c62d67bb911e9800220040ff72212");
    logger.info("findStyle::"+byStyleId.toString());
  }
  @Test
  public void findAllStyle(){
    logger.info("findAllStyle::");
    List<Style> allStyle = styleServiceImpl.findAllStyle();
    JSONArray jsonArray= (JSONArray) JSONArray.toJSON(allStyle);
      logger.info("findAllStyle::结果"+jsonArray);
  }

  @Test
  public void findByLayerId(){
    logger.info("findAllStyle::");
    List<Style> allStyle = styleServiceImpl.findByLayerId("5dc242ec7bb811e9a01e20040ff72212");
    JSONArray jsonArray= (JSONArray) JSONArray.toJSON(allStyle);
    logger.info("findAllStyle::结果"+jsonArray);
  }
  @Test
  public void findByStyleName(){
    logger.info("findAllStyle::");
    List<Style> styleList = styleServiceImpl.findStyleByStyleName("星球大战");
    JSONArray jsonArray= (JSONArray) JSONArray.toJSON(styleList);
    logger.info("findAllStyle::结果"+jsonArray);
  }
  @Test
  public void replaceStyle(){
    Style style2 = new Style();
    style2.setStyleId("94dad65a80f811e98f0820040ff72212");
    List<Style> style = styleServiceImpl.findStyleByStyleName("暗夜精灵");
    for (Style style1 : style) {
      List<RuleName> ruleNameList = style1.getRuleNameList();
      for (RuleName ruleName : ruleNameList) {
        ruleName.setRuleNameId(null);
        ruleName.setStyle(style2);
        Boolean aBoolean = ruleNameService.insertRuleName(ruleName);
        if (aBoolean){
          RuleValue ruleValue = ruleName.getRuleValue();
          ruleValue.setRuleValueId(null);
          ruleValue.setRuleName(ruleName);
          ruleValue.setRuleValue("梦幻灰");
          Boolean aBoolean1 = ruleValueService.insertRuleValue(ruleValue);
          logger.info("replaceStyle::"+aBoolean1);
        }else {
          break;
        }
      }
    }
  }
}
