package cc.wanshan.gisdev.dao;


import cc.wanshan.gisdev.entity.usermanagement.Authority;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Li Cheng
 * @Description 
 * @Date 13:34 2019/3/12
 * @Param 
 * @return 
 **/
@Mapper
@Component
public interface AuthorityDao {
    /**
     * @Author Li Cheng
     * @Description 根据角色id查找对应的权限记录
     * @Date 9:53 2019/4/10
     * @Param [roleId]
     * @return java.util.List<cc.wanshan.demo.entity.Authority>
     **/
    @Select({"select * from tb_authority where author_id in (select author_id from tb_authority_role where role_id=#{roleId})"})
    public List<Authority> findAuthoritiesByRoleId(Integer roleId);
    /**
     * @Author Li Cheng
     * @Description  根据url查找对应的authority
     * @Date 9:57 2019/4/10
     * @Param [url]
     * @return cc.wanshan.demo.entity.Authority
     **/
    @Select({"select * from tb_authority where url=#{url}"})
    @Results({
            @Result(id = true,column = "author_id",property = "authorId"),
            @Result(column = "author_name",property = "authorName"),
            @Result(column = "url",property = "url"),
    })
    public Authority findByUrl(String url);
    /**
     * @Author Li Cheng
     * @Description 根据authorId查找对应的authority
     * @Date 9:58 2019/4/10
     * @Param [authorId]
     * @return cc.wanshan.demo.entity.Authority
     **/
    @Select({"select * from tb_authority where author_id=#{authorId}"})
    @Results({
            @Result(id = true,column = "author_id",property = "authorId"),
            @Result(column = "author_name",property = "authorName"),
            @Result(column = "url",property = "url"),
    })
    public Authority findByAuthorId(Integer authorId);
    /**
     * @Author Li Cheng
     * @Description 新增authority记录
     * @Date 10:03 2019/4/10
     * @Param [authority]
     * @return int
     **/
    @Insert({"insert into tb_authority (author_name,url) values (#{authorName},#{url})"})
    @Options(useGeneratedKeys = true,keyColumn = "author_id",keyProperty = "authorId")
    public int insertAuthority(Authority authority);
    /**
     * @Author Li Cheng
     * @Description  根据authorId修改authority记录
     * @Date 10:06 2019/4/10
     * @Param [authority]
     * @return int
     **/
    @Update({"update tb_authority set author_id=#{authorId},author_name=#{authorName},url=#{url} where author_id=#{authorId}"})
    public int updateAuthority(Authority authority);
    /**
     * @Author Li Cheng
     * @Description  根据authorId删除对应authority
     * @Date 10:11 2019/4/10
     * @Param [authorId]
     * @return int
     **/
    @Delete({"delete from tb_authority where author_id=#{authorId}"})
    public int deleteAuthority(Integer authorId);
}
