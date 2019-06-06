package cc.wanshan.gis.dao.insertfeature.impl;

import cc.wanshan.gis.dao.insertfeature.FeatureDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Feature;
import cc.wanshan.gis.utils.JDBCConnectUtils;
import cc.wanshan.gis.utils.ResultUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository(value = "insertLayerDaoImpl")
public class FeatureDaoImpl implements FeatureDao {

  private static final Logger logger = LoggerFactory.getLogger(FeatureDaoImpl.class);

  @Override
  public Result insertFeatures(List<Feature> features, String tableName, String schema) {
    logger.info("insertFeatures::features = [{}], tableName = [{}], schema = [{}]", features,
        tableName, schema);
    Connection connection = JDBCConnectUtils.getDBConnection();
    PreparedStatement preparedStatement = null;
    int[] de = new int[0];
    try {
      String sql = "INSERT INTO "+schema+"."+"\""+tableName+"\""+"(" +
          " fclass, name, geom)" +
          "VALUES ( ?,?,st_geomfromgeojson(?))";
      preparedStatement = connection.prepareStatement(sql);
      for (Feature feature : features) {
        preparedStatement.setString(1, feature.getProperties().getFclass());
        preparedStatement.setString(2, feature.getProperties().getName());
        preparedStatement.setString(3, feature.getGeometry().toString());
        preparedStatement.addBatch();
      }
      de = preparedStatement.executeBatch();
      System.out.println(de.length);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      JDBCConnectUtils.close(null, preparedStatement, connection);
    }
    if (de.length != 0) {
      return ResultUtil.success();
    } else {
      logger.warn(tableName + "插入失败");
      return ResultUtil.error(1, "插入失败");
    }
  }

  @Override
  public List<Feature> findFeatureByLayerId(String layerId) {
  /*  logger.info("findFeatureByLayerId::layerId = [{}]",layerId);
    Connection connection = JDBCConnectUtils.getDBConnection();
    PreparedStatement preparedStatement = null;
    int[] de = new int[0];
    try {
      String sql = "INSERT INTO "+schema+"."+"\""+tableName+"\""+"(" +
          " fclass, name, geom)" +
          "VALUES ( ?,?,st_geomfromgeojson(?))";
      preparedStatement = connection.prepareStatement(sql);
      for (Feature feature : features) {
        preparedStatement.setString(1, feature.getProperties().getFclass());
        preparedStatement.setString(2, feature.getProperties().getName());
        preparedStatement.setString(3, feature.getGeometry().toString());
        preparedStatement.addBatch();
      }
      de = preparedStatement.executeBatch();
      System.out.println(de.length);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      JDBCConnectUtils.close(null, preparedStatement, connection);
    }*/
    return null;
  }
}

