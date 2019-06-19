package cc.wanshan.gis.common.security;

import cc.wanshan.gis.entity.usermanagement.Authority;
import cc.wanshan.gis.entity.usermanagement.Role;
import cc.wanshan.gis.service.authority.AuthorityService;
import cc.wanshan.gis.service.role.RoleService;
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
    @Resource(name = "authorityServiceImpl")
    private AuthorityService authorityService;
    @Resource(name = "roleServiceImpl")
    private RoleService roleService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        logger.info("用户的请求地址为：" + requestUrl);
        if ("/login".equals(requestUrl)) {
            return null;
        }
        Authority authority = authorityService.findAuthorityByUrl(requestUrl);
        if (authority == null) {
            return null;
        } else {
            List<Role> roles = roleService.findRoleByAuthorId(authority.getAuthorId());
            int size = roles.size();
            String[] values = new String[size];
            for (int i = 0; i < size; i++) {
                values[i] = roles.get(i).getRoleName();
            }
            return SecurityConfig.createList(values);
        }
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
