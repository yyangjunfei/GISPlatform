package cc.wanshan.gis.dao.layer.impl;

import cc.wanshan.gis.common.vo.Result;
import cc.wanshan.gis.dao.layer.SearchLayerTableDao;
import cc.wanshan.gis.utils.JDBCConnectUtils;
import cc.wanshan.gis.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository(value = "searchLayerTableDaoImpl")
public class SearchLayerTableDaoImpl implements SearchLayerTableDao {
    private static final Logger logger = LoggerFactory.getLogger(SearchLayerTableDaoImpl.class);

    @Override
    public Result searchLayer(String layerName, String schema) {
        Connection connection = JDBCConnectUtils.getDBConnection();
        PreparedStatement preparedStatement = null;
        int count = 0;
        try {
            String sql = "select count(*) from pg_tables where schemaname=? and tablename=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, schema.toLowerCase());
            preparedStatement.setString(2, layerName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnectUtils.close(null, preparedStatement, connection);
        }
        if (count == 0) {
            logger.warn(layerName + "表不存在");
            return ResultUtil.error(1, "表不存在");
        } else {
            return ResultUtil.success();
        }
    }
}
