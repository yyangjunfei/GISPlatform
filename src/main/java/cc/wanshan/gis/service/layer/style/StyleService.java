package cc.wanshan.gis.service.layer.style;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.layer.style.Style;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/5/24 9:29
 */
public interface StyleService {

    /**
     * description:查询所有的style
     */
    List<Style> findAllStyle();

    /**
     * description: 根据图层Id查询style
     *
     * @param layerId 图层Id
     * @return java.util.List<cc.wanshan.gis.entity.style.Style>
     **/
    List<Style> findByLayerId(String layerId);

    /**
     * description:  根据风格名查询风格列表
     *
     * @param styleName 风格名
     * @return java.util.List<cc.wanshan.gis.entity.style.Style>
     **/
    List<Style> findStyleByStyleName(String styleName);


    /***
     * Yang 2019-7-23
     * @param styleFile
     * @return
     */

    //创建风格文件(来自SLD文件)
    Result createStyleBySldFile(String workspaceName, String sldName, MultipartFile styleFile);

    //创建风格文件(来自整个SLD文档字符串)
    Result createStyleBySldDocument(String workspaceName, String sldBody, String sldName);

}
