package cc.wanshan.gis;

import cc.wanshan.gis.common.vo.Result;
import cc.wanshan.gis.dao.authorize.RoleDao;
import cc.wanshan.gis.entity.authorize.Role;
import cc.wanshan.gis.entity.authorize.User;
import cc.wanshan.gis.entity.thematic.Thematic;
import cc.wanshan.gis.entity.thematic.ThematicUser;
import cc.wanshan.gis.service.authorize.UserService;
import cc.wanshan.gis.service.thematic.ThematicUserService;
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
    private static final Logger logger = LoggerFactory.getLogger(UserTest.class);
    @Resource
    private UserService userServiceImpl;
    @Resource
    private ThematicUserService thematicUserServiceImpl;
    @Resource
    private RoleDao roleDao;

    @Test
    public void insertUser() {
        logger.info("insertUser::");
        User user = new User();
        Role role = new Role();
        String[] thematicId = {"52ffd62e7c7311e9a07b20040ff72212", "4194542c7e0411e9b9dc20040ff72212"};
        ThematicUser thematicUser = new ThematicUser();
        Thematic thematic = new Thematic();
        user.setUsername("licheng");
        user.setPassword("888888");
        user.setStatus(0);
        user.setDelete(0);
        user.setUpdateTime(new Date());
        user.setInsertTime(new Date());
        Result i = userServiceImpl.insertUser(user);
        logger.info("userServiceImpl.insertUser::" + i);
        if (i.getCode() == 0) {
            thematic.setThematicId("72f0f0747bad11e9ac6420040ff72212");
            role.setRoleId("2d8aa47c7c2911e9a6f820040ff72212");
            user.setSecurity("秘密");
            user.setStatus(1);
            user.setRole(role);
            user.setThematic(thematic);
            user.setUpdateTime(new Date());
            Result updateUserStatus = userServiceImpl.updateUserStatus(user);
            logger.info("updateUser::" + updateUserStatus);
            if (updateUserStatus.getCode() == 0) {
                for (String s : thematicId) {
                    thematicUser.setThematicId(s);
                    thematicUser.setUserId(user.getUserId());
                    thematicUser.setInsertTime(new Date());
                    thematicUser.setUpdateTime(new Date());
                    Boolean aBoolean = thematicUserServiceImpl.insertThematicUser(thematicUser);
                    logger.info("insertUser::" + aBoolean);
                }
            }
        }
        logger.info("insertUser::" + i);
    }

    @Test
    public void findUserByUsername() {
        logger.info("findUserByUsername::");
        User user = userServiceImpl.findUserByUsername("蒹葭苍苍");
        logger.info("findUserByUsername::" + user.toString());
    }

    @Test
    public void updateUser() {
        logger.info("updateUser::");
        User user = new User();
        Role role = new Role();
        Thematic thematic = new Thematic();
        thematic.setThematicId("72f0f0747bad11e9ac6420040ff72212");
        role.setRoleId("2d8aa47c7c2911e9a6f820040ff72212");
        user.setUserId("7ca5cf8e7ab011e98ce020040ff72212");
        user.setSecurity("绝密");
        user.setStatus(1);
        user.setRole(role);
        user.setThematic(thematic);
        user.setUpdateTime(new Date());
        Result i = userServiceImpl.updateUserStatus(user);
        logger.info("updateUser::" + i);
    }
}
