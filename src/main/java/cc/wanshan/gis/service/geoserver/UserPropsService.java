package cc.wanshan.gis.service.geoserver;

import cc.wanshan.gis.entity.Result;
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
  Result findUserPropsByUsername(String username);
  /**
   * description:新增userProps
   *
   * @param username 用户名
   * @param propName 属性名
   * @return java.lang.Integer
   **/
  Result saveUserProps(String username,String propName);
  /**
   * description: 更新userProps
   *
   * @param username
   * @Param propName
   * @return java.lang.Boolean
   **/
  Result updateUserProps(String username,String propName);
  /**
   * description: 删除UserProps
   *
   * @param username
   * @param propName
   * @return java.lang.Boolean
   **/
  Result deleteUserProps(String username,String propName);
}
