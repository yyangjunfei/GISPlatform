/*
package cc.wanshan.gis;

import cc.wanshan.gis.dao.security.RoleDao;
import cc.wanshan.gis.entity.security.Role;
import java.util.Date;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

*/
/**
 * @author Li Cheng
 * @date 2019/5/20 8:34
 *//*

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleTest {
  private static final Logger logger= LoggerFactory.getLogger(RoleTest.class);
  @Resource
  private RoleDao roleDao;
  @Test
  public void insertRose(){
    logger.info("insertRose::");
    Role role = new Role();
    role.setRoleName("ROLE_USER");
    role.setRoleNameZH("用户");
    role.setDescribe("普通");
    role.setInsertTime(new Date());
    role.setUpdateTime(new Date());
    int i = roleDao.insertRole(role);
    System.out.println("结果为"+i);
  }
  @Test
  public void deleteRose(){
    logger.info("insertRose::");
    int i = roleDao.deleteRole("9cd0e3b47a9e11e9bbc220040ff72212");
    logger.info("insertRole::删除结果为："+i);

  }
  @Test
  public void updateRole(){
    logger.info("updateRole::");
    Role role = new Role();
    role.setRoleId("500371327a9e11e9b2cb20040ff72212");
    role.setRoleName("ROLE_USER");
    role.setRoleNameZH("用户");
    role.setDescribe("普通");
    role.setUpdateTime(new Date());
    int i = roleDao.updateRole(role);
    logger.info("updateRole::"+i);
  }
}
*/
