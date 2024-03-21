package com.land.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mcrkw
 * @date 2021年06月28日 14:46
 * L0参数为中央子午线的经线值  石家庄 114
 */
public class XYtoLaLoUtil {
    /**
     * @param X
    	 * @param Y
    	 * @param L0
     * @return double[]
     * @author mcrkw
     * @date 2021/6/28 15:03
     * 2000坐标系转换纬度经度值
     */
    public static double [] xytolatlon(double X, double Y ,double L0) {
        double lat ,lon;
        Y-=500000;
        double []  result  = new double[2];
        double iPI = 0.0174532925199433;//pi/180
        double a = 6378137.0; //长半轴 m
        double b = 6356752.31414; //短半轴 m
        double f = 1/298.257222101;//扁率 a-b/a
        double e = 0.0818191910428; //第一偏心率 Math.sqrt(5)
        double ee = Math.sqrt(a*a-b*b)/b; //第二偏心率
        double bf = 0; //底点纬度
        double a0 = 1+(3*e*e/4) + (45*e*e*e*e/64) + (175*e*e*e*e*e*e/256) + (11025*e*e*e*e*e*e*e*e/16384) + (43659*e*e*e*e*e*e*e*e*e*e/65536);
        double b0 = X/(a*(1-e*e)*a0);
        double c1 = 3*e*e/8 +3*e*e*e*e/16 + 213*e*e*e*e*e*e/2048 + 255*e*e*e*e*e*e*e*e/4096;
        double c2 = 21*e*e*e*e/256 + 21*e*e*e*e*e*e/256 + 533*e*e*e*e*e*e*e*e/8192;
        double c3 = 151*e*e*e*e*e*e*e*e/6144 + 151*e*e*e*e*e*e*e*e/4096;
        double c4 = 1097*e*e*e*e*e*e*e*e/131072;
        bf = b0 + c1*Math.sin(2*b0) + c2*Math.sin(4*b0) +c3*Math.sin(6*b0) + c4*Math.sin(8*b0); // bf =b0+c1*sin2b0 + c2*sin4b0 + c3*sin6b0 +c4*sin8b0 +...
        double tf = Math.tan(bf);
        double n2 = ee*ee*Math.cos(bf)*Math.cos(bf); //第二偏心率平方成bf余弦平方
        double c = a*a/b;
        double v=Math.sqrt(1+ ee*ee*Math.cos(bf)*Math.cos(bf));
        double mf = c/(v*v*v); //子午圈半径
        double nf = c/v;//卯酉圈半径

        //纬度计算
        lat=bf-(tf/(2*mf)*Y)*(Y/nf) * (1-1/12*(5+3*tf*tf+n2-9*n2*tf*tf)*(Y*Y/(nf*nf))+1/360*(61+90*tf*tf+45*tf*tf*tf*tf)*(Y*Y*Y*Y/(nf*nf*nf*nf)));
        //经度偏差
        lon=1/(nf*Math.cos(bf))*Y -(1/(6*nf*nf*nf*Math.cos(bf)))*(1+2*tf*tf +n2)*Y*Y*Y + (1/(120*nf*nf*nf*nf*nf*Math.cos(bf)))*(5+28*tf*tf+24*tf*tf*tf*tf)*Y*Y*Y*Y*Y;
        result[0] =retain6(lat/iPI);
        result[1] =retain6(L0+lon/iPI);
        System.out.println(result[1]+","+result[0]);
        return result;
    }
    /**
     * @param v
     * @return double
     * @author mcrkw
     * @date 2021/6/28 15:02
     * 保留6位小数
     */
    private static double retain6(double v) {
        DecimalFormat d =  new DecimalFormat();
        String style = "00.000000";
        d.applyPattern(style);
        return Double.parseDouble(d.format(v));

    }

/*    public static void GaussForGeodetic(double X, double Y, int type, int Num,double beltNum){
        double Y1 = Y - 500000;
        TuoQiuJiChun tuoQiuJiChun = null;
        switch (Num) {
            case 84:
            // WGS-84坐标系
                tuoQiuJiChun = new TuoQiuJiChun("WGS-84坐标系", 6378137, 6356752.3142);
                break;
            case 80:
                // 西安-80坐标系
                tuoQiuJiChun = new TuoQiuJiChun("西安-80坐标系", 6378140,6356755.2881575287);
                break;
            case 54:// 北京-54坐标系
                tuoQiuJiChun = new TuoQiuJiChun("北京-54坐标系", 6378245,6356863.0187730473);
                break;
        }
        if (Y1 > 1000000) {
            int beltnum = (int) Math.ceil(Y1 / 1000000) - 1;
            Y1 -= beltnum * 1000000 + 500000;
        }
        double m0 = tuoQiuJiChun.getM_Long() * (1 - tuoQiuJiChun.getFirstE());
        double m2 = (double) 3 / 2 * tuoQiuJiChun.getFirstE() * m0;
        double m4 = (double) 5 / 4 * tuoQiuJiChun.getFirstE() * m2;
        double m6 = (double) 7 / 6 * tuoQiuJiChun.getFirstE() * m4;
        double m8 = (double) 9 / 8 * tuoQiuJiChun.getFirstE() * m6;
        double a0, a2, a4, a6, a8;
        a0 = m0 + 1.0 / 2.0 * m2 + 3.0 / 8.0 * m4 + 5.0 / 16.0 * m6 + 35.0/ 128.0 * m8;
        a2 = 1.0 / 2.0 * m2 + 1.0 / 2.0 * m4 + 15.0 / 32.0 * m6 + 7.0 / 16.0 * m8;
        a4 = 1.0 / 8.0 * m4 + 3.0 / 16.0 * m6 + 7.0 / 32.0 * m8;
        a6 = 1.0 / 32.0 * m6 + 1.0 / 16.0 * m8;
        a8 = 1.0 / 128.0 * m8;
        double B1;
        double Bf = X / a0;
        do {
            B1 = Bf;
            Bf = (X + 1.0 / 2.0 * a2 * Math.sin(2 * B1) - 1.0 / 4.0 * a4
                    * Math.sin(4 * B1) + 1.0 / 6.0 * a6 * Math.sin(6 * B1))/ a0;
        } while (Math.abs(B1 - Bf) > 0.00000000001);
        double tf = Math.tan(Bf);
        double it2 = tuoQiuJiChun.getSecondE() * Math.cos(Bf);
        double Nf = tuoQiuJiChun.getM_Long()/ Math.sqrt(1 - tuoQiuJiChun.getFirstE() * Math.sin(Bf)* Math.sin(Bf));
        double Mf = Nf/ (1 + tuoQiuJiChun.getSecondE() * Math.cos(Bf) * Math.cos(Bf));
        double B1f = Bf - (1.0 / 2.0) * tf * Math.pow(Y1, 2)/ (Mf * Nf * Math.cos(Bf)) + (1.0 / 24.0) * tf
                * (5 + 3 * tf * tf + it2 - 9 * it2 * tf * tf) * Math.pow(Y1, 4)/ (Mf * Math.pow(Nf, 3)) - (1.0 / 720.0) * tf
                * (61 + 90 * tf * tf + 45 * tf * tf * tf * tf)
                * Math.pow(Y1, 6) / (Mf * Math.pow(Nf, 5));
        double l = Y1/ (Nf * Math.cos(Bf))- (1.0 / 6.0)* (1 + 2 * tf * tf + it2)* Math.pow(Y1, 3)/ (Math.pow(Nf, 3) * Math.cos(Bf))
                + (1.0 / 120.0)
                * (5 + 28 * tf * tf + 24 * tf * tf * tf * tf + 6 * it2 + 8
                * it2 * tf * tf) * Math.pow(Y1, 5)
                / (Math.pow(Nf, 5) * Math.cos(Bf));
        l = l * 180 / Math.PI;
        double L = l + type * beltNum - ((type == 6) ? 3 : 0);
        double B = B1f * 180 / Math.PI;
    }*/


    public static void main(String[] args) {
        XYtoLaLoUtil util = new XYtoLaLoUtil();
        util.xytolatlon(4179071.287,573250.803,114);
    }
}
