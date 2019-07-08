package cc.wanshan.gis.entity.search;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Suggest {

    private String type;

    private Set<String> suggestSet;

}
