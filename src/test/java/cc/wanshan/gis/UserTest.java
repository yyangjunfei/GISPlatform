package cc.wanshan.gis;

import cc.wanshan.gis.dao.RoleDao;
import cc.wanshan.gis.dao.UserDao;
import cc.wanshan.gis.entity.thematic.Thematic;
import cc.wanshan.gis.entity.usermanagement.Role;
import cc.wanshan.gis.entity.usermanagement.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
    private static final Logger logger= LoggerFactory.getLogger(UserTest.class);
    @Resource
    private UserDao userDao;
    @Resource
    private RoleDao roleDao;
    @Test
    public void insertUser(){
        logger.info("insertUser::");
        User user = new User();
        user.setUsername("蒹葭苍苍");
        user.setPassword("白露为霜");
        user.setStatus(0);
        user.setDelete(0);
        user.setUpdateTime(new Date());
        user.setInsertTime(new Date());
        int i = userDao.insertUser(user);
        logger.info("insertUser::"+i);
    }
    @Test
    public void findUserByUsername(){
        logger.info("findUserByUsername::");
        User user = userDao.findUserByUsername("蒹葭苍苍");
        logger.info("findUserByUsername::"+user.toString());
    }
    @Test
    public void  updateUser(){
        logger.info("updateUser::");
        User user = new User();
        Role role = new Role();
        Thematic thematic = new Thematic();
        thematic.setThematicId("829699847b8f11e9b10120040ff72212");
        role.setRoleId("dd786ef07a9911e990f920040ff72212");
        user.setUserId("7ca5cf8e7ab011e98ce020040ff72212");
        user.setSecurity("绝密");
        user.setStatus(1);
        user.setRole(role);
        user.setThematic(thematic);
        user.setUpdateTime(new Date());
        int i = userDao.updateUserStatus(user);
        logger.info("updateUser::"+i);
    }
}
