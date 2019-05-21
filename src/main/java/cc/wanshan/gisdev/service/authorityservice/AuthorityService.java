package cc.wanshan.gisdev.service.authorityservice;


import cc.wanshan.gisdev.entity.usermanagement.Authority;
import cc.wanshan.gisdev.entity.usermanagement.Role;

import java.util.List;

public interface AuthorityService {
    public Authority findAuthorityByUrl(String url);
    public List<Role> findRolesByAuthorId(String authorId);
}
