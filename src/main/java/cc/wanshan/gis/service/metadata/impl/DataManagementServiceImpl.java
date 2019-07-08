package cc.wanshan.gis.service.metadata.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.dao.metadata.DataManagementDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.metadata.metadata;
import cc.wanshan.gis.service.metadata.DataManagementService;
import cc.wanshan.gis.service.metadata.FileService;
<<<<<<< HEAD
import cc.wanshan.gis.utils.GeoServerUtils;
import cc.wanshan.gis.utils.LanguageUtils;
=======
>>>>>>> develop
import cc.wanshan.gis.utils.ResultUtil;
import cc.wanshan.gis.utils.ShpReader;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public Result metadataImport(String jsonString , MultipartFile[] file) {

        LOG.info("metadataImport...");
        //本地文件上传服务器中
        Result uploadResult = fileService.upload(Arrays.asList(file), "");

        List<Map<String, String>> data = (List<Map<String, String>>) uploadResult.getData();

        if (null == data || data.size() <= 0) {
            return ResultUtil.error(ResultCode.UPLOAD_FILE_NULL);
        }

        String filePath = data.get(0).get("filePath");

        //String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        metadata metadata = JSON.parseObject(jsonString, metadata.class);

        //将中文图层名转换为拼音字符作为数据库中的表名
        metadata.setLayerName(LanguageUtils.getPinYin(metadata.getLayerName()));

        //读取shp文件并且发布到数据库
        try {
            ShpReader.readSHP(filePath,metadata);

            //发布完成之后删除上传文件
            for(Map<String, String> map :data){
                fileService.delFile(map.get("filePath"));
            }

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
    public List<metadata> findLayerProperties() {

        return dataManagementDao.findLayerProperties();
    }

    @Override
    public Result deleteLayerPropertiesData(int id) {

        int i = dataManagementDao.deleteLayerPropertiesData(id);

        if(i>0){
            return  ResultUtil.success("删除数据成功");
        }else {
            return ResultUtil.error("删除数据失败！");
        }
    }

    @Override
    public Result editLayerPropertiesData(metadata metadata) {

        int i =dataManagementDao.editLayerPropertiesData(metadata);

        if(i>0){
            return  ResultUtil.success("更新数据成功");
        }else {
            return ResultUtil.error("更新数据失败！");
        }
    }

    @Override
    public List<metadata> findLayerPropertiesData(metadata metadata ) {

        return dataManagementDao.findLayerPropertiesData(metadata);

    }

    @Override
    public Result changePublicationStatus(int id) {

        int i =dataManagementDao.changePublicationStatus(id);
        if(i>0){
            return  ResultUtil.success("更新数据成功");
        }else {
            return ResultUtil.error("更新数据失败！");
        }
    }

}
