package cc.wanshan.gis.service.security.impl;

import cc.wanshan.gis.dao.security.AuthorityDao;
import cc.wanshan.gis.entity.security.Authority;
import cc.wanshan.gis.entity.security.Role;
import cc.wanshan.gis.service.security.AuthorityService;
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
    private static final Logger logger= LoggerFactory.getLogger(AuthorityServiceImpl.class);
    @Resource
    private AuthorityDao authorityDao;
    @Override
    public Authority findAuthorityByUrl(String url) {
        logger.info("findAuthorityByUrl::url = [{}]",url);
        return authorityDao.findByUrl(url);
    }
    @Override
    public List<Role> findRolesByAuthorId(String authorId) {
        logger.info("findRolesByAuthorId::authorId = [{}]",authorId);
        Authority authority = authorityDao.findByAuthorId(authorId);
        return authority.getRoleList();
    }
}
