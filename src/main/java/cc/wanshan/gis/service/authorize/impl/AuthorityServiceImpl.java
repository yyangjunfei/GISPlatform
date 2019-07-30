package cc.wanshan.gis.service.authorize.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.authorize.AuthorityDao;
import cc.wanshan.gis.entity.authorize.Authority;
import cc.wanshan.gis.entity.authorize.Role;
import cc.wanshan.gis.service.authorize.AuthorityService;
import cc.wanshan.gis.utils.base.ResultUtil;
import com.sun.org.apache.regexp.internal.RE;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Li Cheng
 * @Date 8:42 2019/7/17
 **/
@Service(value = "authorityServiceImpl")
public class AuthorityServiceImpl implements AuthorityService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    @Resource
    private AuthorityDao authorityDao;

    @Override
    public Result findByUrl(String url) {
        logger.info("findAuthorityByUrl::url = [{}]", url);
         if (StringUtils.isBlank(url)){
             return ResultUtil.error(ResultCode.PARAM_IS_NULL);
         }
        Authority authority = authorityDao.findByUrl(url);
         if (authority!=null){
             return ResultUtil.success(authority);
         }
         return ResultUtil.error(ResultCode.FIND_NULL);
    }

    @Override
    public Result findByAuthorId(String authorId) {
        logger.info("findRolesByAuthorId::authorId = [{}]", authorId);
        if (StringUtils.isBlank(authorId)){
            return ResultUtil.error(ResultCode.PARAM_IS_NULL);
        }
        Authority authority = authorityDao.findByAuthorId(authorId);
        if (authority!=null){
            return ResultUtil.success(authority);
        }
        return ResultUtil.error(ResultCode.FIND_NULL);
    }


    @Override
    public Result insert(String authorName, String url) {
        logger.info("insert::authorName = [{}], url = [{}]",authorName, url);
        if (StringUtils.isBlank(authorName)&&StringUtils.isBlank(url)){
            return ResultUtil.error(ResultCode.PARAM_IS_NULL);
        }

        int insert = authorityDao.insert(new Authority(authorName, url,new Date(),new Date()));
        if (insert!=1){
            return ResultUtil.error(ResultCode.SAVE_FAIL);
        }
        return ResultUtil.success();
    }

    @Override
    public Result update(String authorId, String authorName, String url) {
        logger.info("update::authorId = [{}], authorName = [{}], url = [{}]",authorId, authorName, url);
        if (StringUtils.isBlank(authorId)&&StringUtils.isBlank(authorName)&&StringUtils.isBlank(url)){
            return ResultUtil.error(ResultCode.PARAM_IS_NULL);
        }
        int update = authorityDao.update(new Authority(authorId, url, authorName,new Date(),new Date()));
        if (update!=1){
            return ResultUtil.error(ResultCode.UPDATE_FAIL);
        }
        return ResultUtil.success();
    }

    @Override
    public Result delete(String authorId) {
        logger.info("delete::authorId = [{}]",authorId);
        if (StringUtils.isBlank(authorId)){
            return ResultUtil.error(ResultCode.PARAM_IS_NULL);
        }
        int delete = authorityDao.delete(authorId);
        if (delete!=1){
            return ResultUtil.error(ResultCode.DELETE_FAIL);
        }
        return ResultUtil.success();
    }
}
