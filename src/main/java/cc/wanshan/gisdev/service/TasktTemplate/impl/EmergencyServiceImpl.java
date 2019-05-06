package cc.wanshan.gisdev.service.TasktTemplate.impl;

import cc.wanshan.gisdev.common.enums.FieldEnum;
import cc.wanshan.gisdev.common.enums.ResultCode;
import cc.wanshan.gisdev.common.factory.EmergencyFactory;
import cc.wanshan.gisdev.dao.EmergencyRepository;
import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.entity.TasktTemplate.Emergency;
import cc.wanshan.gisdev.service.TasktTemplate.EmergencyService;
import cc.wanshan.gisdev.utils.GeotoolsUtils;
import cc.wanshan.gisdev.utils.JsonUtils;
import cc.wanshan.gisdev.utils.ResultUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class EmergencyServiceImpl implements EmergencyService {

    private static Logger LOG = LoggerFactory.getLogger(EmergencyServiceImpl.class);

    private static HashMap<String, EmergencyFactory<Emergency>> map = Maps.newHashMap();

    @Autowired
    private EmergencyRepository emergencyRepository;

    static {

        map.put("emergency", new Emergency());
    }

    @Override
    public Result save(String jsonStr) {
        //校验字符串是否是合法的json字符串
        if (!JsonUtils.validate(jsonStr)) {
            return ResultUtil.error(ResultCode.PARAM_NOT_JSON);
        }
        Emergency emergency = null;
        try {
            emergency = map.get("emergency").create(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (emergencyRepository.save(emergency) != null) {
            return ResultUtil.success(emergency);
        } else {
            return ResultUtil.error(ResultCode.SAVE_FAIL);
        }
    }

    @Override
    public Result findAll() {
        List<Emergency> emergencyList = emergencyRepository.findAll();
        if (emergencyList != null) {
            return ResultUtil.success(emergencyList);
        } else {
            return ResultUtil.error(ResultCode.FIND_NULL);
        }
    }

    @Override
    public Result update(String jsonStr) {
        //校验字符串是否是合法的json字符串
        if (!JsonUtils.validate(jsonStr)) {
            return ResultUtil.error(ResultCode.PARAM_NOT_JSON);
        }
        Emergency emergency = JSON.parseObject(jsonStr, Emergency.class);

        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String geoJson = jsonObject.getString(FieldEnum.geometry.name());
        if (null != geoJson && geoJson.length() > 0) {
            try {
                emergency.setGeometry(GeotoolsUtils.geoJson2Geometry(geoJson));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (emergencyRepository.saveAndFlush(emergency) != null) {
            return ResultUtil.success(emergency);
        } else {
            return ResultUtil.error(ResultCode.UPDATE_FAIL);
        }
    }
}
