package cc.wanshan.gis.entity.search;

import lombok.Data;

@Data
public class Poi {

    private Long id;

    private String province;

    private String city;

    private String county;

    private String firstClass;

    private String secondClass;

    private String thirdClass;

    private String name;

    private String autocomplete;

    private String telephone;

    private String address;

    private String geometry;

//    private String adcode;

//    private String typecode;

}
