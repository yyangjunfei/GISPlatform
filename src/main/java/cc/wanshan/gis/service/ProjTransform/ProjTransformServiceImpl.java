package cc.wanshan.gis.service.ProjTransform;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.metadata.Properties;
import cc.wanshan.gis.entity.metadata.ShpInfo;
import cc.wanshan.gis.entity.metadata.metadata;
import cc.wanshan.gis.service.metadata.FileService;
import cc.wanshan.gis.utils.base.ResultUtil;
import cc.wanshan.gis.utils.transform.FileUtils;
import cc.wanshan.gis.utils.transform.ProjTransform;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.*;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/***
 * @author  Yang
 * @date    2019-9-3
 * @version [v1.0]
 * @descriptionweb shp文件坐标转换
 */

@Service
@Slf4j
public class ProjTransformServiceImpl implements ProjTransformService {

    @Autowired
    private FileService fileService;

    /***
     *
     * @param shpFiles  上传需要转换的shp文件
     * @return
     */

    @Override
    public Result tranfeWgs84ToGcj02ToDB(MultipartFile[] shpFiles) {
        log.info("开始转换shp文件到DB!");
        Map<String,String> map = (Map<String,String>)getUploadFilePathAndName(shpFiles).getData();
        String shppath = map.get("filePath"); //获取文件上传路径
        String fileName = map.get("fileName"); //获取文件名
        String typeName;
        ShapefileDataStore shpDataStore = null;
        FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection;
        List<ShpInfo> shpInfoList = Lists.newArrayList();
        try {
            log.info("读取shp文件内容!");
            shpDataStore = new ShapefileDataStore(new File(shppath).toURI().toURL());
            shpDataStore.setCharset(Charset.forName("GBK"));
            typeName= shpDataStore.getTypeNames()[0];
            featureSource = shpDataStore.getFeatureSource(typeName);
            Filter filter = Filter.INCLUDE;     // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)")
            collection = featureSource.getFeatures(filter);
            FeatureIterator<SimpleFeature> features = collection.features();
            FeatureJSON fjson = new FeatureJSON();
            StringWriter stringWriter =null;
            Coordinate coor =null;
            while (features.hasNext()) {
                //转换 feature
                SimpleFeature feature = features.next();
                List<Object> list = feature.getAttributes();
                Object obj = list.get(0);
                if (obj instanceof LineString || obj instanceof MultiLineString) {
                    Geometry line = ((Geometry) obj);
                    int parts = line.getNumGeometries();
                    for (int i = 0; i < parts; i++) {
                        LineString l = (LineString) line.getGeometryN(i);
                        for (int j = 0, num = l.getNumPoints(); j < num; j++) {
                            coor = l.getCoordinateN(j);
                            log.info("转换前:"+coor.x+"::"+coor.y);
                            double[] xy = cc.wanshan.gis.utils.transform.ProjTransform.wgs84togcj02(coor.x,coor.y);  // wgs84togcj02
                            log.info("转换后:"+xy[0]+"::"+xy[1]);
                            Coordinate newcoor = new Coordinate(xy[0], xy[1]);
                            coor.setCoordinate(newcoor);
                        }
                    }
                } else if (obj instanceof Point) {
                    Point pt = (Point) obj;
                    coor = pt.getCoordinate();
                    log.info("转换前:"+coor.x+"::"+coor.y);
                    double[] xy = cc.wanshan.gis.utils.transform.ProjTransform.wgs84togcj02(coor.x,coor.y);  // wgs84togcj02
                    log.info("转换后:"+xy[0]+"::"+xy[1]);
                    coor.setCoordinate(new Coordinate(xy[0], xy[1]));
                }

                //转换到新的 SimpleFeature
                SimpleFeature fNew = new SimpleFeatureImpl(feature.getAttributes(), feature.getType(),feature.getIdentifier());
                stringWriter = new StringWriter();
                fjson.writeFeature(fNew, stringWriter);

                // 构建实体
                ShpInfo shpInfo = JSONObject.parseObject(stringWriter.toString(), ShpInfo.class);
                //单独封装geometryJson
                shpInfo.setGeometryJson(JSONObject.toJSONString(shpInfo.getGeometry()));
                //单独封装properties
                Properties properties = new Properties();
                properties.setLon(String.valueOf(coor.x));
                properties.setLat(String.valueOf(coor.y));
                shpInfo.setProperties(properties);
                shpInfoList.add(shpInfo);
            }

            log.info("转换shp文件内容完毕!,关闭IO流");
            stringWriter.close();
            features.close();
            shpDataStore.dispose();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        metadata metadata = new metadata();
        String fileOtherName = FileUtils.getFileName(fileName);
        metadata.setLayerName(fileOtherName);
        //设置geometryType
        metadata.setGeoType(shpInfoList.get(0).getGeometry().getType());
        //shp数据存储到数据库
        log.info("将shp文件内容存储在数据库");
        fileService.publishShpData2DB(shpInfoList, metadata);
        log.info("shp文件已经存储在数据库");
        return ResultUtil.success(200,"文件已经存储在数据库");
    }

    /***
     *
     * @param shpFiles      上传需要转换的shp文件
     * @param destShpPath   输出转换的shp文件
     * @return
     */

    @Override
    public Result tranfeWgs84ToGcj02ToShpFile(MultipartFile[] shpFiles, String destShpPath) {
        log.info("开始转换shp文件到目标路径");
        Map<String,String> map = (Map<String,String>)getUploadFilePathAndName(shpFiles).getData();
        String originshppath = map.get("filePath"); //获取文件上传路径
        String fileName = map.get("fileName"); //获取文件名
        File destPath = new File(destShpPath);
        // 检测是否存在目录
        if (!destPath.exists()) {
            log.info("创建目录:"+destPath.getAbsolutePath());
            destPath.mkdirs();
        }

        File targetFile = new File(destPath.getAbsolutePath() + File.separator + fileName);

        //读取源shp文件
        log.info("读取源shp文件内容!");
        ShapefileDataStore shpDataStore =null;
        SimpleFeature feature =null;
        FeatureIterator<SimpleFeature> features =null;
        try {
            shpDataStore = new ShapefileDataStore(new File(originshppath).toURI().toURL());
            shpDataStore.setCharset(Charset.forName("GBK"));
            String typeName= shpDataStore.getTypeNames()[0];
            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = shpDataStore.getFeatureSource(typeName);
            Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)")
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures(filter);
            features = collection.features();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //创建目标shape文件对象
        log.info("创建目标shape文件对象!");
        Map<String, Serializable> params = Maps.newHashMap();
        FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer =null;
        try {
            params.put(ShapefileDataStoreFactory.URLP.key, targetFile.toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) factory.createNewDataStore(params);
            ds.setCharset(shpDataStore.getCharset());
            // 设置属性
            SimpleFeatureSource fs = shpDataStore.getFeatureSource(shpDataStore.getTypeNames()[0]);
            //下面这行还有其他写法，根据源shape文件的simpleFeatureType可以不用retype，而直接用fs.getSchema设置
            ds.createSchema(SimpleFeatureTypeBuilder.retype(fs.getSchema(), DefaultGeographicCRS.WGS84));
            //设置writer
            writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        //转换shp文件坐标并输出文件
        log.info("转换shp文件坐标并输出文件!");
        while (features.hasNext()) {
            feature = features.next();
            List<Object> list = feature.getAttributes();
            Object obj = list.get(0);
            if (obj instanceof LineString || obj instanceof MultiLineString) {
                Geometry line = ((Geometry) obj);
                int parts = line.getNumGeometries();
                for (int i = 0; i < parts; i++) {
                    LineString l = (LineString) line.getGeometryN(i);
                    for (int j = 0, num = l.getNumPoints(); j < num; j++) {
                        Coordinate coor = l.getCoordinateN(j);
                        log.info("转换前:"+coor.x+"::"+coor.y);
                        double[] xy = ProjTransform.wgs84togcj02(coor.x,coor.y);  // wgs84togcj02
                        log.info("转换后:"+xy[0]+"::"+xy[1]);
                        Coordinate newcoor = new Coordinate(xy[0], xy[1]);
                        coor.setCoordinate(newcoor);
                    }
                }
            } else if (obj instanceof Point) {
                Point pt = (Point) obj;
                Coordinate coor = pt.getCoordinate();
                log.info("转换前:"+coor.x+"::"+coor.y);
                double[] xy = ProjTransform.wgs84togcj02(coor.x,coor.y);  // wgs84togcj02
                log.info("转换后:"+xy[0]+"::"+xy[1]);
                coor.setCoordinate(new Coordinate(xy[0], xy[1]));
            }

            //写入目标shp文件
            try {
                SimpleFeature fNew = writer.next();
                fNew.setAttributes(feature.getAttributes());  //转换到文件
                writer.write();   //写入文件

            } catch (IOException e) {
                e.printStackTrace();
            }
          }

        log.info("写入目标路径shp文件完毕!");

        //关闭资源
        log.info("关闭IO资源!");
            try {
                writer.close();
                features.close();
                shpDataStore.dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        log.info("shp文件转换完毕!");
        return ResultUtil.success("转换文件成功!");
    }

    @Override
    public Result getUploadFilePathAndName(MultipartFile[] shpFiles) {
        List<Map<String,String>> data = (List<Map<String,String>>) getUploadFile(shpFiles).getData();
        Map<String,String> map= Maps.newHashMap();
        map.put("filePath",data.get(0).get("filePath"));
        map.put("fileName",data.get(0).get("filename"));
        return ResultUtil.success(map);
    }

    @Override
    public Result getUploadFile(MultipartFile[] shpFiles) {
        if (shpFiles.length==0){
            log.error("file is empty.");
            return ResultUtil.error("file is empty.pls try upload again");
        }
        //本地文件上传服务器中
        Result uploadResult = fileService.upload(Arrays.asList(shpFiles));
        List<Map<String,String>> data = (List<Map<String,String>>) uploadResult.getData();
        return ResultUtil.success(data);
    }
}
