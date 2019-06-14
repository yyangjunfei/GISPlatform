package cc.wanshan.gis.entity.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Properties {

    private Long id;
    private String provincesName;
    private String cityName;
    private String areaName;
    private String firstName;
    private String secondName;
    private String baiduFirstName;
    private String baiduSecondName;
    private String name;
    private String phone;
    private String addr;
}
