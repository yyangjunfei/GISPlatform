package cc.wanshan.gis.service.layer.thematic;

import cc.wanshan.gis.entity.layer.thematic.FirstClassification;

/**
 * @author Li Cheng
 * @date 2019/5/25 14:34
 */
public interface FirstClassificationService {

    /**
     * description:新增第一分类记录数
     *
     * @param firstClassification 第一分类对象
     * @return java.lang.Boolean
     **/
    Boolean insertFirstClassification(FirstClassification firstClassification);

}
