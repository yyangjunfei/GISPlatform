package cc.wanshan.gis.entity.provider;

import cc.wanshan.gis.entity.drawlayer.Layer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Li Cheng
 * @date 2019/6/4 15:04
 */

public class LayerDaoProvider {
  private static final Logger logger= LoggerFactory.getLogger(LayerDaoProvider.class);
  /**
   * description: 根据layerId批量删除
   *
   * @param map layerId集合
   * @return java.lang.String
   **/
  public String deleteAll(Map map) {
    logger.info("deleteAllByLayerId::map = [{}]",map);
    List<Layer> list = (List<Layer>) map.get("list");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("DELETE FROM layer WHERE layer_id in (");
    for (int i = 0; i < list.size(); i++) {
      stringBuilder.append("'").append(list.get(i).getLayerId()).append("'");
      if (i < list.size() - 1) {
        stringBuilder.append(",");
      }
    }
    stringBuilder.append(")");
    logger.info("stringBuilder.toString()::" + stringBuilder.toString());
    return stringBuilder.toString();
  }
}
