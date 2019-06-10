package cc.wanshan.gis.controller.layer;


import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Store;
import cc.wanshan.gis.service.store.StoreService;
import cc.wanshan.gis.utils.ResultUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;

@Controller
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/store")
public class StoreController {
    private static final Logger logger= LoggerFactory.getLogger(StoreController.class);
    @Resource(name = "storeServiceImpl")
    private StoreService storeService;
    @RequestMapping("/findstorebyusername")
    @ResponseBody
    public Result findStoreByUsername(@RequestBody JSONObject jsonObject){
        logger.info("findStoreByUsername::username = [{}]",jsonObject);
        String username=jsonObject.getString("username");
        if (StringUtils.isNotBlank(username)){
            List<Store> stores = storeService.findStoreByUsername(username);
            if (stores!=null&&stores.size()>0){
                JSONArray jsonArray= (JSONArray) JSONArray.toJSON(stores);
                return ResultUtil.success(jsonArray);
            }else {
                return ResultUtil.error(1,"结果为null");
            }
        }else {
            logger.warn("userId为null");
            return ResultUtil.error(1,"userId为null");
        }
    }
}
