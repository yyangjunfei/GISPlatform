package cc.wanshan.gis.dao.layer;

import cc.wanshan.gis.entity.thematic.SecondClassification;
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
 * @date 2019/5/25 14:26
 */
@Mapper
@Component
public interface SecondClassificationDao {


  /**
   * description: 新增第二分类记录数
   *
   * @param secondClassification 第二分类
   * @return int
   **/
  @Insert("insert into "
      + "second_classification "
      + "(second_classification_name,"
      + "first_classification_id,"
      + "describe,"
      + "insert_time,"
      + "update_time "
      + ") values ("
      + "#{secondClassificationName},"
      + "#{firstClassification.firstClassificationId},"
      + "#{describe},"
      + "#{insertTime,jdbcType=TIMESTAMP},"
      + "#{updateTime,jdbcType=TIMESTAMP}"
      + ")"
  )
  int insertSecondClassification(SecondClassification secondClassification);


  /**
   * description: 查询第二分类
   *
   * @param firstClassificationId 第一分类id
   * @return java.util.List<cc.wanshan.gis.entity.thematic.SecondClassification>
   **/
  @Select({"select * from "
      + "second_classification "
      + "where "
      + "first_classification_id=#{firstClassification.firstClassificationId}"
  })
  @Results({
      @Result(id = true, column = "second_classification_id", property = "secondClassificationId"),
      @Result(column = "second_classification_name", property = "secondClassificationName"),
      @Result(column = "first_classification_Id", property = "firstClassification.firstClassificationId"),
      @Result(column = "insert_time", property = "insertTime"),
      @Result(column = "update_time", property = "updateTime"),
      @Result(column = "describe", property = "describe"),
      @Result(column = "second_classification_id", property = "layer", many = @Many(select = "cc.wanshan.gis.dao.layer.LayerDao.findLayerBySecondClassId", fetchType = FetchType.LAZY)),
  })
  List<SecondClassification> findByFirstClassificationId(String firstClassificationId);
}
