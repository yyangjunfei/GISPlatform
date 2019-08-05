package cc.wanshan.gis.service.layer.export;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.plot.of2d.Layer;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Li Cheng
 * @date 2019/6/14 16:25
 */
public interface ExportService {
    /**
     * description:
     *
     * @param layerId 图层Id
    	* @param suffix 导出类型
     * @return cc.wanshan.gis.common.pojo.Result
     **/
    void export(String layerId,String suffix, HttpServletResponse response) throws Exception;

    /**
     * description: 将layer的shp格式导出
     *
     * @param layer 需要导出的图层
     * @return java.lang.Boolean
     **/
    Boolean writeSHP(Layer layer) throws Exception;

    /**
     * description:  将layer的JSON格式导出
     *
     * @param layer 需要导出的图层
     * @return java.lang.Boolean
     **/
    Boolean writeJSON(Layer layer) throws IOException;

    /**
     * description: 将layer的XML格式导出
     *
     * @param layer 需要导出的图层
     * @return java.lang.Boolean
     **/
    Boolean writeKML(Layer layer);

    /**
     * description: 将layer的GPS格式导出
     *
     * @param layer 需要导出的图层
     * @return java.lang.Boolean
     **/
    Boolean writeGPS(Layer layer);
}
