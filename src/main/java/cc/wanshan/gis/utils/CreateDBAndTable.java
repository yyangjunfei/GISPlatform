package cc.wanshan.gis.utils;
import cc.wanshan.gis.entity.metadata.ShpInfo;
import java.sql.*;
import java.util.List;

public class CreateDBAndTable
{
    public static void  createDB(String DBName)throws Exception{

        Class.forName("org.postgresql.Driver");

        //一开始必须填一个已经存在的数据库
        String url = "jdbc:postgresql://192.168.1.133:5432/China3857?useAffectedRows=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=serverTimezone=Asia/Shanghai";
        Connection conn = DriverManager.getConnection(url, "postgres", "wanshan");
        Statement stat = conn.createStatement();

        //如果数据库存在卸载数据库并且创建
/*      String sql= "drop database IF EXISTS "+DBName+";create DATABASE "+DBName+"";
        boolean bool=stat.execute(sql); //返回值为true时，表示执行的是查询语句,返回值为false时，执行的是更新语句或DDL语句
        if (!bool){
            System.out.println("数据库:"+DBName+":已经创建");
        }else {
            System.out.println("数据库:"+DBName+":创建失败");
        }
        stat.close();
        conn.close();*/

        //判断数据库DBName是否存在
        ResultSet rs = stat.executeQuery("SELECT u.datname FROM pg_catalog.pg_database u where u.datname='"+DBName.toLowerCase()+"'");

        if (rs.next()) {
            System.out.println("数据库:"+DBName+":存在");
        }else {
           //数据库DBName不存在
            System.out.println("创建数据库:"+DBName+"开始");
            //创建数据库hello
            stat.executeUpdate("create database "+DBName+"");
            stat.close();
            conn.close();
            System.out.println("数据库:"+DBName+":已经创建");
        }
    }

    public static void createTable(String dbName, String tabName, List<ShpInfo> geolist){
        try {
            createDB(dbName);

            //连接数据库
            String url = "jdbc:postgresql://192.168.1.133:5432/"+dbName.toLowerCase()+"?useAffectedRows=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=serverTimezone=Asia/Shanghai";
            Connection conn = DriverManager.getConnection(url, "postgres", "wanshan");
            Statement stat = conn.createStatement();
            conn.setAutoCommit(false);
            //判断数据库中的表是否存在
            ResultSet rs = stat.executeQuery("select count(*) from pg_class where relname = '"+tabName.toLowerCase()+"';");
            if (rs.next()){
                //数据库中的表不存在
                if (rs.getInt(1)==0){
                    //创建表tabName
                    String sql = "CREATE TABLE \"public\".\""+tabName+"\" (\"gid\" int4 NOT NULL,\"osm_id\" varchar(40),\"fclass\" varchar(255),\"code\" int4,\"name\" varchar(255),\"type\" varchar(255),\"geom\" varchar(255));";
                    stat.executeUpdate(sql);
                    //添加数据
                    PreparedStatement ps = null;
                    String sqlInsert = "insert into "+tabName+" values (?,?,?,?,?,?,?)";
                    ps = conn.prepareStatement(sqlInsert); // 批量插入时ps对象必须放到for循环外面

                    System.out.println("开始添加数据:"+geolist.size()+"条");
                    int count =0;
                    for (ShpInfo geo : geolist){
                        ps.setInt(1, geo.getGid());
                        ps.setString(2,geo.getOsm_id());
                        ps.setString(3,geo.getFclass());
                        ps.setInt(4,geo.getCode());
                        ps.setString(5,geo.getName());
                        ps.setString(6,geo.getType());
                        ps.setString(7,geo.getGeom().toString());
                        ps.addBatch();

                        if (count % 1000 == 0){
                            ps.executeBatch();
                            conn.commit();
                            ps.clearBatch();
                        }
                    }
                    // 剩余数量不足1000
                    ps.executeBatch();
                    conn.commit();
                    ps.clearBatch();
                    stat.close();
                    conn.close();
                    System.out.println("开始添加数据:"+geolist.size()+"成功");
                }else {
                    System.out.println("数据库中的表名"+tabName+"已经存在请修改发布表名");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
         // DBUtils.createTable("yang","yangjunfei");
          // DBUtils.createDB("yang");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}