package cc.wanshan.gis.entity.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Properties {

    private Long id;
    private String province;
    private String city;
    private String county;
    private String firstClass;
    private String secondClass;
    private String thirdClass;
    private String name;
    private String telephone;
    private String address;
}
