package cc.wanshan.gis.service.merchant;
import cc.wanshan.gis.dao.merchant.MerchantDao;
import cc.wanshan.gis.entity.merchant.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***
 * @author Yang
 * @date  2019-8-12
 * @version [v1.0]
 * @descriptionweb TODO
 */
@Service
public class MerchantServiceImpl implements MerchantService{

    @Autowired
    private MerchantDao merchantDao;

    @Override
    public int saveMerchantInfo(Merchant merchant) {
        String wkt = "Point("+merchant.getLongitude()+" "+merchant.getLatitude()+")";
        merchant.setGeometry(wkt);
        return  merchantDao.saveMerchantInfo(merchant);
    }
}
