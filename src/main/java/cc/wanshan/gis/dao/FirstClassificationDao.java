package cc.wanshan.gis.dao;

import cc.wanshan.gis.entity.thematic.FirstClassification;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

/**
 * @author Li Cheng
 * @date 2019/5/25 14:18
 */
@Mapper
@Component
public interface FirstClassificationDao {
  @Insert("insert into "
      + "tb_first_classification ("
      + "first_classification_name,"
      + "thematic_id,"
      + "describe,"
      + "insert_time,"
      + "update_time "
      + ") values ("
      + "#{firstClassificationName},"
      + "#{thematic.thematicId},"
      + "#{describe},"
      + "#{insertTime,jdbcType=TIMESTAMP},"
      + "#{updateTime,jdbcType=TIMESTAMP}"
      + ")"
  )
  /**
   * description: 新增第一分类记录
   *
   * @param firstClassification 第一分类
   * @return int 新增记录数
   **/
  public int insertFirstClassification(FirstClassification firstClassification);
  @Select({"select * from "
      + "tb_first_classification "
      + "where "
      + "thematic_id=#{thematic.thematicId}"
  })
  @Results({
      @Result(id = true,column = "first_classification_id",property = "firstClassificationId"),
      @Result(column = "first_classification_name",property = "firstClassificationName"),
      @Result(column = "thematic_id",property = "thematic.thematicId"),
      @Result(column = "insert_time",property = "insertTime"),
      @Result(column = "update_time",property = "updateTime"),
      @Result(column = "describe",property = "describe"),
      @Result(column = "first_classification_id",property = "secondClassificationList",many = @Many(select = "cc.wanshan.gis.dao.SecondClassificationDao.findByFirstClassificationId",fetchType = FetchType.LAZY)),
  })
  /**
   * description: 根据专题Id查询第一分类
   *
   * @param thematicId 专题id
   * @return java.util.List<cc.wanshan.gis.entity.thematic.FirstClassification>
   **/
  public List<FirstClassification> findByThematicId(String thematicId);
}
