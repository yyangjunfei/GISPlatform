package cc.wanshan.gis.dao.merchant;
import cc.wanshan.gis.entity.merchant.Merchant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/***
 * @author  Yang
 * @date    2019-8-12
 * @version [v1.0]
 * @descriptionweb TODO
 */

@Mapper
@Component
public interface MerchantDao {

    @Insert({"INSERT into merchant.merchant (position_description,address,geo_type,geometry,latitude,longitude,telephone,TYPE) VALUES (#{PositionDescription},#{address},#{geoType},geomfromewkt(#{geometry}),#{latitude},#{longitude},#{telephone},#{type})"})
    int saveMerchantInfo(Merchant merchant);
}
