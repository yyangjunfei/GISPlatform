package cc.wanshan.gis.utils.geo;

import cc.wanshan.gis.entity.metadata.ShpInfo;
import cc.wanshan.gis.entity.metadata.metadata;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class publishData {

    private static final Logger LOG = LoggerFactory.getLogger(publishData.class);

    public static void publishShpData2DB(List<ShpInfo> geolist, metadata metadata) {
        try {
            //连接数据库
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://192.168.1.133:5432/dev?useAffectedRows=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=serverTimezone=Asia/Shanghai";
            Connection conn = DriverManager.getConnection(url, "postgres", "wanshan");
            Statement stat = conn.createStatement();
            conn.setAutoCommit(false);

            //判断数据库中的shp表名是否存在
            ResultSet rs = stat.executeQuery("select count(*) from pg_class where relname = '" + metadata.getLayerName().toLowerCase() + "';");
            if (rs.next()) {
                //数据库中的shp表名不存在
                if (rs.getInt(1) == 0) {
                    //创建表tabName
                    String type = geolist.get(0).getGeometry().getType();
                    String sql = "CREATE TABLE \"shpdb\".\"" + metadata.getLayerName() + "\" (\"id\" serial,\"fid\" varchar(255),\"type\" varchar(255),\"geo_type\" varchar(255),\"geom\" geometry (" + type + "),\"province\" varchar(255),\"city\" varchar(255),\"second_cla\" varchar(255),\"county\" varchar(255),\"first_clas\" varchar(255),\"name\" varchar(255),\"lon\" varchar(255),\"lat\" varchar(255),\"baidu_firs\" varchar(255),\"baidu_seco\" varchar(255),\"telephone\" varchar(255),\"addr\" varchar(255),constraint pk_" + metadata.getLayerName() + "_a_id primary key(id));";
                    stat.executeUpdate(sql);
                } else {
                    LOG.info("数据库中的表名" + metadata.getLayerName() + "已经存在请修改发布表名");
                }
            }

            //上传shp数据到数据库
            PreparedStatement ps = null;
            String sqlInsert = "INSERT INTO \"shpdb\".\"" + metadata.getLayerName() + "\" (fid,type,geo_type,geom,province,city,second_cla,county,first_clas,name,lon,lat,baidu_firs,baidu_seco,telephone,addr) VALUES (?,?,?,st_geomfromgeojson(?),?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sqlInsert); // 批量插入时ps对象必须放到for循环外面
            LOG.info("开始添加数据:" + geolist.size() + "条");
            int count = 0;
            for (ShpInfo geo : geolist) {

                //转换成为{"coordinates":[107.0828,32.9666],"type":"Point"}
                String geometry = JSONObject.toJSONString(geo.getGeometry());

                ps.setString(1, geo.getId());
                ps.setString(2, geo.getType());
                ps.setString(3, geo.getGeometry().getType());
                ps.setString(4, geometry);
                ps.setString(5, geo.getProperties().getProvince());
                ps.setString(6, geo.getProperties().getCity());
                ps.setString(7, geo.getProperties().getSecond_cla());
                ps.setString(8, geo.getProperties().getCounty());
                ps.setString(9, geo.getProperties().getFirst_clas());
                ps.setString(10, geo.getProperties().getName());
                ps.setString(11, geo.getProperties().getLon());
                ps.setString(12, geo.getProperties().getLat());
                ps.setString(13, geo.getProperties().getBaidu_firs());
                ps.setString(14, geo.getProperties().getBaidu_seco());
                ps.setString(15, geo.getProperties().getTelephone());
                ps.setString(16, geo.getProperties().getAddr());
                ps.addBatch();

                if (count % 1000 == 0) {
                    ps.executeBatch();
                    conn.commit();
                    ps.clearBatch();
                }
                count++;
            }
            // 剩余数量不足1000
            ps.executeBatch();
            conn.commit();
            ps.clearBatch();

            LOG.info("开始添加数据:" + geolist.size() + "成功");

            //创建上传图层属性信息LAYER_PROPERTIES表
            String sqlUpdta = "select count(*) from pg_class where relname ='LAYER_PROPERTIES'";
            ResultSet rst = stat.executeQuery(sqlUpdta);
            if (rst.next()) {
                //数据库中表不存在
                if (rst.getInt(1) == 0) {
                    String createSql = "CREATE TABLE \"shpdb\".\"LAYER_PROPERTIES\" (\"id\" serial,\"workspace_name\" varchar(255),\"store_name\" varchar(255),\"data_type\" varchar(255),\"layer_name\" varchar(255),\"safety_level\" varchar(255),\"vector_types\" varchar(255),\"style_name\" varchar(255),\"create_time\" timestamp,\"create_by\" varchar(255),\"delete\" int8,\"release_flag\" int8,constraint pk_test_a_id primary key(id)\n" + ");";
                    stat.executeUpdate(createSql);
                }
            }
            // 插入发布信息
            String insertSql = "INSERT INTO \"shpdb\".\"LAYER_PROPERTIES\" (workspace_name,store_name,data_type,layer_name,safety_level,vector_types,style_name,create_time,create_by,delete,release_flag)VALUES (?, ?, ?, ?, ?, ?, ?,now(),?,0,0);";
            PreparedStatement pres = conn.prepareStatement(insertSql);
            pres.setString(1, metadata.getWorkspaceName());
            pres.setString(2, metadata.getStoreName());
            pres.setString(3, metadata.getDataType());
            pres.setString(4, metadata.getLayerName());
            pres.setString(5, metadata.getSafetyLevel());
            pres.setString(6, metadata.getVectorTypes());
            pres.setString(7, metadata.getStyleName());
            pres.setString(8, metadata.getCreateBy());
            pres.executeUpdate();
            conn.commit();
            stat.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}