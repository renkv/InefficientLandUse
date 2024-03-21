package com.land.utils;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author mcrkw
 * @date 2021年06月29日 10:08
 */
@Data
public class Location {
    //经度
    private BigDecimal lon;
    //纬度
    private BigDecimal lat;
}
