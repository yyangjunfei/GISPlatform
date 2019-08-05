package cc.wanshan.gis.dao.layer;

import cc.wanshan.gis.entity.layer.geoserver.UserProps;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @author Li Cheng
 * @date 2019/7/17 16:07
 */
@Mapper
@Component
public interface UserPropsDao {

    @Select({
            "select * "
                    + "from "
                    + "user_props "
                    + "where "
                    + "username=#{username} "
    })
    @Results({
            @Result(id = true, column = "username", property = "username"),
            @Result(column = "propname", property = "propName"),
            @Result(column = "propvalue", property = "propValue"),
    })

    /**
     * description: 根据用户名查询对应的Geoserver authkey
     *
     * @param username 用户名
     * @return cc.wanshan.gis.dao.geoserver.UserPropsDao
     **/
    UserProps findUsersPropsByUsername(String username);

    @Insert({
            "insert into "
                    + "user_props "
                    + "("
                    + "username,"
                    + "propname,"
                    + "propvalue"
                    + ") "
                    + "values "
                    + "("
                    + "#{username},"
                    + "#{propName},"
                    + "#{propValue}"
                    + ")"
    })
    @Options(useGeneratedKeys = true, keyProperty = "username", keyColumn = "username")
    /**
     * description:新增UserProps
     *
     * @param userProps
     * @return int
     */
    int insertUserProps(UserProps userProps);


    @Update({
            "update "
                    + "user_props "
                    + "set "
                    + "propname=#{propName},"
                    + "propvalue=#{propValue}"
                    + "where "
                    + "username=#{username}"
    })

    /**
     * description: 更新userProps
     *
     * @param userProps
     * @return int
     */
    int updateUserProps(UserProps userProps);


    @Delete({
            "delete from "
                    + "user_props "
                    + "where "
                    + "username=#{username} and "
                    + "propname=#{propName}"
    })
    /**
     * description:删除userProps
     *
     * @param username
     * @return int
     */
    int deleteUserProps(String username, String propName);

    @Select("select * from user_props where propvalue=#{propValue}")
    UserProps findUsersPropsByPropValue(String propValue);

}
