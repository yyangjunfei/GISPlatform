package cc.wanshan.gisdev.service.authorityservice.impl;

import cc.wanshan.gisdev.dao.AuthorityDao;
import cc.wanshan.gisdev.entity.usermanagement.Authority;
import cc.wanshan.gisdev.entity.usermanagement.Role;
import cc.wanshan.gisdev.service.authorityservice.AuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
