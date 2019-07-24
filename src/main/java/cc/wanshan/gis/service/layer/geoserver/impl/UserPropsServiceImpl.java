package cc.wanshan.gis.service.layer.geoserver.impl;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.layer.UserPropsDao;
import cc.wanshan.gis.entity.layer.geoserver.UserProps;
import cc.wanshan.gis.service.layer.geoserver.UserPropsService;
import cc.wanshan.gis.utils.base.ResultUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author Li Cheng
 * @date 2019/7/19 8:53
 */
@Service("userPropsServiceImpl")
public class UserPropsServiceImpl implements UserPropsService {

    private static final Logger logger = LoggerFactory.getLogger(UserPropsServiceImpl.class);
    @Resource
    private UserPropsDao userPropsDao;

    @Override
    public Result findUserPropsByUsername(String username) {
        logger.info("findUserPropsByUsername::username = [{}]", username);

        UserProps usersProps = userPropsDao.findUsersPropsByUsername(username);
        if (usersProps != null) {
            return ResultUtil.success(usersProps);
        }
        return ResultUtil.error("该用户无相关权限");
    }

    @Override
    public Result saveUserProps(String username, String propName) {
        logger.info("saveUserProps::username = [{}], propName = [{}]", username, propName);
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(propName)) {
            String propValue = UUID.randomUUID().toString().replace("-", "");
            int i = userPropsDao.insertUserProps(new UserProps(username, propName, propValue));
            if (i == 1) {
                return ResultUtil.success();
            }
            return ResultUtil.error("保存失败");
        }
        return ResultUtil.error(4, "参数为null");
    }

    @Override
    public Result updateUserProps(String username, String propName) {
        logger.info("updateUserProps::username = [{}], propName = [{}]", username, propName);
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(propName)) {
            String propValue = UUID.randomUUID().toString().replace("-", "");
            int i = userPropsDao.updateUserProps(new UserProps(username, propName, propValue));
            if (i == 1) {
                return ResultUtil.success();
            }
            return ResultUtil.error("更新失败");
        }
        return ResultUtil.error(4, "参数为null");
    }

    @Override
    public Result deleteUserProps(String username, String propName) {
        logger.info("deleteUserProps::username = [{}], propName = [{}]", username, propName);
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(propName)) {
            int i = userPropsDao.deleteUserProps(username, propName);
            if (i == 1) {
                return ResultUtil.success();
            }
            return ResultUtil.error("删除失败");
        }
        return ResultUtil.error(4, "参数为null");
    }

}
