package cc.wanshan.gis.dao.cretelayerstable.impl;

import cc.wanshan.gis.dao.cretelayerstable.CreatLayerTableDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.utils.JDBCConnectUtils;
import cc.wanshan.gis.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Repository(value = "createLayerTableDaoImpl")
public class CreateLayerTableDaoImpl implements CreatLayerTableDao {

  private static final Logger logger = LoggerFactory.getLogger(CreateLayerTableDaoImpl.class);

  @Override
  public Result creatTable(String workspace, String layerName, String type, String epsg) {
    logger
        .info("creatTable::workspace = [{}], layerName = [{}], type = [{}], epsg = [{}]", workspace,
            layerName, type, epsg);
    Connection connection = JDBCConnectUtils.getDBConnection();
    PreparedStatement preparedStatement = null;
    String sql = null;
    int de = 0;
    try {
      sql = "CREATE TABLE  " + workspace + "." + "\"" + layerName + "\"" +
          " (gid  SERIAL PRIMARY KEY NOT NULL," +
          " osmid character varying(10) , " +
          " code smallint, " +
          " fclass character varying(28) , " +
          " name character varying(100)  , " +
          " geom geometry(" + type + ") " +
          " );";
      preparedStatement = connection.prepareStatement(sql);
      de = preparedStatement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    } finally {
      JDBCConnectUtils.close(null, preparedStatement, connection);
    }
    if (de == 0) {
      return ResultUtil.success();
    } else {
      logger.warn(layerName + "表创建失败");
      return ResultUtil.error(1, layerName + "创建失败");
    }
  }
}
