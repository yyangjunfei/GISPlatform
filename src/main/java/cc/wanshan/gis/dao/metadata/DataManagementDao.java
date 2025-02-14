package cc.wanshan.gis.dao.metadata;
import cc.wanshan.gis.entity.metadata.metadata;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import java.util.List;

@Mapper
@Component
public interface DataManagementDao {

    @Select({"SELECT shpdb.\"LAYER_PROPERTIES\".\"id\", shpdb.\"LAYER_PROPERTIES\".workspace_name, shpdb.\"LAYER_PROPERTIES\".store_name, shpdb.\"LAYER_PROPERTIES\".layer_group, shpdb.\"LAYER_PROPERTIES\".data_type, shpdb.\"LAYER_PROPERTIES\".layer_name, shpdb.\"LAYER_PROPERTIES\".safety_level, shpdb.\"LAYER_PROPERTIES\".vector_types, shpdb.\"LAYER_PROPERTIES\".style_name, shpdb.\"LAYER_PROPERTIES\".attribution_department, shpdb.\"LAYER_PROPERTIES\".create_time, shpdb.\"LAYER_PROPERTIES\".create_by, shpdb.\"LAYER_PROPERTIES\".\"delete\", shpdb.\"LAYER_PROPERTIES\".release_flag FROM shpdb.\"LAYER_PROPERTIES\" where shpdb.\"LAYER_PROPERTIES\".\"id\" =#{id}"})
    metadata shpData2Geoserver(int id);

    @Select({"SELECT shpdb.\"LAYER_PROPERTIES\".\"id\", shpdb.\"LAYER_PROPERTIES\".workspace_name, shpdb.\"LAYER_PROPERTIES\".store_name, shpdb.\"LAYER_PROPERTIES\".layer_group, shpdb.\"LAYER_PROPERTIES\".data_type, shpdb.\"LAYER_PROPERTIES\".layer_name, shpdb.\"LAYER_PROPERTIES\".safety_level, shpdb.\"LAYER_PROPERTIES\".vector_types, shpdb.\"LAYER_PROPERTIES\".style_name, shpdb.\"LAYER_PROPERTIES\".attribution_department, shpdb.\"LAYER_PROPERTIES\".create_time, shpdb.\"LAYER_PROPERTIES\".create_by, shpdb.\"LAYER_PROPERTIES\".\"delete\", shpdb.\"LAYER_PROPERTIES\".release_flag FROM shpdb.\"LAYER_PROPERTIES\" where shpdb.\"LAYER_PROPERTIES\".\"delete\" =0"})
    @Results(id="metadataMap",value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "workspace_name", property = "workspaceName"),
            @Result(column = "store_name", property = "storeName"),
            @Result(column = "layer_group", property = "layerGroup"),
            @Result(column = "data_type", property = "DataType"),
            @Result(column = "layer_name", property = "layerName"),
            @Result(column = "safety_level", property = "safetyLevel"),
            @Result(column = "attribution_department", property = "attributionDepartment"),
            @Result(column = "vector_types", property = "vectorTypes"),
            @Result(column = "style_name", property = "styleName"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "create_by", property = "createBy"),
            @Result(column = "release_flag", property = "release_flag"),
    })
    List<metadata> findLayerProperties();

    @Update({
            "<script>"
            +"<foreach collection=\"layerIds\" item=\"id\" index=\"index\" open=\"\" close=\"\" separator=\";\">"
            +"update shpdb.\"LAYER_PROPERTIES\" set delete =1 where shpdb.\"LAYER_PROPERTIES\".\"id\"= #{id}"
            +"</foreach>"
            + "</script>"
    })
    int deleteLayerPropertiesData(@Param(value = "layerIds") Integer [] layerIds);

    @Update({
            "update shpdb.\"LAYER_PROPERTIES\" set workspace_name =#{workspaceName},store_name= #{storeName},layer_group=#{layerGroup},data_type=#{DataType}, layer_name=#{layerName},safety_level =#{safetyLevel},attribution_department=#{attributionDepartment},vector_types= #{vectorTypes},style_name =#{styleName} where shpdb.\"LAYER_PROPERTIES\".\"id\" = #{id}"
    })
    int editLayerPropertiesData(metadata metadata);

    @Select({"<script>"
            + "SELECT shpdb.\"LAYER_PROPERTIES\".workspace_name, shpdb.\"LAYER_PROPERTIES\".store_name, shpdb.\"LAYER_PROPERTIES\".layer_group, shpdb.\"LAYER_PROPERTIES\".data_type, shpdb.\"LAYER_PROPERTIES\".layer_name, shpdb.\"LAYER_PROPERTIES\".safety_level, shpdb.\"LAYER_PROPERTIES\".vector_types, shpdb.\"LAYER_PROPERTIES\".style_name, shpdb.\"LAYER_PROPERTIES\".attribution_department, shpdb.\"LAYER_PROPERTIES\".create_by FROM shpdb.\"LAYER_PROPERTIES\" WHERE 1=1"
            + "<if test='workspaceName != null'>"
            + "and workspace_name=#{workspaceName}"
            + "</if>"
            + "<if test='storeName != null'>"
            + "and store_name=#{storeName}"
            + "</if>"
            + "<if test='layerGroup != null'>"
            + "and layer_group=#{layerGroup}"
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
            + "<if test='attributionDepartment != null'>"
            + "and attribution_department=#{attributionDepartment}"
            + "</if>"
            + "<if test='createBy != null'>"
            + "and create_by=#{createBy}"
            + "</if>"
            + "</script>"})

    @ResultMap(value="metadataMap")
    List<metadata> findLayerPropertiesData(metadata metadata);

    @Update({
            "update shpdb.\"LAYER_PROPERTIES\" set release_flag =#{release_flag} where id = #{id}"
    })
    int changePublicationStatus(int release_flag,int id);
}
