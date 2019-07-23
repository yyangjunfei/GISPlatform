package cc.wanshan.gis.service.style.impl;

import cc.wanshan.gis.controller.style.StyleController;
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
import cc.wanshan.gis.utils.LanguageUtils;
import cc.wanshan.gis.utils.ResultUtil;
import org.geotools.styling.SLD;
import org.openxmlformats.schemas.presentationml.x2006.main.SldDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Li Cheng
 * @date 2019/5/24 9:42
 */
@Service(value = "styleServiceImpl")
public class StyleServiceImpl implements StyleService {

  private static Logger LOG = LoggerFactory.getLogger(StyleServiceImpl.class);

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
  public Result createStyleBySldFile(String workspaceName,String sldName,MultipartFile styleFile) {
    Result uploadResult = fileService.upload(styleFile);
    Map<String,String> map =(Map<String,String>) uploadResult.getData();
    String filePath = map.get("filePath");
    //如果是汉字转换为拼音
    String sldFileName =LanguageUtils.getPinYin(sldName);
    //样式是否存在
    boolean styleFlag = GeoServerUtils.reader.existsStyle(workspaceName,sldFileName);
    if(styleFlag){
      LOG.warn(sldFileName+":样式已经存在了！");
    }else {
        boolean sldPublishResult = GeoServerUtils.publisher.publishStyleInWorkspace(workspaceName,new File(filePath),sldFileName);
        if(sldPublishResult){
          return  ResultUtil.success(sldFileName+":样式发布成功！");
        }else{
          return  ResultUtil.error(sldFileName+":样式发布失败！");
        }
    }
    //删除上传文件
    fileService.delFile(filePath);
    return null;
  }

  @Override
  public Result createStyleBySldDocument(String workspaceName,String sldBody,String sldName) {

    //如果是汉字转换为拼音
    String sldFileName =LanguageUtils.getPinYin(sldName);

    boolean sldPublishResult =GeoServerUtils.publisher.publishStyleInWorkspace(workspaceName,sldBody,sldFileName);
    if(sldPublishResult){
      return  ResultUtil.success(":样式发布成功！");
    }else{
      return  ResultUtil.error(":样式发布失败！");
    }
  }
}
