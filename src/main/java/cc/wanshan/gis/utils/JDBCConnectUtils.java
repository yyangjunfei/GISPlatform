package cc.wanshan.gis.utils;


import cc.wanshan.gis.entity.Postgis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.*;

@Component
public class JDBCConnectUtils {
    private static final Logger logger= LoggerFactory.getLogger(JDBCConnectUtils.class);
    private static Postgis postgis;
    @Resource
    public  void setPostgis(Postgis postgis) {
        JDBCConnectUtils.postgis = postgis;
    }

    public static Connection getDBConnection(){
        Connection connection=null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(postgis.getUrl(),postgis.getUser(),postgis.getPassword());
        } catch (Exception e) {
            logger.warn("获取连接失败：getDBConnection::",e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public static void close(ResultSet rs, PreparedStatement stat, Connection conn) {
        logger.info("已进入：close::rs = [{}], stat = [{}], conn = [{}]",rs, stat, conn);
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.warn("在关闭游标的时候出错了：close::rs = [{}], stat = [{}], conn = [{}]",e.getMessage());
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                logger.warn("在关闭操作对象的时候出错了：close::rs = [{}], stat = [{}], conn = [{}]", e.getMessage());
                System.out.println("在关闭操作对象的时候出错了" + e.getMessage());
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warn("在关闭连接对象的时候出错了：close::rs = [{}], stat = [{}], conn = [{}]",e.getMessage());
                System.out.println("在关闭连接对象的时候出错了" + e.getMessage());
            }
        }
    }


}
