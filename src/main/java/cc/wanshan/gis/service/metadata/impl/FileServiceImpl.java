package cc.wanshan.gis.service.metadata.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.metadata.FilePublishDao;
import cc.wanshan.gis.entity.metadata.ShpInfo;
import cc.wanshan.gis.entity.metadata.metadata;
import cc.wanshan.gis.service.metadata.FileService;
import cc.wanshan.gis.utils.base.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
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
        return uploadFile(file);
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
        File targetFile = new File(uploadFilePath);
        // 检测是否存在目录
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }else {
            delAllFile(targetFile.getPath());
        }

        for (MultipartFile file : fileList) {
            // 调用单文件
            Result result = upload(file);
            if (succeed(list, result)) {
                return result;
            }
        }
        return ResultUtil.success(list);
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

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public  boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                deleteFile(new File(path + "/" + tempList[i]));//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public boolean delFile(String filePath) {
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
    public List<ShpInfo> readSHP(String publishPath){

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


    /**
     * 上传文件
     *
     * @param file 文件
     * @return
     */
    private Result uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return ResultUtil.error(ResultCode.UPLOAD_FILE_NULL);
        }
        HashMap<String, Object> map = Maps.newHashMap();
        try {
            String filename = file.getOriginalFilename();
            File targetFile = new File(uploadFilePath + File.separator + filename);
            // 将上传文件保存到目标文件目录
            file.transferTo(targetFile);
            map.put("filename", filename);
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
        if (result.getCode() == 200) {
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
