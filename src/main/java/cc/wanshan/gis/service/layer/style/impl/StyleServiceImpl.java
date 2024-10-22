package cc.wanshan.gis.service.layer.style.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.layer.style.StyleDao;
import cc.wanshan.gis.entity.layer.style.Style;
import cc.wanshan.gis.service.layer.style.StyleService;
import cc.wanshan.gis.service.metadata.FileService;
import cc.wanshan.gis.utils.LanguageUtils;
import cc.wanshan.gis.utils.base.ResultUtil;
import cc.wanshan.gis.utils.geo.GeoServerUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;

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
    public Result findAll() {
        LOG.info("findAll::");
        List<Style> styles = styleDao.findAllStyle();
        if (styles!=null){
            return ResultUtil.success(styles);
        }
        return ResultUtil.error(ResultCode.FIND_NULL);
    }

    @Override
    public Result findByLayerId(String layerId) {
        LOG.info("findByLayerId::layerId = [{}]",layerId);
        if (StringUtils.isBlank(layerId)){
            return ResultUtil.error(ResultCode.PARAM_IS_NULL);
        }
        List<Style> styles = styleDao.findByLayerId(layerId);
        if (styles.size()>0){
            return ResultUtil.success(styles);
        }
        return ResultUtil.error(ResultCode.FIND_NULL);
    }

    @Override
    public Result findByStyleName(String styleName) {
        LOG.info("findByLayerId::layerId = [{}]",styleName);
        if (StringUtils.isBlank(styleName)){
            return ResultUtil.error(ResultCode.PARAM_IS_NULL);
        }
        List<Style> styles = styleDao.findStyleByStyleName(styleName);
        if (styles.size()>0){
            return ResultUtil.success(styles);
        }
        return ResultUtil.error(ResultCode.FIND_NULL);
    }

    @Override
    public Result createStyleBySldFile(String workspaceName, String sldName, MultipartFile styleFile) {
        Result uploadResult = fileService.upload(styleFile);
        Map<String, String> map = (Map<String, String>) uploadResult.getData();
        String filePath = map.get("filePath");
        //如果是汉字转换为拼音
        String sldFileName = LanguageUtils.getPinYin(sldName);
        //样式是否存在
        boolean styleFlag = GeoServerUtils.reader.existsStyle(workspaceName, sldFileName);
        if (styleFlag) {
            LOG.warn(sldFileName + ":样式已经存在了！");
        } else {
            boolean sldPublishResult = GeoServerUtils.publisher.publishStyleInWorkspace(workspaceName, new File(filePath), sldFileName);
            if (sldPublishResult) {
                return ResultUtil.success(sldFileName + ":样式发布成功！");
            } else {
                return ResultUtil.error(sldFileName + ":样式发布失败！");
            }
        }
        //删除上传文件
        fileService.delFile(filePath);
        return null;
    }

    @Override
    public Result createStyleBySldDocument(String workspaceName, String sldBody, String sldName) {

        //如果是汉字转换为拼音
        String sldFileName = LanguageUtils.getPinYin(sldName);

        boolean sldPublishResult = GeoServerUtils.publisher.publishStyleInWorkspace(workspaceName, sldBody, sldFileName);
        if (sldPublishResult) {
            return ResultUtil.success(":样式发布成功！");
        } else {
            return ResultUtil.error(":样式发布失败！");
        }
    }
}
