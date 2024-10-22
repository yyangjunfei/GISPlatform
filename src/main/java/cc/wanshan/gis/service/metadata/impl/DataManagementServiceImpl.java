package cc.wanshan.gis.service.metadata.impl;
import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.metadata.DataManagementDao;
import cc.wanshan.gis.entity.common.PageBean;
import cc.wanshan.gis.entity.metadata.ShpInfo;
import cc.wanshan.gis.entity.metadata.metadata;
import cc.wanshan.gis.service.metadata.DataManagementService;
import cc.wanshan.gis.service.metadata.FileService;
import cc.wanshan.gis.utils.LanguageUtils;
import cc.wanshan.gis.utils.base.ResultUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class DataManagementServiceImpl implements DataManagementService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileService fileService;

    @Autowired
    private DataManagementDao dataManagementDao;

    @Override
    public Result metadataImportPublication(String jsonString, MultipartFile[] file) {

        LOG.info("开始上传shp文件");
        //本地文件上传服务器中
        Result uploadResult = fileService.upload(Arrays.asList(file));
        List<Map<String,String>> data = (List<Map<String,String>>) uploadResult.getData();

        if (null == data || data.size() <= 0) {
            return ResultUtil.error(ResultCode.UPLOAD_FILE_NULL);
        }

        String filePath = data.get(0).get("filePath");

        //String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        metadata metadata = JSON.parseObject(jsonString, metadata.class);

        //读取shp文件并且发布到数据库
        LOG.info("读取shp文件并且发布到数据库");
        try {

            List<ShpInfo> shpInfoList = fileService.readSHP(filePath);
            //将中文图层名转换为拼音字符作为数据库中的表名
            metadata.setLayerName(LanguageUtils.getPinYin(metadata.getLayerName()));
            //设置geometryType
            metadata.setGeoType(shpInfoList.get(0).getGeometry().getType());

            //shp数据存储到数据库
            LOG.info("将shp数据存储到数据库");
            fileService.publishShpData2DB(shpInfoList, metadata);
            LOG.info("完成shp数据存储");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.success(metadata);
    }

    @Override
    public metadata shpData2Geoserver(int id) {
        return dataManagementDao.shpData2Geoserver(id);
    }

    @Override
    public PageBean<metadata> findLayerProperties(Integer pageNum,Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<metadata> metadataList = dataManagementDao.findLayerProperties();

        PageInfo info = new PageInfo(metadataList);   //1、PageInfo 是 pagehelper中内值的分页的信息类
        PageBean pageBean = new PageBean(pageNum,pageSize,Integer.valueOf(new Long(info.getTotal()).intValue()));

        pageBean.setItems(metadataList);

        return pageBean;
    }

    @Override
    public Result deleteLayerPropertiesData(Integer [] layerIds) {

        int i = dataManagementDao.deleteLayerPropertiesData(layerIds);
        if (i== 0) {
            return ResultUtil.success("删除数据成功");
        } else {
            return ResultUtil.error("删除数据失败！");
        }
    }

    @Override
    public Result editLayerPropertiesData(metadata metadata) {

        int i = dataManagementDao.editLayerPropertiesData(metadata);

        if (i > 0) {
            return ResultUtil.success("更新数据成功");
        } else {
            return ResultUtil.error("更新数据失败！");
        }
    }

    /***
     * 封装参数 : workspaceName,storeName,DataType,layerName,safetyLevel,vectorTypes,styleName,createBy
     * @param metadata
     * @return
     */

    @Override
    public List<metadata> findLayerPropertiesData(metadata metadata) {
        return dataManagementDao.findLayerPropertiesData(metadata);
    }

    @Override
    public Result changePublicationStatus(int release_flag, int id) {

        int i = dataManagementDao.changePublicationStatus(release_flag, id);
        if (i > 0) {
            return ResultUtil.success("更改发布状态成功");
        } else {
            return ResultUtil.error("更新发布状态失败！");
        }
    }

}
