package cc.wanshan.gisdev.service.plot.impl;

import cc.wanshan.gisdev.common.constants.Constant;
import cc.wanshan.gisdev.common.enums.ResultCode;
import cc.wanshan.gisdev.dao.plot.PlotLineRepository;
import cc.wanshan.gisdev.dao.plot.PlotPointRepository;
import cc.wanshan.gisdev.dao.plot.PlotPolygonRepository;
import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.entity.plot.Plot;
import cc.wanshan.gisdev.service.plot.PlotService;
import cc.wanshan.gisdev.utils.JsonUtils;
import cc.wanshan.gisdev.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
public class PlotServiceImpl implements PlotService {

    private static Logger LOG = LoggerFactory.getLogger(PlotServiceImpl.class);

    private static final HashMap<String, JpaRepository> daoMap = Maps.newHashMap();

    @Autowired
    private PlotPointRepository plotPointRepository;
    @Autowired
    private PlotLineRepository plotLineRepository;
    @Autowired
    private PlotPolygonRepository plotPolygonRepository;

    private JpaRepository getDao(String key) {

        daoMap.put(Constant.GEO_POINT, plotPointRepository);
        daoMap.put(Constant.GEO_LINESTRING, plotLineRepository);
        daoMap.put(Constant.GEO_POLYGON, plotPolygonRepository);

        return daoMap.get(key);
    }

    @Override
    public Result save(String jsonString) {
        if (!JsonUtils.validate(jsonString)) {
            return ResultUtil.error(ResultCode.PARAM_NOT_JSON);
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            String type = jsonObject.getString(Constant.TYPE);

            Object save = getDao(type).save(Plot.create(jsonObject));

            if (save != null) {
                return ResultUtil.success(save);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtil.error(ResultCode.SAVE_FAIL);
    }

    @Override
    public Result findAll(String type) {
        return ResultUtil.success(getDao(type).findAll());
    }

    @Override
    public Result deleteById(String type, String id) {
        return ResultUtil.success(getDao(type).findById(id));
    }

    @Override
    public Result update(String jsonString) {
        if (!JsonUtils.validate(jsonString)) {
            return ResultUtil.error(ResultCode.PARAM_NOT_JSON);
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            String type = jsonObject.getString(Constant.TYPE);

            Object save = getDao(type).saveAndFlush(Plot.create(jsonObject));

            if (save != null) {
                return ResultUtil.success(save);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtil.error(ResultCode.UPDATE_FAIL);
    }
}
