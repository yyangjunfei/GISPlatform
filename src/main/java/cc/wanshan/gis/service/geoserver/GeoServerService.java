package cc.wanshan.gis.service.geoserver;

import cc.wanshan.gis.entity.Result;

import java.net.URISyntaxException;

public interface GeoServerService {

    /**
     * @return
     * @Author Li Cheng
     * @Description 创建工作空间
     * @Date 13:59 2019/4/23
     * @Param []
     **/
    public Result creatWorkspace(String workspace) throws URISyntaxException;

    /**
     * description: 查询layer是否存在
     *
     * @param thematicName
     * @param layerName
     * @return
     */
    public Boolean searchLayer(String thematicName, String layerName);

    /**
     * description: 删除layer
     *
     * @param thematicName
     * @param storeName
     * @param layerName
     * @return
     */
    public Boolean deleteLayer(String thematicName, String storeName, String layerName);


    /**
     * 创建工作空间
     *
     * @param workSpaceName 工作空间名称
     * @param uri           工作空间uri，可为空
     * @return
     * @throws URISyntaxException
     */
    public boolean createWorkSpace(String workSpaceName, String uri) throws URISyntaxException;

    /**
     * 创建存储名称
     *
     * @param storeName 存储名称
     * @param workspace 工作空间
     * @return
     */
    public Result createDataStore(String storeName, String workspace);

    /**
     * 发布图层
     *
     * @param ws        工作空间
     * @param storeName 存储名称
     * @param tableName 数据库表名称
     * @return
     */
    public Result publishLayer(String ws, String storeName, String tableName);
}
