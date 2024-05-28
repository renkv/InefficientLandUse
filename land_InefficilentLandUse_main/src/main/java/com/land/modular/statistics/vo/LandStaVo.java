package com.land.modular.statistics.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class LandStaVo {
    private String xdm;
    private String xmc;
    private String year;
    private String category;
    private String dlmc;
    private String xlmc;
    private String xzyt;
    private String reasonsType;
    private Integer landNum;
    private Double landArea;
    private String landStatus;
}
