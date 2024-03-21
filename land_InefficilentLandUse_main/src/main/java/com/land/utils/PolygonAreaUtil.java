package com.land.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mcrkw
 * @date 2021年06月28日 15:13
 * 计算多边形的面积根据网上的地理测绘面积服务算法改写
 */
public class PolygonAreaUtil {
    private static double earthRadiusMeters = 6378000.0;//源码中使用半径为6367460.0   6378137  6371000 6378000.0
    private double metersPerDegree = 2.0 * Math.PI * earthRadiusMeters / 360.0;
    private double radiansPerDegree = Math.PI / 180.0;
    private double degreesPerRadian = 180.0 / Math.PI; 
    public double calculateArea(List<double[]> points) {
        double result = 0;
        if (points.size() > 2) {
            double areaMeters2 = PlanarPolygonAreaMeters2(points);
            if (areaMeters2 > 1000000.0) areaMeters2 = SphericalPolygonAreaMeters2(points);
            System.out.println("面积为："+areaMeters2+"（平方米）");
            result = areaMeters2;
        }
        return result;
    }
    /**
     * @param null
     * @return
     * @author mcrkw
     * @date 2021/6/28 15:19
     * 球面多边形计算面积
     */
    private double SphericalPolygonAreaMeters2(List<double[]> points) {
        double totalAngle = 0.0; 
        for (int i = 0; i < points.size(); ++i) {
            int j = (i + 1) % points.size();
            int k = (i + 2) % points.size();
            totalAngle += Angle(points.get(i), points.get(j), points.get(k));
        }
        double planarTotalAngle = (points.size() - 2) * 180.0;
        double sphericalExcess = totalAngle - planarTotalAngle;
        if (sphericalExcess > 420.0) {
            totalAngle = points.size() * 360.0 - totalAngle;
            sphericalExcess = totalAngle - planarTotalAngle;
        }else if(sphericalExcess > 300.0 && sphericalExcess < 420.0){
            sphericalExcess = Math.abs(360.0 - sphericalExcess);
        }
        return sphericalExcess * radiansPerDegree * earthRadiusMeters * earthRadiusMeters; 

    }
    /**
     * @param null
     * @return
     * @author mcrkw
     * @date 2021/6/28 16:02
     * 角度
     */
    private double Angle(double[] p1, double[] p2, double[] p3) { 
        double bearing21 = Bearing(p2, p1);
        double bearing23 = Bearing(p2, p3);
        double angle = bearing21 - bearing23;
        if (angle < 0.0) angle += 360.0;
        return angle;
    }
    /**
     * @param null
     * @return
     * @author mcrkw
     * @date 2021/6/28 16:04
     * 方向
     */
    private double Bearing(double []from, double[] to) { 
        double lat1 = from[1] * radiansPerDegree;
        double lon1 = from[0] * radiansPerDegree;
        double lat2 = to[1] * radiansPerDegree;
        double lon2 = to[0] * radiansPerDegree;
        double angle = -Math.atan2(Math.sin(lon1 - lon2) * Math.cos(lat2), Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
        if (angle < 0.0) angle += Math.PI * 2.0;
        angle = angle * degreesPerRadian;
        return angle;
    }
    /**
     * @param null
     * @return
     * @author mcrkw
     * @date 2021/6/28 16:06
     * 平面多边形面积
     */
    private double PlanarPolygonAreaMeters2(List<double[]> points) {
        double a = 0.0;
        for (int i = 0; i < points.size(); ++i) {
            int j = (i + 1) % points.size();
            double xi = points.get(i)[0] * metersPerDegree * Math.cos(points.get(i)[1] *radiansPerDegree);
            double yi = points.get(i)[1] * metersPerDegree;
            double xj = points.get(j)[0] * metersPerDegree * Math.cos(points.get(j)[1] *radiansPerDegree);
            double yj = points.get(j)[1] * metersPerDegree; 
            a += xi * yj - xj * yi;
        }
        return Math.abs(a / 2.0);
    }

    public static void main(String[] args) {
        List<double[]> points = new ArrayList<double[]> ();
        String s="112.5293197631836,37.868892669677734;112.5170669555664,37.8605842590332;112.52099609375,37.849857330322266;112.54137420654297,37.85125732421875;112.53511810302734,37.858699798583984";
        String[] s1 = s.split(";");
        for(String ss : s1 ) {
            String[] temp = ss.split(",");
            double[] point = {Double.parseDouble(temp[0]) , Double.parseDouble(temp[1])};
            points.add(point); 
            System.out.println(temp[1]+","+temp[0]); 
        }
        PolygonAreaUtil tp = new PolygonAreaUtil(); 
        tp.calculateArea(points); 
    }
}
