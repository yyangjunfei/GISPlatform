package cc.wanshan.gis.controller.layer;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Store;
import cc.wanshan.gis.service.storeservice.StoreService;
import cc.wanshan.gis.utils.ResultUtil;
import io.micrometer.core.instrument.util.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;

@Controller
@MapperScan("cc.wanshan.demo.entity")
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/store")
public class StoreController {
    private static final Logger logger= LoggerFactory.getLogger(StoreController.class);
    @Resource(name = "storeServiceImpl")
    private StoreService storeService;
    @RequestMapping("/findStoreByUsername")
    @ResponseBody
    public Result findStoreByUsername(@RequestParam String username){
        logger.info("findStoreByUsername::username = [{}]",username);
        if (StringUtils.isNotBlank(username)){
            List<Store> stores = storeService.findStoreByUsername(username);
            if (stores!=null&&stores.size()>0){
                return ResultUtil.success(stores);
            }else {
                return ResultUtil.error(1,"结果为null");
            }
        }else {
            logger.warn("userId为null");
            return ResultUtil.error(1,"userId为null");
        }
    }
}
