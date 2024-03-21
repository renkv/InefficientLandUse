package com.land.utils;

import java.util.List;

/**
 * @author mcrkw
 * @date 2021年06月29日 10:07
 */
public class PolygonAreaUtil2 {
    /**
     * public class Location {
     *
     *   private BigDecimal lon;
     *
     *   private BigDecimal lat;
     * }
     */

    /**
     * 球面积计算公式
     * @param locationList
     * @return
     */
    public static double calculatePolygonArea(List<Location> locationList) {
        double area = 0;
        int size = locationList.size();
        if (size > 2) {
            double LowX = 0.0;
            double LowY = 0.0;
            double MiddleX = 0.0;
            double MiddleY = 0.0;
            double HighX = 0.0;
            double HighY = 0.0;

            double AM = 0.0;
            double BM = 0.0;
            double CM = 0.0;

            double AL = 0.0;
            double BL = 0.0;
            double CL = 0.0;

            double AH = 0.0;
            double BH = 0.0;
            double CH = 0.0;

            double CoefficientL = 0.0;
            double CoefficientH = 0.0;

            double ALtangent = 0.0;
            double BLtangent = 0.0;
            double CLtangent = 0.0;

            double AHtangent = 0.0;
            double BHtangent = 0.0;
            double CHtangent = 0.0;

            double ANormalLine = 0.0;
            double BNormalLine = 0.0;
            double CNormalLine = 0.0;

            double OrientationValue = 0.0;

            double AngleCos = 0.0;

            double Sum1 = 0.0;
            double Sum2 = 0.0;
            double Count2 = 0;
            double Count1 = 0;

            double Sum = 0.0;
            double Radius = 6378000;

            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    LowX = locationList.get(size-1).getLon().doubleValue() * Math.PI / 180;
                    LowY = locationList.get(size-1).getLat().doubleValue() * Math.PI / 180;
                    MiddleX =locationList.get(0).getLon().doubleValue() * Math.PI / 180;
                    MiddleY = locationList.get(0).getLat().doubleValue() * Math.PI / 180;
                    HighX = locationList.get(1).getLon().doubleValue() * Math.PI / 180;
                    HighY = locationList.get(1).getLat().doubleValue() * Math.PI / 180;
                } else if (i == size - 1) {
                    LowX = locationList.get(size-2).getLon().doubleValue() * Math.PI / 180;
                    LowY = locationList.get(size-2).getLat().doubleValue() * Math.PI / 180;
                    MiddleX =locationList.get(size-1).getLon().doubleValue() * Math.PI / 180;
                    MiddleY = locationList.get(size-1).getLat().doubleValue() * Math.PI / 180;
                    HighX = locationList.get(0).getLon().doubleValue() * Math.PI / 180;
                    HighY = locationList.get(0).getLat().doubleValue() * Math.PI / 180;
                } else {
                    LowX = locationList.get(i-1).getLon().doubleValue() * Math.PI / 180;
                    LowY = locationList.get(i-1).getLat().doubleValue() * Math.PI / 180;
                    MiddleX = locationList.get(i).getLon().doubleValue() * Math.PI / 180;
                    MiddleY = locationList.get(i).getLat().doubleValue() * Math.PI / 180;
                    HighX = locationList.get(i+1).getLon().doubleValue() * Math.PI / 180;
                    HighY = locationList.get(i+1).getLat().doubleValue() * Math.PI / 180;
                }

                AM = Math.cos(MiddleY) * Math.cos(MiddleX);
                BM = Math.cos(MiddleY) * Math.sin(MiddleX);
                CM = Math.sin(MiddleY);
                AL = Math.cos(LowY) * Math.cos(LowX);
                BL = Math.cos(LowY) * Math.sin(LowX);
                CL = Math.sin(LowY);
                AH = Math.cos(HighY) * Math.cos(HighX);
                BH = Math.cos(HighY) * Math.sin(HighX);
                CH = Math.sin(HighY);

                CoefficientL = (AM * AM + BM * BM + CM * CM) / (AM * AL + BM * BL + CM * CL);
                CoefficientH = (AM * AM + BM * BM + CM * CM) / (AM * AH + BM * BH + CM * CH);

                ALtangent = CoefficientL * AL - AM;
                BLtangent = CoefficientL * BL - BM;
                CLtangent = CoefficientL * CL - CM;
                AHtangent = CoefficientH * AH - AM;
                BHtangent = CoefficientH * BH - BM;
                CHtangent = CoefficientH * CH - CM;

                AngleCos = (AHtangent * ALtangent + BHtangent * BLtangent + CHtangent * CLtangent) / (
                        Math.sqrt(AHtangent * AHtangent + BHtangent * BHtangent + CHtangent * CHtangent)
                                * Math.sqrt(ALtangent * ALtangent + BLtangent * BLtangent
                                + CLtangent * CLtangent));

                AngleCos = Math.acos(AngleCos);

                ANormalLine = BHtangent * CLtangent - CHtangent * BLtangent;
                BNormalLine = 0 - (AHtangent * CLtangent - CHtangent * ALtangent);
                CNormalLine = AHtangent * BLtangent - BHtangent * ALtangent;

                if (AM != 0) {
                    OrientationValue = ANormalLine / AM;
                } else if (BM != 0) {
                    OrientationValue = BNormalLine / BM;
                } else {
                    OrientationValue = CNormalLine / CM;
                }

                if (OrientationValue > 0) {
                    Sum1 += AngleCos;
                    Count1++;

                } else {
                    Sum2 += AngleCos;
                    Count2++;
                    //Sum +=2*Math.PI-AngleCos;
                }
            }
            if (Sum1 > Sum2) {
                Sum = Sum1 + (2 * Math.PI * Count2 - Sum2);
            } else {
                Sum = (2 * Math.PI * Count1 - Sum1) + Sum2;
            }
            //平方米
            area = (Sum - (size - 2) * Math.PI) * Radius * Radius;
        }
        return Math.abs(area);
    }
}
