package cc.wanshan.gisdev.service.geoserverservice;

import cc.wanshan.gisdev.entity.Result;

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

}
