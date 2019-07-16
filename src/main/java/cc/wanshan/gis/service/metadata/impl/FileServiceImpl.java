package cc.wanshan.gis.service.metadata.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.dao.metadata.FilePublishDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.metadata.ShpInfo;
import cc.wanshan.gis.entity.metadata.metadata;
import cc.wanshan.gis.service.metadata.FileService;
import cc.wanshan.gis.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.mybatis.spring.SqlSessionTemplate;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Value("${file.path}")
    private String uploadFilePath;

    @Autowired
    private FilePublishDao filePublishDao;

    /**
     * 上传单文件
     *
     * @param file
     * @return
     */
    @Override
    public Result upload(MultipartFile file) {
        return uploadFile(file, uploadFilePath);
    }

    /**
     * 上传单文件，带文件夹路径
     *
     * @param file
     * @param folderPath
     * @return
     */
    @Override
    public Result upload(MultipartFile file, String folderPath) {
        return uploadFile(file, uploadFilePath + File.separator + folderPath);
    }

    /**
     * 上传多文件
     *
     * @param fileList
     * @return
     */
    @Override
    public Result upload(List<MultipartFile> fileList) {
        ArrayList<Map<String, String>> list = Lists.newArrayList();
        for (MultipartFile file : fileList) {
            // 调用单文件
            Result result = upload(file);
            if (succeed(list, result)) {
                return result;
            }
        }
        return ResultUtil.success(list);
    }

    /**
     * 上传多文件,带文件夹路径
     *
     * @param fileList
     * @param folderPath
     * @return
     */
    @Override
    public Result upload(List<MultipartFile> fileList, String folderPath) {
        List<Map<String, String>> pathList = Lists.newArrayList();
        for (MultipartFile file : fileList) {
            // 调用单文件
            Result result = upload(file, folderPath);
            if (succeed(pathList, result)) {
                return result;
            }
        }
        return ResultUtil.success(pathList);
    }

    /***
     * 删除文件夹和文件
     * @param file
     */

    @Override
    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    deleteFile(f);
                }
            }
            file.delete();
        }
    }

    public boolean  delFile(String filePath) {
        File file = new File(filePath);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                LOG.info("删除单个文件" + filePath + "成功！");
                return true;
            } else {
                LOG.error("删除单个文件" + filePath + "失败！");
                return false;
            }
        } else {
            LOG.warn("删除单个文件失败：" + filePath + "不存在！");
            return false;
        }
    }


    @Override
    public List<ShpInfo> readSHP(String publishPath) {

        // 一个数据存储实现，允许从Shapefiles读取和写入
        ShapefileDataStore shpDataStore = null;
        File file = new File(publishPath);
        List<ShpInfo> geolist = new ArrayList<ShpInfo>();
        try {
            shpDataStore = new ShapefileDataStore(file.toURI().toURL());
            shpDataStore.setCharset(Charset.forName("GBK"));
            // 获取这个数据存储保存的类型名称数组
            // getTypeNames:获取所有地理图层
            String typeName = shpDataStore.getTypeNames()[0];

            // 通过此接口可以引用单个shapefile、数据库表等。与数据存储进行比较和约束
            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = null;
            featureSource = (FeatureSource<SimpleFeatureType, SimpleFeature>) shpDataStore.getFeatureSource(typeName);

            // 一个用于处理FeatureCollection的实用工具类。提供一个获取FeatureCollection实例的机制
            FeatureCollection<SimpleFeatureType, SimpleFeature> result = featureSource.getFeatures();
            FeatureIterator<SimpleFeature> iterator = result.features();
            FeatureJSON fjson = new FeatureJSON();
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                StringWriter writer = new StringWriter();
                fjson.writeFeature(feature, writer);
                // 构建实体
                ShpInfo shpInfo = JSONObject.parseObject(writer.toString(), ShpInfo.class);

                //单独封装geometryJson
                shpInfo.setGeometryJson(JSONObject.toJSONString(shpInfo.getGeometry()));

                geolist.add(shpInfo);

            } // end 最外层 while

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return geolist;
    }

    @Override
    public void publishShpData2DB(List<ShpInfo> shpInfoList, metadata metadata) {

        String shpTableName = metadata.getLayerName();
        int shpTabExist = filePublishDao.checkIfTablePShpExist(shpTableName);

        if (shpTabExist == 0) {  //表不存在创建shp表
            filePublishDao.createShpTable(metadata);
        }
        // 分页迭代List集合, 使用mybatis批量每1000条记录插入
        int totalcount = shpInfoList.size();
        int pagecount = 0;
        int pagesize = 1000;

        int m = totalcount % pagesize;
        if (m > 0) {
            pagecount = totalcount / pagesize + 1;
        } else {
            pagecount = totalcount / pagesize;
        }
        for (int i = 1; i <= pagecount; i++) {
            if (m == 0) {
                List<ShpInfo> subList = shpInfoList.subList((i - 1) * pagesize, pagesize * (i));
                metadata.setShpInfoList(subList);
                //插入shp数据到表
                filePublishDao.insertTableShpData(metadata);
            } else {
                if (i == pagecount) {
                    List<ShpInfo> subList = shpInfoList.subList((i - 1) * pagesize, totalcount);
                    metadata.setShpInfoList(subList);
                    //插入shp数据到表
                    filePublishDao.insertTableShpData(metadata);
                } else {
                    List<ShpInfo> subList = shpInfoList.subList((i - 1) * pagesize, pagesize * (i));
                    metadata.setShpInfoList(subList);
                    //插入shp数据到表
                    filePublishDao.insertTableShpData(metadata);
                }
            }
        }

        int layerPropertieseExist = filePublishDao.checkIfTableLayerPropertieseExist("LAYER_PROPERTIES");
        if (layerPropertieseExist == 0) {  //表不存在创建LAYER_PROPERTIES表
            filePublishDao.createLayerPropertieseTable("LAYER_PROPERTIES");
        }
        // 插入发布属性信息
        filePublishDao.insertLayerPropertieseTableData(metadata);
    }

    //JDBC 批量插入
   /* @Override
    public void publishShpData2DB(List<ShpInfo> geolist, metadata metadata) {
        try {
            //连接数据库
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://192.168.1.133:5432/test?useAffectedRows=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=serverTimezone=Asia/Shanghai";
            Connection conn = DriverManager.getConnection(url, "postgres", "wanshan");

            Statement stat = conn.createStatement();
            conn.setAutoCommit(false);

            //判断数据库中的shp表名是否存在
            ResultSet rs = stat.executeQuery("select count(*) from pg_class where relname = '"+metadata.getLayerName().toLowerCase()+"';");
            if (rs.next()){
                //数据库中的shp表名不存在
                if (rs.getInt(1)==0){
                    //创建表tabName
                    String type = geolist.get(0).getGeometry().getType();
                    String sql = "CREATE TABLE \"shpdb\".\""+metadata.getLayerName()+"\" (\"id\" serial,\"fid\" varchar(255),\"type\" varchar(255),\"geo_type\" varchar(255),\"geom\" geometry ("+type+"),\"province\" varchar(255),\"city\" varchar(255),\"second_cla\" varchar(255),\"county\" varchar(255),\"first_clas\" varchar(255),\"name\" varchar(255),\"lon\" varchar(255),\"lat\" varchar(255),\"baidu_firs\" varchar(255),\"baidu_seco\" varchar(255),\"telephone\" varchar(255),\"addr\" varchar(255),constraint pk_"+metadata.getLayerName()+"_a_id primary key(id));";
                    stat.executeUpdate(sql);
                }else {
                    LOG.info("数据库中的表名"+metadata.getLayerName()+"已经存在请修改发布表名");
                }
            }
            //上传shp数据到数据库
            PreparedStatement ps = null;
            String sqlInsert = "INSERT INTO \"shpdb\".\""+metadata.getLayerName()+"\" (fid,type,geo_type,geom,province,city,second_cla,county,first_clas,name,lon,lat,baidu_firs,baidu_seco,telephone,addr) VALUES (?,?,?,st_geomfromgeojson(?),?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sqlInsert); // 批量插入时ps对象必须放到for循环外面
            LOG.info("开始添加数据:"+geolist.size()+"条");
            int count =0;
            for (ShpInfo geo : geolist){

                //转换成为{"coordinates":[107.0828,32.9666],"type":"Point"}
                String geometry= JSONObject.toJSONString(geo.getGeometry());
                ps.setString(1,geo.getId());
                ps.setString(2,geo.getType());
                ps.setString(3,geo.getGeometry().getType());
                ps.setString(4,geometry);
                ps.setString(5,geo.getProperties().getProvince());
                ps.setString(6,geo.getProperties().getCity());
                ps.setString(7,geo.getProperties().getSecond_cla());
                ps.setString(8,geo.getProperties().getCounty());
                ps.setString(9,geo.getProperties().getFirst_clas());
                ps.setString(10,geo.getProperties().getName());
                ps.setString(11,geo.getProperties().getLon());
                ps.setString(12,geo.getProperties().getLat());
                ps.setString(13,geo.getProperties().getBaidu_firs());
                ps.setString(14,geo.getProperties().getBaidu_seco());
                ps.setString(15,geo.getProperties().getTelephone());
                ps.setString(16,geo.getProperties().getAddr());
                ps.addBatch();

                if (count % 1000 == 0){
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

            LOG.info("开始添加数据:"+geolist.size()+"成功");

            //创建上传图层属性信息LAYER_PROPERTIES表
            String sqlUpdta= "select count(*) from pg_class where relname ='LAYER_PROPERTIES'";
            ResultSet rst= stat.executeQuery(sqlUpdta);
            if (rst.next()){
                //数据库中表不存在
                if (rst.getInt(1)==0){
                    String createSql="CREATE TABLE \"shpdb\".\"LAYER_PROPERTIES\" (\"id\" serial,\"workspace_name\" varchar(255),\"store_name\" varchar(255),\"data_type\" varchar(255),\"layer_name\" varchar(255),\"safety_level\" varchar(255),\"vector_types\" varchar(255),\"style_name\" varchar(255),\"create_time\" timestamp,\"create_by\" varchar(255),\"delete\" int8,\"release_flag\" int8,constraint pk_test_a_id primary key(id)\n" + ");";
                    stat.executeUpdate(createSql);
                }
            }
            // 插入发布信息
            String insertSql = "INSERT INTO \"shpdb\".\"LAYER_PROPERTIES\" (workspace_name,store_name,data_type,layer_name,safety_level,vector_types,style_name,create_time,create_by,delete,release_flag)VALUES (?, ?, ?, ?, ?, ?, ?,now(),?,0,0);";
            PreparedStatement pres= conn.prepareStatement(insertSql);
            pres.setString(1,metadata.getWorkspaceName());
            pres.setString(2,metadata.getStoreName());
            pres.setString(3,metadata.getDataType());
            pres.setString(4,metadata.getLayerName());
            pres.setString(5,metadata.getSafetyLevel());
            pres.setString(6,metadata.getVectorTypes());
            pres.setString(7,metadata.getStyleName());
            pres.setString(8,metadata.getCreateBy());
            pres.executeUpdate();
            conn.commit();
            stat.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 上传文件
     *
     * @param file     文件
     * @param filePath 上传的目标路径
     * @return
     */
    private Result uploadFile(MultipartFile file, String filePath) {
        if (file.isEmpty()) {
            return ResultUtil.error(ResultCode.UPLOAD_FILE_NULL);
        }

        HashMap<String, Object> map = Maps.newHashMap();
        try {
            String filename = file.getOriginalFilename();

            map.put("filename", filename);

            File targetFile = new File(filePath + File.separator + filename);
            // 检测是否存在目录
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            // 将上传文件保存到目标文件目录
            file.transferTo(targetFile);

            map.put("filePath", targetFile.getAbsolutePath());
            return ResultUtil.success(map);

        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(ResultCode.UPLOAD_FAIL);
        }
    }

    /**
     * 判断是否上传成功
     *
     * @param pathList
     * @param result
     * @return
     */
    private boolean succeed(List<Map<String, String>> pathList, Result result) {
        if (result.getCode() == 0) {
            pathList.add((Map) result.getData());
        } else {
            for (Map map : pathList) {
                delFile(map.get("filePath").toString());
            }
            result.setMsg(result.getMsg() + ",已回滚");
            return true;
        }
        return false;
    }
}
