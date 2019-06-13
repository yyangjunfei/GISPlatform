package cc.wanshan.gis.utils;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class importDB2Es {
    private static Logger LOG = LoggerFactory.getLogger(importDB2Es.class);
    public final static int BULK_COUNT = 20000;
    public static  TransportClient transportClient;

  public static BulkProcessor createBulkProcessor() {
      // 初始化Bulk处理器
    BulkProcessor bulkProcessor = BulkProcessor.builder(transportClient, new BulkProcessor.Listener() {
         long begin = 0;
         long cost;
         int count = 0;
         @Override
          public void beforeBulk(long l, BulkRequest bulkRequest) {
                begin = System.currentTimeMillis();
           }
            @Override
           public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                cost = (System.currentTimeMillis() - begin) / 1000;
                count += bulkRequest.numberOfActions();
                LOG.info("bulk success. size:[{" + count + "}] cost:[{" + cost + "}s]");
          }
           @Override
           public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                LOG.info("bulk update has failures, will retry:" + throwable);
               }
           })
           .setBulkActions(20000)// 批量导入个数
           .setBulkSize(new ByteSizeValue(200, ByteSizeUnit.MB))// 满xMB进行导入
           .setConcurrentRequests(10)// 并发数
           .setFlushInterval(TimeValue.timeValueSeconds(50))// 冲刷间隔
           .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1), 3)) // 重试3次，间隔1s
           .build();
        return bulkProcessor;
    }

    public static long importData(String dbURL,String dbUserName,String dbPassword,String driverClassName,String sql,String esindexName,String esTypeName)throws InterruptedException{
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e1){
            e1.printStackTrace();
        }
        boolean isLastEmpty = true;
        long count = 0;
        int blukNum = 0;
        List<String> columnName = Arrays.asList("gid", "sjlxdm", "xzqdmc", "bz", "geom");
        Connection con = null;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(dbURL,dbUserName,dbPassword);
            ps = (PreparedStatement) con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(0);
            ps.setFetchDirection(ResultSet.FETCH_REVERSE);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            long allStart = System.currentTimeMillis();//记录批量执行的开始时间
            BulkProcessor bulkProcessor = createBulkProcessor();
            while (rs.next()) {             // while控制行数
                blukNum++;
                Map<String, String> map = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    String name = rsmd.getColumnName(i);
                    if (columnName.contains(name)) {
                        String value = rs.getString(i);
                        if (value != null && !"".equals(value.trim()) && value.trim().length() > 0){
                            map.put(name, value);
                        }
                    }
                }
                bulkProcessor.add(new IndexRequest(esindexName, esTypeName, blukNum + "").source(map));
            }//end while
            // 关闭
            bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
            LOG.info("取回数据量为  " + blukNum + " 行！");
            long end = System.currentTimeMillis();//记录批量入库结束的开始时间
            LOG.info("共耗时  " + (end - allStart) / 1000 + " s！");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
/*        if (transportClient != null) {
            transportClient.close();
        }*/
        return count;
    }
}
