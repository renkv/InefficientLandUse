package com.land.utils;

import java.util.List;

/**
 * @author mcrkw
 * @date 2021年07月01日 11:54
 */
public class AreaUtil {
    public static double getArea(List<Tuple> pointList){
        double area = 0;
        for (int i = 1; i <= pointList.size(); i++) {
            double X = pointList.get(i - 1).getX();
            double Y = pointList.get(i - 1).getY();
            double nextX = pointList.get(i % pointList.size()).getX();
            double nextY = pointList.get(i % pointList.size()).getY();
//            if (i == pointList.size()) {
//                 nextX = pointList.get(0).x;
//                 nextY = pointList.get(0).y;
//            } else {
//                nextX = pointList.get(i).x;
//                nextY = pointList.get(i).y;
//            }
            double temp = X * nextY - nextX * Y;
            area += temp;
        }
        area = Math.abs(area/2.0);
        return area;
    }
}
