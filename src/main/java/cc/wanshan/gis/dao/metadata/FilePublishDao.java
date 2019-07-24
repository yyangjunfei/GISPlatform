package cc.wanshan.gis.dao.metadata;

import cc.wanshan.gis.entity.metadata.metadata;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface FilePublishDao {

    @Select({"select count(*) from pg_class where relname =#{tabName}"})
    int checkIfTablePShpExist(String tabName);


    @Update({"CREATE TABLE shpdb.${layerName}(ID serial, fid VARCHAR ( 255 ), TYPE VARCHAR ( 255 ),geo_type VARCHAR ( 255 ),geom geometry (${geoType}),province VARCHAR ( 255 ),city VARCHAR ( 255 ),second_cla VARCHAR ( 255 ),county VARCHAR ( 255 ),first_clas VARCHAR ( 255 ),NAME VARCHAR ( 255 ), lon VARCHAR ( 255 ), lat VARCHAR ( 255 ),baidu_firs VARCHAR ( 255 ),baidu_seco VARCHAR ( 255 ),telephone VARCHAR ( 255 ),addr VARCHAR ( 255 ),CONSTRAINT pk_${layerName}_a_id PRIMARY KEY ( ID ));"})
    int createShpTable(metadata metadata);


    //mybatis 单个对象插入
/*  @Insert({"INSERT INTO shpdb.${layerName} (fid,type,geo_type,geom,province,city,second_cla,county,first_clas,name,lon,lat,baidu_firs,baidu_seco,telephone,addr) VALUES (#{shpInfo.id},#{shpInfo.type},#{shpInfo.geometry.type},st_geomfromgeojson(#{geometryJson}),#{shpInfo.properties.province},#{shpInfo.properties.city},#{shpInfo.properties.second_cla},#{shpInfo.properties.county},#{shpInfo.properties.first_clas},#{shpInfo.properties.name},#{shpInfo.properties.lon},#{shpInfo.properties.lat},#{shpInfo.properties.baidu_firs},#{shpInfo.properties.baidu_seco},#{shpInfo.properties.telephone},#{shpInfo.properties.addr});"})
    int insertTableData(metadata metadata);*/

    //mybatis 批量插入
    @Insert({"<script>" +
            "INSERT INTO shpdb.${layerName} (fid,type,geo_type,geom,province,city,second_cla,county,first_clas,name,lon,lat,baidu_firs,baidu_seco,telephone,addr) VALUES "
            + "<foreach collection='shpInfoList' item='item' separator=',' > "
            + "(#{item.id},#{item.type},#{item.geometry.type},st_geomfromgeojson(#{item.geometryJson}),#{item.properties.province},#{item.properties.city},#{item.properties.second_cla},#{item.properties.county},#{item.properties.first_clas},#{item.properties.name},#{item.properties.lon},#{item.properties.lat},#{item.properties.baidu_firs},#{item.properties.baidu_seco},#{item.properties.telephone},#{item.properties.addr})"
            + "</foreach>"
            + "</script>"})
    int insertTableShpData(metadata metadata);


    @Select({"select count(*) from pg_class where relname =#{tabName}"})
    int checkIfTableLayerPropertieseExist(String tabName);


    @Update({"CREATE TABLE shpdb.\"${tableName}\"(id serial,workspace_name varchar(255),store_name varchar(255),layer_group varchar(255),data_type varchar(255),layer_name varchar(255),safety_level varchar(255),vector_types varchar(255),style_name varchar(255),attribution_department varchar(255),create_time timestamp,create_by varchar(255),delete int8,release_flag int8,constraint pk_${tableName}_a_id primary key(id));"})
    int createLayerPropertieseTable(@Param("tableName") String tableName);


    @Insert({"INSERT INTO shpdb.\"LAYER_PROPERTIES\" (workspace_name,store_name,layer_group,data_type,layer_name,safety_level,vector_types,style_name,attribution_department,create_time,create_by,delete,release_flag)VALUES (#{workspaceName},#{storeName},#{layerGroup},#{DataType},#{layerName},#{safetyLevel},#{vectorTypes},#{styleName},#{attributionDepartment},now(),#{createBy},0,0);"})
    int insertLayerPropertieseTableData(metadata metadata);

}
