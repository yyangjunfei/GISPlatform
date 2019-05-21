package cc.wanshan.gis.service.plot.impl;

import cc.wanshan.gis.common.constants.Constant;
import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.dao.plot.PlotLineDao;
import cc.wanshan.gis.dao.plot.PlotPointMapper;
import cc.wanshan.gis.dao.plot.PlotPolygonDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.plot.PlotPoint;
import cc.wanshan.gis.service.plot.PlotService;
import cc.wanshan.gis.utils.JsonUtils;
import cc.wanshan.gis.utils.ResultUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PlotServiceImpl implements PlotService {

    private static Logger LOG = LoggerFactory.getLogger(PlotServiceImpl.class);

    @Resource
    private PlotPointMapper plotPointMapper;

    //    @Autowired
    private PlotLineDao plotLineDao;

    //        @Autowired
    private PlotPolygonDao plotPolygonDao;

    @Override
    public Result save(String jsonString) {
        if (!JsonUtils.validate(jsonString)) {
            return ResultUtil.error(ResultCode.PARAM_NOT_JSON);
        }

        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String type = jsonObject.getString(Constant.TYPE);
        int insert = 0;

        switch (type) {
            case Constant.GEO_POINT:
                PlotPoint plotPoint = JSON.parseObject(jsonString, PlotPoint.class);
                insert = plotPointMapper.insert(plotPoint);
//            case Constant.GEO_LINESTRING:
//                PlotLine plotLine = jsonObject.getObject(jsonString, PlotLine.class);
//                insert = plotLineDao.insert(plotLine);
//            case Constant.GEO_POLYGON:
//                PlotPolygon plotPolygon = jsonObject.getObject(jsonString, PlotPolygon.class);
//                insert = plotPolygonDao.insert(plotPolygon);
            default:
                break;
        }


        if (insert > 0) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultCode.SAVE_FAIL);
        }
    }

    @Override
    public Result findAll(String type) {
        return null;
//        //判断type是否为空
//        if (type == null || type.length() <= 0) {
//            switch (type) {
//                case Constant.GEO_POINT:
//                    return ResultUtil.success(plotPointMapper.findAll());
//                case Constant.GEO_LINESTRING:
//                    return ResultUtil.success(plotLineDao.findAll());
//                case Constant.GEO_POLYGON:
//                    return ResultUtil.success(plotPolygonDao.findAll());
//                default:
//                    return ResultUtil.error(ResultCode.PARAMS_TYPE_ERROR);
//            }
//        }
//        //合并多个List
//        ArrayList<Object> list = Lists.newArrayList();
//        List<PlotPoint2> pointList = plotPointDao.findAll();
//        if (pointList != null) {
//            list.addAll(pointList);
//        }
//        List<PlotLine> lineList = plotLineDao.findAll();
//        if (lineList != null) {
//            list.addAll(lineList);
//        }
//        List<PlotPolygon> polygonList = plotPolygonDao.findAll();
//        if (polygonList != null) {
//            list.addAll(polygonList);
//        }
//        if (list == null) {
//            return ResultUtil.error(ResultCode.FIND_NULL);
//        } else {
//            return ResultUtil.success(list);
//        }

    }

    @Override
    public Result deleteById(String type, String id) {
        return null;
//        return ResultUtil.success(getDao(type).findById(id));
    }

    @Override
    public Result update(String jsonString) {
        if (!JsonUtils.validate(jsonString)) {
            return ResultUtil.error(ResultCode.PARAM_NOT_JSON);
        }
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(jsonString);
//            String type = jsonObject.getString(Constant.TYPE);
//
//            Object save = getDao(type).saveAndFlush(Plot.create(jsonObject));
//
//            if (save != null) {
//                return ResultUtil.success(save);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return ResultUtil.error(ResultCode.UPDATE_FAIL);
    }

}
