package cc.wanshan.gis.service.layerservice;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Layer;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

/**
 * @Author Li Cheng
 * @Description layer表增删改查
 * @Date 15:32 2019/3/21
 * @Param
 * @return
 **/
public interface LayerService {
    public Result newLayer(String workspace, String layerName, String type, String epsg);
    /**
     * @Author Li Cheng
     * @Description 保存图层
     * @Date 8:44 2019/4/1
     * @Param layer图层实体类
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result insertLayer(Layer layer, String workspace);
    /**
     * @Author Li Cheng
     * @Description 根据分装的layer实体类的layerId和storeId删除对应的layer
     * @Date 8:44 2019/4/1
     * @Param [jsonObject] 封装了layer信息的json对象
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result deleteLayer(JSONObject jsonObject);
    /**
     * @Author Li Cheng
     * @Description 查找图层是否为已发布图层
     * @Date 16:37 2019/4/10
     * @Param [jsonObject]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result searchLayer(JSONObject jsonObject);
    /**
     * @Author Li Cheng
     * @Description 创建图层对应表发布图层
     * @Date 14:43 2019/2/28
     * @Param [object]
     * @return void
     **/
    public Result insertFeatures(JSONObject object) throws IOException;
    /**
     * @Author Li Cheng
     * @Description 根据username和layerName判断图层是否重名
     * @Date 15:59 2019/4/22
     * @Param [username, layerName]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result findLayerCountByUsernameAndLayerName(String username, String layerName);
}
