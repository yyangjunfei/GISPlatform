package cc.wanshan.gis.service.layer.thematic;

import cc.wanshan.gis.entity.layer.thematic.Thematic;

/**
 * @author Li Cheng
 * @date 2019/5/20 16:11
 */
public interface ThematicService {

    /**
     * description: 新增thematic
     */
    Boolean insertThematic(Thematic thematic);

    /**
     * description: 根据专题id 查询专题图层信息
     *
     * @param thematicId 专题id
     * @return java.util.List<cc.wanshan.gis.entity.plot.of2d.Layer>
     **/
    Thematic findByThematicId(String thematicId);
}
