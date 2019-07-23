package cc.wanshan.gis.service.style.impl;

import cc.wanshan.gis.dao.style.StyleDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.style.Style;
import cc.wanshan.gis.service.metadata.FileService;
import cc.wanshan.gis.service.style.StyleService;

import java.io.File;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import cc.wanshan.gis.utils.GeoServerUtils;
import cc.wanshan.gis.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Li Cheng
 * @date 2019/5/24 9:42
 */
@Service(value = "styleServiceImpl")
public class StyleServiceImpl implements StyleService {

  @Resource
  private StyleDao styleDao;
  @Autowired
  private FileService fileService;

  @Override
  public List<Style> findAllStyle() {
    return styleDao.findAllStyle();
  }

  @Override
  public List<Style> findByLayerId(String layerId) {
    return styleDao.findByLayerId(layerId);
  }

  @Override
  public List<Style> findStyleByStyleName(String styleName) {
    return styleDao.findStyleByStyleName(styleName);
  }

  @Override
  public Result createStyle(MultipartFile styleFile) {
    Result uploadResult = fileService.upload(styleFile);
    Map<String,String> map =(Map<String,String>) uploadResult.getData();
    String filePath = map.get("filePath");
    boolean ps = GeoServerUtils.publisher.publishStyle(new File(filePath));
    fileService.delFile(filePath);
    if(ps){
      return  ResultUtil.success("创建风格样式成功！");
    }else {
      return  ResultUtil.error("创建风格样式失败！");
    }

  }
}
