package cc.wanshan.gis.service.authorize;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.authorize.Authority;
import cc.wanshan.gis.entity.authorize.Role;

import java.util.List;

public interface AuthorityService {

    Authority findAuthorityByUrl(String url);

    List<Role> findRolesByAuthorId(String authorId);

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    Result refresh(String token);

    /**
     * 退出登录
     *
     * @param token
     * @return
     */
    Result logout(String token);
}
