package cc.wanshan.gis.service.security;


import cc.wanshan.gis.entity.security.Authority;
import cc.wanshan.gis.entity.security.Role;

import java.util.List;

public interface AuthorityService {
    /**
     * description: 根据url查询权限详情
     *
     * @param url url
     * @return cc.wanshan.gis.entity.security.Authority
     **/
    Authority findAuthorityByUrl(String url);
    /**
     * description:
     *
     * @param authorId 根据权限Id查询对应角色集合
     * @return java.util.List<cc.wanshan.gis.entity.security.Role>
     **/
    List<Role> findRolesByAuthorId(String authorId);
}
