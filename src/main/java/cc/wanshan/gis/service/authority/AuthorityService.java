package cc.wanshan.gis.service.authority;


import cc.wanshan.gis.entity.usermanagement.Authority;
import cc.wanshan.gis.entity.usermanagement.Role;

import java.util.List;

public interface AuthorityService {
    public Authority findAuthorityByUrl(String url);
    public List<Role> findRolesByAuthorId(String authorId);
}
