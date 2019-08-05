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
public class AccessDecisionManagerImpl implements AccessDecisionManager {
private static final Logger logger= LoggerFactory.getLogger(AccessDecisionManagerImpl.class);
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        logger.info("decide::authentication = [{}], o = [{}], collection = [{}]",authentication, o, collection);
        Iterator<ConfigAttribute> iterator = collection.iterator();
        while(iterator.hasNext()){
            ConfigAttribute ca = iterator.next();
            String needRole = ca.getAttribute();
            if ("ROLE_LOGIN".equals(needRole)){
                if (authentication instanceof AnonymousAuthenticationToken){
                    throw new BadCredentialsException("未登录");
                }else{
                    return;
                }
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator1 = authorities.iterator();
            while (iterator1.hasNext()){
                GrantedAuthority authority = iterator1.next();
                String authority1 = authority.getAuthority();
                int length = authority1.length();
                String roleName=authority1.substring(2, length-2);
                logger.info("authority::",authority);
                if (roleName.equals(needRole)){
                    return;
                }
            }
        }
        //执行到这里说明没有匹配到应有权限
        throw new AccessDeniedException("权限不足!");
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
