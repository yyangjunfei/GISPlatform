package cc.wanshan.gis.dao.searchschema.impl;

import cc.wanshan.gis.dao.searchschema.SearchSchemaDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.utils.JDBCConnectUtils;
import cc.wanshan.gis.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository(value = "searchSchemaDaoImpl")
public class SearchSchemaDaoImpl implements SearchSchemaDao {
    private static final Logger logger= LoggerFactory.getLogger(SearchSchemaDaoImpl.class);
    @Override
    public Result searchSchema(String schema) {
        Connection connection = JDBCConnectUtils.getDBConnection();
        PreparedStatement preparedStatement=null;
        int count=0;
        try {
            String sql="SELECT count (*)FROM information_schema.schemata where schema_name = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,schema);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCConnectUtils.close(null,preparedStatement,connection);
        }
        if (count==0){
            return ResultUtil.error(1,"schema不存在");
        }else {
            return ResultUtil.success();
        }
    }
}
