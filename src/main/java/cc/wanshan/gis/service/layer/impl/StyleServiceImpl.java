package cc.wanshan.gis.service.layer.impl;

import cc.wanshan.gis.dao.layer.StyleDao;
import cc.wanshan.gis.entity.style.Style;
import cc.wanshan.gis.service.layer.StyleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/5/24 9:42
 */
@Service(value = "styleServiceImpl")
public class StyleServiceImpl implements StyleService {

    @Resource
    private StyleDao styleDao;

    @Override
    public List<Style> findAllStyle() {
        return styleDao.findAllStyle();
    }

    @Override
    public List<Style> findByLayerId(String layerId) {
        return styleDao.findByLayerId(layerId);
    }

    @Override
    public List<Style> findStyleByStyleName(String styleName) {
        return styleDao.findStyleByStyleName(styleName);
    }
}
