package cc.wanshan.gis.dao.layer.thematic;

import cc.wanshan.gis.entity.layer.thematic.Thematic;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/5/18 16:57
 */

/**
 * thematic_id character varying(32) NOT NULL DEFAULT replace(uuid_generate_v1()::text,'-',''),
 * thematic_name character varying(20) COLLATE pg_catalog."default" NOT NULL, thematic_name_zh
 * character varying(32) COLLATE pg_catalog."default" NOT NULL, insert_time timestamp(0) with time
 * zone NOT NULL, update_time timestamp(0) with time zone NOT NULL, describe character varying(100)
 * COLLATE pg_catalog."default", CONSTRAINT thematic_pkey PRIMARY KEY (thematic_id)
 **/
@Mapper
@Component
public interface ThematicDao {

    /**
     * description: 根据thematicId查找Thematic
     *
     * @return cc.wanshan.gisdev.entity.thematic.Thematic
     */
    @Select({
            "select "
                    + "thematic_id,"
                    + "thematic_name,"
                    + "thematic_name_zh,"
                    + "insert_time,"
                    + "update_time,"
                    + "describe "
                    + "from "
                    + "thematic "
                    + "where "
                    + "thematic_id=#{thematicId}"
    })
    @Results({
            @Result(id = true, column = "thematic_id", property = "thematicId"),
            @Result(column = "thematic_name", property = "thematicName"),
            @Result(column = "thematic_name_zh", property = "thematicNameZH"),
            @Result(column = "describe", property = "describe"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "insert_time", property = "updateTime"),
            @Result(column = "thematic_id", property = "firstClassificationList", many = @Many(select = "cc.wanshan.gis.dao.layer.thematic.FirstClassificationDao.findByThematicId", fetchType = FetchType.LAZY)),
    })
    Thematic findByThematicId(String thematicId);


    /**
     * description:新增thematic
     *
     * @return int
     */
    @Insert({
            "insert into "
                    + "thematic "
                    + "(thematic_name,"
                    + "thematic_name_zh,"
                    + "insert_time,"
                    + "update_time,"
                    + "describe) "
                    + "values "
                    + "(#{thematicName},"
                    + "#{thematicNameZH},"
                    + "#{insertTime,jdbcType=TIMESTAMP},"
                    + "#{updateTime,jdbcType=TIMESTAMP},"
                    + "#{describe})"
    })
    @Options(useGeneratedKeys = true, keyColumn = "thematic_id", keyProperty = "thematicId")
    int insertThematic(Thematic thematic);


    /**
     * description:更新thematic
     *
     * @return int
     */
    @Update({
            "update "
                    + "thematic "
                    + "set "
                    + "thematic_id=#{thematicId},"
                    + "thematic_name=#{thematicName},"
                    + "thematic_name_zh=#{thematicNameZH},"
                    + "describe={#describe},"
                    + "update_time={#updateTime,jdbcType=TIMESTAMP} "
                    + "where "
                    + "thematic_id=#{thematicId}"
    })
    int updateThematic(Thematic thematic);


    /**
     * description:根据thematicId删除thematic
     *
     * @return int
     */
    @Delete({
            "delete from "
                    + "thematic "
                    + "where "
                    + "thematic_id=#{thematicId}"
    })
    int deleteThematic(String thematicId);


    /**
     * description:根据用户名查找thematic
     *
     * @return cc.wanshan.gisdev.entity.security.thematic
     */
    @Select({
            "select "
                    + "* "
                    + "from "
                    + "thematic"
                    + "where "
                    + "thematic_id=("
                    + "select  "
                    + "u.thematic_id"
                    + "from "
                    + "security as u "
                    + "where "
                    + "u.username =#{username})"
    })
    @Results({
            @Result(id = true, column = "thematic_id", property = "thematicId"),
            @Result(column = "thematic_name", property = "thematicName"),
            @Result(column = "thematic_name_zh", property = "thematicNameZH"),
            @Result(column = "describe", property = "describe"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "insert_time", property = "updateTime"),
    })
    Thematic findThematicByUserName(String username);


    /**
     * description: 查询所有的thematic
     *
     * @return java.util.List<cc.wanshan.gisdev.entity.security.thematic>
     */
    @Select({
            "select "
                    + "thematic_id ,"
                    + "thematic_name,"
                    + "thematic_name_zh,"
                    + "describe "
                    + "insert_time,"
                    + "update_time "
                    + "from "
                    + "thematic"})
    @Results({
            @Result(id = true, column = "thematic_id", property = "thematicId"),
            @Result(column = "thematic_name", property = "thematicName"),
            @Result(column = "thematic_name_zh", property = "thematicNameZH"),
            @Result(column = "describe", property = "describe"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "insert_time", property = "updateTime"),
            @Result(column = "thematic_id", property = "userList", many = @Many(select = "cc.wanshan.gis.dao.authorize.UserDao.findUsersByThematicId", fetchType = FetchType.LAZY))
    })
    List<Thematic> findAllThematic();


    /**
     * description: 根据thematicName查询thematic
     *
     * @return cc.wanshan.gisdev.entity.security.thematic
     */
    @Select({
            "select"
                    + "thematic_id,"
                    + "thematic_name,"
                    + "thematic_name_zh,"
                    + "describe,"
                    + "insert_time,"
                    + "update_time "
                    + "from "
                    + "thematic "
                    + "where "
                    + "thematic_name=#{thematicName}"
    })
    @Results({
            @Result(id = true, column = "thematic_id", property = "thematicId"),
            @Result(column = "thematic_name", property = "thematicName"),
            @Result(column = "thematic_name_zh", property = "thematicNameZH"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "insert_time", property = "updateTime"),
            @Result(column = "describe", property = "describe")
    })
    Thematic findThematicByThematicName(String thematicName);


    /**
     * description:判断thematic是否已存在
     *
     * @return int
     */
    @Select({
            "select "
                    + "count(*) "
                    + "from "
                    + "thematic "
                    + "where "
                    + "thematic_name=#{thematicName}"
    })
    int findCountByThematicName(String thematicName);


    /**
     * description:根据thematicNameZh查询thematic
     *
     * @return cc.wanshan.gisdev.entity.security.thematic
     */
    @Select({
            "select "
                    + "thematic_id,"
                    + "thematic_name,"
                    + "thematic_name_zh,"
                    + "describe,"
                    + "insert_time,"
                    + "update_time "
                    + "from "
                    + "thematic "
                    + "where "
                    + "thematic_name_zh=#{thematicNameZH}"
    })
    @Results({
            @Result(id = true, column = "thematic_id", property = "thematicId"),
            @Result(column = "thematic_name", property = "thematicName"),
            @Result(column = "thematic_name_zh", property = "thematicNameZH"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "insert_time", property = "updateTime"),
            @Result(column = "describe", property = "describe")
    })
    Thematic findThematicByThematicNameZH(String thematicNameZH);


    /**
     * description:判断thematicNameZH是否存在
     *
     * @return int
     */
    @Select({
            "select "
                    + "count(*) "
                    + "from "
                    + "thematic "
                    + "where "
                    + "thematic_name_zh=#{thematicNameZH}"
    })
    int findCountByThematicNameZH(String thematicNameZH);

}
