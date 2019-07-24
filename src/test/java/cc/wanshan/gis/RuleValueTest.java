package cc.wanshan.gis;

import cc.wanshan.gis.entity.layer.style.RuleName;
import cc.wanshan.gis.entity.layer.style.RuleValue;
import cc.wanshan.gis.service.layer.style.RuleValueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Li Cheng
 * @date 2019/5/24 10:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleValueTest {

    private static final Logger logger = LoggerFactory.getLogger(RuleValueTest.class);
    @Resource
    private RuleValueService ruleValueService;
    private RuleValue ruleValue = new RuleValue();
    private RuleName ruleName = new RuleName();

    @Test
    public void insertRuleValue() {
        logger.info("insertRuleValue::");
        ruleName.setRuleNameId("7b3493587dd811e9a3b520040ff72212");
        ruleValue.setRuleValue("星球大战");
        ruleValue.setFillValue("#BEDBF9");
        ruleValue.setOpacityValue("1");
        ruleValue.setInsertTime(new Date());
        ruleValue.setUpdateTime(new Date());
        ruleValue.setRuleName(ruleName);
        Boolean aBoolean = ruleValueService.insertRuleValue(ruleValue);
        logger.info("insertRuleValue::" + aBoolean);
    }
}
