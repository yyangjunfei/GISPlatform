package cc.wanshan.gis.service.geoserver;

import cc.wanshan.gis.entity.geoserver.UserProps;

/**
 * @author Li Cheng
 * @date 2019/7/18 9:53
 */
public interface UserPropsService {
  /**
   * description:根据用户名查找userProps
   *
   * @param username 用户名
   * @return cc.wanshan.gis.entity.geoserver.UserProps
   **/
  UserProps findUserPropsByUsername(String username);
}
