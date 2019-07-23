package cc.wanshan.gis.dao.plot.of2d.impl;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.plot.of2d.DropLayerDao;
import cc.wanshan.gis.utils.JDBCConnectUtils;
import cc.wanshan.gis.utils.base.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository(value = "dropLayerDaoImpl")
public class DropLayerDaoImpl implements DropLayerDao {
    private static final Logger logger = LoggerFactory.getLogger(DropLayerDaoImpl.class);

    @Override
    public Result dropLayer(String schema, String layerName) {
        logger.info("dropLayer::schema = [{}], layerName = [{}]", schema, layerName);
        Connection connection = JDBCConnectUtils.getDBConnection();
        PreparedStatement preparedStatement = null;
        int de = 0;
        try {
            String sql = "drop table if exists " + schema + " ." + "\"" + layerName + "\"" + "; ";
            preparedStatement = connection.prepareStatement(sql);
            de = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnectUtils.close(null, preparedStatement, connection);
        }
        if (de == 0) {
            return ResultUtil.success();
        } else {
            logger.warn(layerName + "删除失败");
            return ResultUtil.error(1, "删除失败");
        }
    }
}
