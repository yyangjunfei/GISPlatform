package cc.wanshan.gis.service.authorize;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.authorize.Authority;
import cc.wanshan.gis.entity.authorize.Role;

import java.net.URL;
import java.util.List;

public interface AuthorityService {
    /**
     * description: 根据url查询权限详情
     *
     * @param url url
     * @return cc.wanshan.gis.entity.security.Authority
     **/
    Result findByUrl(String url);

    /**
     * description:
     *
     * @param authorId 根据权限Id查询对应角色集合
     * @return java.util.List<cc.wanshan.gis.entity.security.Role>
     **/
    Result findByAuthorId(String authorId);
    Result insert(String authorName, String url);
    Result update(String authorId,String authorName, String url);
    Result delete(String authorId);

}
