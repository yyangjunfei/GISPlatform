package cc.wanshan.gis.dao.metadata;

import cc.wanshan.gis.entity.metadata.metadata;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DataManagementDao {

    @Select({"SELECT shpdb.\"LAYER_PROPERTIES\".\"id\", shpdb.\"LAYER_PROPERTIES\".workspace_name, shpdb.\"LAYER_PROPERTIES\".store_name, shpdb.\"LAYER_PROPERTIES\".data_type, shpdb.\"LAYER_PROPERTIES\".layer_name, shpdb.\"LAYER_PROPERTIES\".safety_level, shpdb.\"LAYER_PROPERTIES\".vector_types, shpdb.\"LAYER_PROPERTIES\".style_name, shpdb.\"LAYER_PROPERTIES\".create_time, shpdb.\"LAYER_PROPERTIES\".create_by, shpdb.\"LAYER_PROPERTIES\".\"delete\", shpdb.\"LAYER_PROPERTIES\".release_flag FROM shpdb.\"LAYER_PROPERTIES\" where shpdb.\"LAYER_PROPERTIES\".\"id\" =#{id}"})
    metadata shpData2Geoserver(int id);

    @Select({"SELECT shpdb.\"LAYER_PROPERTIES\".\"id\", shpdb.\"LAYER_PROPERTIES\".workspace_name, shpdb.\"LAYER_PROPERTIES\".store_name, shpdb.\"LAYER_PROPERTIES\".data_type, shpdb.\"LAYER_PROPERTIES\".layer_name, shpdb.\"LAYER_PROPERTIES\".safety_level, shpdb.\"LAYER_PROPERTIES\".vector_types, shpdb.\"LAYER_PROPERTIES\".style_name, shpdb.\"LAYER_PROPERTIES\".create_time, shpdb.\"LAYER_PROPERTIES\".create_by, shpdb.\"LAYER_PROPERTIES\".\"delete\", shpdb.\"LAYER_PROPERTIES\".release_flag FROM shpdb.\"LAYER_PROPERTIES\"  where shpdb.\"LAYER_PROPERTIES\".\"delete\" = 0 "})
    @Results(value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "workspace_name", property = "workspaceName"),
            @Result(column = "store_name", property = "storeName"),
            @Result(column = "DataType", property = "data_type"),
            @Result(column = "layer_name", property = "layerName"),
            @Result(column = "safety_level", property = "safetyLevel"),
            @Result(column = "vector_types", property = "vectorTypes"),
            @Result(column = "style_name", property = "styleName"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "create_by", property = "createBy"),
            @Result(column = "release_flag", property = "release_flag"),
    })
    List<metadata> findLayerProperties();

    @Update({
            "update shpdb.\"LAYER_PROPERTIES\" set delete =1 where shpdb.\"LAYER_PROPERTIES\".\"id\"= #{id}"
    })
    int deleteLayerPropertiesData(int id);

    @Update({
            "update shpdb.\"LAYER_PROPERTIES\" set workspace_name =#{workspaceName},store_name= #{storeName},data_type=#{DataType}, layer_name=#{layerName},safety_level =#{safetyLevel},vector_types= #{vectorTypes},style_name =#{styleName} where shpdb.\"LAYER_PROPERTIES\".\"id\" = #{id}"
    })
    int editLayerPropertiesData(metadata metadata);

    @Select({"<script>"
            + "SELECT shpdb.\"LAYER_PROPERTIES\".\"id\", shpdb.\"LAYER_PROPERTIES\".workspace_name, shpdb.\"LAYER_PROPERTIES\".store_name, shpdb.\"LAYER_PROPERTIES\".data_type, shpdb.\"LAYER_PROPERTIES\".layer_name, shpdb.\"LAYER_PROPERTIES\".safety_level, shpdb.\"LAYER_PROPERTIES\".vector_types, shpdb.\"LAYER_PROPERTIES\".style_name, shpdb.\"LAYER_PROPERTIES\".create_time, shpdb.\"LAYER_PROPERTIES\".create_by, shpdb.\"LAYER_PROPERTIES\".\"delete\", shpdb.\"LAYER_PROPERTIES\".release_flag FROM shpdb.\"LAYER_PROPERTIES\" where 1=1"
            + "<if test='workspaceName != null'>"
            + "and workspace_name=#{workspaceName}"
            + "</if>"
            + "<if test='storeName != null'>"
            + "and store_name=#{storeName}"
            + "</if>"
            + "<if test='DataType != null'>"
            + "and data_type=#{DataType}"
            + "</if>"
            + "<if test='layerName != null'>"
            + "and layer_name=#{layerName}"
            + "</if>"
            + "<if test='safetyLevel != null'>"
            + "and safety_level=#{safetyLevel}"
            + "</if>"
            + "<if test='vectorTypes != null'>"
            + "and vector_types=#{vectorTypes}"
            + "</if>"
            + "<if test='styleName != null'>"
            + "and style_name=#{styleName}"
            + "</if>"
            + "<if test='createBy != null'>"
            + "and create_by=#{createBy}"
            + "</if>"
            + "</script>"})

    @Results(value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "workspace_name", property = "workspaceName"),
            @Result(column = "store_name", property = "storeName"),
            @Result(column = "data_type", property = "DataType"),
            @Result(column = "layer_name", property = "layerName"),
            @Result(column = "safety_level", property = "safetyLevel"),
            @Result(column = "vector_types", property = "vectorTypes"),
            @Result(column = "style_name", property = "styleName"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "create_by", property = "createBy"),
            @Result(column = "release_flag", property = "release_flag"),
    })
    List<metadata> findLayerPropertiesData(metadata metadata);

    @Update({
            "update shpdb.\"LAYER_PROPERTIES\" set release_flag =1 where id = #{id}"
    })
    int changePublicationStatus(int id);

}
