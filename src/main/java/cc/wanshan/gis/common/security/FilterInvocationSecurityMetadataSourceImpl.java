package cc.wanshan.gis.common.security;

import cc.wanshan.gis.dao.authorize.AuthorityDao;
import cc.wanshan.gis.dao.authorize.RoleDao;
import cc.wanshan.gis.entity.authorize.Authority;
import cc.wanshan.gis.entity.authorize.Role;
import cc.wanshan.gis.service.authorize.AuthorityService;
import cc.wanshan.gis.service.authorize.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    private static final Logger logger = LoggerFactory.getLogger(FilterInvocationSecurityMetadataSourceImpl.class);

    @Resource
    private AuthorityDao authorityDao;

    @Resource
    private RoleDao roleDao;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) {
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        logger.info("用户的请求地址为：" + requestUrl);
        String[] split = requestUrl.split("\\?");
        Authority authority = authorityDao.findByUrl(split[0]);
        logger.info("authorityDao.findByUrl(requestUrl)"+authority);
        if (authority !=null) {
            List<Role> roles = roleDao.findByAuthorId(authority.getAuthorId());
            int size = roles.size();
            String[] values = new String[size];
            for (int i = 0; i < size; i++) {
                values[i] = roles.get(i).getRoleName();
            }
            return SecurityConfig.createList(values);
        }
        return SecurityConfig.createList("ROLE_USER");
    }
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
