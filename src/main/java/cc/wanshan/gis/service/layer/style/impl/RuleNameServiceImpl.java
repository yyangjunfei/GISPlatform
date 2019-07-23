package cc.wanshan.gis.service.layer.style.impl;

import cc.wanshan.gis.dao.plot.of2d.RuleNameDao;
import cc.wanshan.gis.entity.layer.style.RuleName;
import cc.wanshan.gis.service.layer.style.RuleNameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/5/24 10:41
 */
@Service(value = "ruleNameServiceImpl")
public class RuleNameServiceImpl implements RuleNameService {

    private static final Logger logger = LoggerFactory.getLogger(RuleNameServiceImpl.class);

    @Resource
    private RuleNameDao ruleNameDao;

    @Override
    public Boolean insertRuleName(RuleName ruleName) {
        logger.info("insertRuleName::ruleName = [{}]", ruleName);
        if (ruleName != null) {
            int i = ruleNameDao.insertRuleName(ruleName);
            if (i > 0) {
                return true;
            } else {
                return false;
            }
        }
        logger.error("插入失败");
        return null;
    }

    @Override
    public List<RuleName> findRuleNameByStyleId(String styleId) {
        logger.info("findRuleNameByLayerId::layerId = [{}]", styleId);
        return ruleNameDao.findRuleNamesByStyleId(styleId);
    }

    @Override
    public List<RuleName> findRuleNamesByLayerName(String layerName) {
        return ruleNameDao.findRuleNamesByLayerName(layerName);
    }
}
