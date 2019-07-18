package cc.wanshan.gis.dao.layer.impl;


import cc.wanshan.gis.common.vo.Result;
import cc.wanshan.gis.dao.layer.CreateSchemaDao;
import cc.wanshan.gis.utils.JDBCConnectUtils;
import cc.wanshan.gis.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Repository(value = "createSchemaDaoImpl")
public class CreateSchemaDaoImpl implements CreateSchemaDao {
    private static final Logger logger = LoggerFactory.getLogger(CreateSchemaDaoImpl.class);

    @Override
    public Result createSchema(String schema) {
        logger.info("createSchema::schema = [{}]", schema);
        Connection connection = JDBCConnectUtils.getDBConnection();
        PreparedStatement preparedStatement = null;
        int de = 0;
        try {
            String sql = "create schema " + schema + ";";
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
            logger.info("创建schema" + schema + "成功");
            return ResultUtil.success();
        } else {
            logger.warn("创建schema" + schema + "失败");
            return ResultUtil.error(1, "创建schema" + schema + "失败");
        }
    }
}
