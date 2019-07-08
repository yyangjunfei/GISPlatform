package cc.wanshan.gis.utils;
import cc.wanshan.gis.entity.metadata.ShpInfo;
import cc.wanshan.gis.entity.metadata.metadata;
import com.alibaba.fastjson.JSONObject;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import java.io.File;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取矢量数据
 */
public class ShpReader {

    private static DataStore pgDatastore = null;
    public static void readSHP(String publishPath, metadata metadata) throws Exception {

        // 一个数据存储实现，允许从Shapefiles读取和写入
        ShapefileDataStore shpDataStore = null;
        File file  = new File(publishPath);
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
        List<ShpInfo> geolist = new ArrayList<ShpInfo>();
        FeatureJSON fjson = new FeatureJSON();
        while (iterator.hasNext()){
            SimpleFeature feature = iterator.next();
            StringWriter writer = new StringWriter();
            fjson.writeFeature(feature, writer);

            // 构建实体
            ShpInfo shpInfo = JSONObject.parseObject(writer.toString(), ShpInfo.class);
            geolist.add(shpInfo);

        } // end 最外层 while

        //发布shp数据到DB
        publishData.publishShpData2DB(geolist,metadata);
    }
}
