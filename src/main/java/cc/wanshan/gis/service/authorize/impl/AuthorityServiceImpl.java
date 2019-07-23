package cc.wanshan.gis.service.authorize.impl;

import cc.wanshan.gis.common.constants.SecurityConstant;
import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.authorize.AuthorityDao;
import cc.wanshan.gis.entity.authorize.Authority;
import cc.wanshan.gis.entity.authorize.Role;
import cc.wanshan.gis.service.authorize.AuthorityService;
import cc.wanshan.gis.utils.JwtTokenUtils;
import cc.wanshan.gis.utils.base.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "authorityServiceImpl")
public class AuthorityServiceImpl implements AuthorityService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    @Resource
    private AuthorityDao authorityDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Authority findAuthorityByUrl(String url) {
        logger.info("findAuthorityByUrl::url = [{}]", url);
        return authorityDao.findByUrl(url);
    }

    @Override
    public List<Role> findRolesByAuthorId(String authorId) {
        logger.info("findRolesByAuthorId::authorId = [{}]", authorId);
        Authority authority = authorityDao.findByAuthorId(authorId);
        return authority.getRoleList();
    }

    @Override
    public Result refresh(String oldToken) {
        if (oldToken == null || oldToken.isEmpty() || !oldToken.startsWith(SecurityConstant.TOKEN_PREFIX)) {
            return ResultUtil.error(ResultCode.JWT_TOKEN_FAIL);
        }

//        boolean expiration = JwtTokenUtils.isExpiration(oldToken);

        String username = JwtTokenUtils.getUsername(oldToken);
        String role = JwtTokenUtils.getUserRole(oldToken);
        String token = JwtTokenUtils.createToken(username, role);

        //将token存入redis缓存中
        redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + username, SecurityConstant.TOKEN_PREFIX + token);

        return ResultUtil.success(SecurityConstant.TOKEN_PREFIX + token);
    }

    @Override
    public Result logout(String token) {

        String username = JwtTokenUtils.getUsername(token);

        if (redisTemplate.delete(SecurityConstant.USER_TOKEN + username)) {
            return ResultUtil.success();
        }
        return ResultUtil.error("退出失败");
    }
}
