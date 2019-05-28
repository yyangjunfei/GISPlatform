package cc.wanshan.gis.utils;

import cc.wanshan.gis.entity.metadata.ShpInfo;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 读取矢量数据
 */
public class ShpReader {

    private static Connection connection = null;
    private static DataStore pgDatastore = null;

    public static void readSHP(String path) throws Exception {

        // 一个数据存储实现，允许从Shapefiles读取和写入
        ShapefileDataStore shpDataStore = null;
        shpDataStore = new ShapefileDataStore(new File(path).toURI().toURL());
        shpDataStore.setCharset(Charset.forName("UTF-8"));

        // 获取这个数据存储保存的类型名称数组
        // getTypeNames:获取所有地理图层
        String typeName = shpDataStore.getTypeNames()[0];

        // 通过此接口可以引用单个shapefile、数据库表等。与数据存储进行比较和约束
        FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = null;
        featureSource = (FeatureSource<SimpleFeatureType, SimpleFeature>) shpDataStore.getFeatureSource(typeName);

        // 一个用于处理FeatureCollection的实用工具类。提供一个获取FeatureCollection实例的机制
        FeatureCollection<SimpleFeatureType, SimpleFeature> result = featureSource.getFeatures();

        FeatureIterator<SimpleFeature> iterator = result.features();

        // 迭代 特征 只迭代100个 太大了，一下子迭代完，非常耗时
        int stop = 0;
        List<ShpInfo> geolist = new ArrayList<ShpInfo>();
        while (iterator.hasNext()) {

            if (stop > 100) {
                break;
            }

            SimpleFeature feature = iterator.next();
            Collection<Property> p = feature.getProperties();
            Iterator<Property> it = p.iterator();
            // 构建实体
            ShpInfo geo = new ShpInfo();

            // 特征里面的属性再迭代,属性里面有字段
            String name;
            while (it.hasNext()) {

                Property pro = it.next();
                name = pro.getName().toString();

                /**
                 * 根据shp文件里面的属性值进行过滤
                 */
                if (name.equals("the_geom")) {
                    geo.setGeom(pro.getValue());
                }

                if (name.equals("osm_id")) {
                    geo.setOsm_id(pro.getValue().toString());
                }

                if (name.equals("code")) {
                    geo.setCode(Integer.parseInt(pro.getValue().toString()));
                }

                if (name.equals("fclass")) {
                    geo.setFclass(pro.getValue().toString());
                }

                if (name.equals("name")) {
                    geo.setName(pro.getValue().toString());
                }

                if (name.equals("type")) {
                    geo.setType(pro.getValue().toString());
                }

            } // end 里层while

            geolist.add(geo);
            stop++;

        } // end 最外层 while

        iterator.close();
        boolean bRes = true;
        for (ShpInfo geo : geolist) {
            /**
             * 存储对象geo，这里是循环插入，也可以做成mybatis的批量insert
             */
            if (!shpSave(geo)) {
                bRes = false;
                break;
            }
        }

        if (bRes) {
            System.out.println("读取shapefile文件内容并插入数据库成功！");
        }
    }

    /**
     * 存储shp文件的数据 == 文件内容映射成Java实体类存储在postgresql数据库中
     *
     * @param geo
     * @return
     * @throws Exception
     */
    public static boolean shpSave(ShpInfo geo) throws Exception {

        boolean result = false;
        String sql = "insert into geotable (osm_id,code,fclass,name,type,geom) values('" + geo.getOsm_id() + "','"
                + geo.getCode() + "','" + geo.getFclass() + "','" + geo.getName() + "','" + geo.getType() + "',"
                + "st_geomfromewkt('" + geo.getGeom().toString() + "'))";

        PreparedStatement pstmt;
        pstmt = connection.prepareStatement(sql);

        int i = pstmt.executeUpdate();
        if (i > 0) {
            result = true;
        }
        pstmt.close();
        return result;
    }
}
