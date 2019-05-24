package cc.wanshan.gis.service.geoserverservice;

import cc.wanshan.gis.entity.Result;

import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import java.net.URISyntaxException;

public interface GeoserverService {
    /**
     * @Author Li Cheng
     * @Description  创建工作空间
     * @Date 13:59 2019/4/23
     * @Param []
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result creatWorkspace(String workspace) throws URISyntaxException;
    /**
     * description: 查询layer是否存在
     *
     * @param thematicName
     * @param layerName
     * @return
     */
    public Boolean searchLayer(String thematicName,String layerName);
    /**
     * description: 删除layer
     *
     * @param thematicName
     * @param storeName
     * @param layerName
     * @return
     */
    public Boolean deleteLayer(String thematicName,String storeName,String layerName);
}
