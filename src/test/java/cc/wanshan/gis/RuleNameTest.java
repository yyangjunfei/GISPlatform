/*
package cc.wanshan.gis;

import cc.wanshan.gis.dao.RuleNameDao;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.style.RuleName;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

*/
/**
 * @author Li Cheng
 * @date 2019/5/21 19:52
 *//*

@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleNameTest {

  private static final Logger logger = LoggerFactory.getLogger(RuleNameTest.class);
  @Resource
  private RuleNameDao ruleNameDao;
  private RuleName ruleName = new RuleName();

  @Test
  public void insertRuleName() {
    logger.info("insetRuleName::");
    ruleName.setFillEnv("C1_hy_fillcolor");
    ruleName.setOpacityEnv("C1_hy_opacity");
    int i = ruleNameDao.insertRuleName(ruleName);
    logger.info("insetRuleName::"+i+ ruleName.toString());
  }
}
*/
