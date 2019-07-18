package cc.wanshan.gis.service.thematic.impl;

import cc.wanshan.gis.dao.thematic.ThematicUserDao;
import cc.wanshan.gis.entity.thematic.Thematic;
import cc.wanshan.gis.entity.thematic.ThematicUser;
import cc.wanshan.gis.service.thematic.ThematicUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/5/25 11:25
 */
@Service(value = "thematicUserServiceImpl")
public class ThematicUserServiceImpl implements ThematicUserService {
    @Resource
    private ThematicUserDao thematicUserDao;

    @Override
    public Boolean insertThematicUser(ThematicUser thematicUser) {
        int i = thematicUserDao.insertThematicUser(thematicUser);
        return i > 0;
    }

    @Override
    public Boolean updateThematicUser(ThematicUser thematicUser) {
        int i = thematicUserDao.updateThematicUser(thematicUser);
        return i > 0;
    }

    @Override
    public Boolean deleteThematicUser(String thematicUserId) {
        int i = thematicUserDao.deleteThematicUser(thematicUserId);
        return i > 0;
    }

    @Override
    public List<Thematic> findThematicLayersByUserId(String userId) {
        return thematicUserDao.findThematicLayersByUserId(userId);
    }
}
