package cc.wanshan.gis.entity.search;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegionInput {

    private Double level;

    private String keyword;

    private List<String> regionNameList;


}
