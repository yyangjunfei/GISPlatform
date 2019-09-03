package cc.wanshan.gis.utils.transform;
import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;



/***
 * @author
 * @date
 * @version [v1.0]
 * @descriptionweb TODO
 */
public class test {

    public  static void test_Transform(){

        CoordinateReferenceSystem sourceCRS = null;
        CoordinateReferenceSystem targetCRS = null;
        MathTransform transform = null;
        try {
            sourceCRS = CRS.decode("EPSG:4326");
            targetCRS = CRS.decode("EPSG:3857");
            transform = CRS.findMathTransform(sourceCRS, targetCRS);
            Coordinate coorDst=new Coordinate();
            JTS.transform(new Coordinate(73.6770723800199, 39.13769693730567),coorDst, transform);
            System.out.println(coorDst);
        } catch (FactoryException | TransformException e) {
            e.printStackTrace();
        }
    }
}
