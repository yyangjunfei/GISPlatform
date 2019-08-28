package cc.wanshan.gis.common.security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Iterator;

@Component
//决策管理器
public class AccessDecisionManagerImpl implements AccessDecisionManager {
    private static final Logger logger = LoggerFactory.getLogger(AccessDecisionManagerImpl.class);

    //该方法决定该用户权限是否有权限访问该资源，其实object就是一个资源的地址，authentication是当前用户的
    //对应权限，如果没登陆就为游客，登陆了就是该用户对应的权限

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        logger.info("decide::authentication = [{}], o = [{}], collection = [{}]", authentication, o, collection);
        //所请求的资源拥有的权限(一个资源对多个权限)
        Iterator<ConfigAttribute> iterator = collection.iterator();
        while (iterator.hasNext()) {
            ConfigAttribute ca = iterator.next();
            //访问所请求资源所需要的权限
            String needRole = ca.getAttribute();
            if ("ROLE_LOGIN".equals(needRole)) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new BadCredentialsException("未登录");
                } else {
                    return;
                }
            }
            //用户所拥有的权限authentication 与访问所请求资源 权限对比
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator1 = authorities.iterator();
            while (iterator1.hasNext()) {
                GrantedAuthority authority = iterator1.next();
                if (needRole.trim().equals(authority.getAuthority())) {
                    return;
                }
            }
        }
        //执行到这里说明没有匹配到应有权限
        throw new AccessDeniedException("抱歉，您没有访问权限");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
