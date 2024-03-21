package com.land.utils;

import static java.lang.Math.*;



/**
 * @author mcrkw
 * @date 2021年06月30日 10:42
 */
public class GaussGeoUtil {

    public static void main(String[]args){
        Tuple t = GaussGeoUtil.GaussToGeo(573250.803,4179071.287,3,114);
        System.out.println(t.getBdec()+","+t.getLdec());
        Tuple t1 = GaussGeoUtil.GKgetJW(3,4179071.287,573250.803,114,0,500000,0);
        System.out.println(t1.getBdec()+","+t1.getLdec());
    }
    /**
     * 用于初始化椭球参数
     * @param {*} TuoqiuCanshu 枚举类型，提供北京54、西安80、WGS84、CGCS2000椭球参数
     * @param {*} CentralMeridian 中央经线
     * @param {*} OriginLatitude 原点纬度，如果是标准的分幅，则该参数是0
     * @param {*} EastOffset 东偏移
     * @param {*} NorthOffset 北偏移
     */
    public static Tuple GKgetJW(int TuoqiuCanshu, double y, double x,double CentralMeridian,double OriginLatitude,double EastOffset,double NorthOffset){
        //基本变量定义
        double a=0;//'椭球体长半轴
        double b=0;// '椭球体短半轴
        double f=0; //'扁率
        double e=0;// '第一偏心率
        double e1=0;//'第二偏心率

        double FE=0;//'东偏移
        double FN=0;//'北偏移
        double L0=0;//'中央经度
        double W0=0;//'原点纬线
        double k0=0;//'比例因子
        // double PI=3.14159265358979;
        /*
             *  Canshu
             *  Beijing54 = 0;Krassovsky （北京54采用） 6378245 6356863.0188
                Xian80 = 1;IAG 75（西安80采用） 6378140 6356755.2882
                WGS84 = 2;WGS 84 6378137 6356752.3142
                CGCS2000 = 3
            */
        if (TuoqiuCanshu == 0)//北京五四
        {
            a = 6378245;
            b = 6356863.0188;
        }
        if (TuoqiuCanshu == 1)// '西安八零
        {
            a = 6378140;
            b = 6356755.2882;
        }
        if (TuoqiuCanshu == 2)//'WGS84
        {
            a = 6378137;
            b = 6356752.3142;
        }
        if (TuoqiuCanshu == 3)//'CGCS2000
        {
            a = 6378137;
            b = 6356752.314140356;
        }
        f = (a - b) / a;//扁率
        //e = Math.sqrt(1 - Math.pow((b / a) ,2));//'第一偏心率
        e = Math.sqrt(2 * f - Math.pow(f, 2));//'第一偏心率

        //eq = Math.sqrt(Math.pow((a / b) , 2) - 1);//'第二偏心率
        e1 = e / Math.sqrt(1 - Math.pow(e, 2));//'第二偏心率

        L0 = CentralMeridian;//中央经
        W0 = OriginLatitude;//原点纬线
        k0 = 1;//'比例因子
        FE = EastOffset;//东偏移
        FN = NorthOffset;//北偏移

        Tuple tuple = new Tuple();
        double El1 = (1 - Math.sqrt(1 - Math.pow(e, 2))) / (1 + Math.sqrt(1 - Math.pow(e, 2)));
        double Mf = (y - FN) / k0;//真实坐标值
        double Q = Mf / (a * (1 - Math.pow(e, 2) / 4 - 3 * Math.pow(e, 4) / 64 - 5 * Math.pow(e, 6) / 256));//角度
        double Bf = Q + (3 * El1 / 2 - 27 * Math.pow(El1, 3) / 32) * Math.sin(2 * Q) + (21 * Math.pow(El1, 2) / 16 - 55 * Math.pow(El1, 4) / 32) * Math.sin(4 * Q) + (151 * Math.pow(El1, 3) / 96) * Math.sin(6 * Q) + 1097 / 512 * Math.pow(El1, 4) * Math.sin(8 * Q);
        double Rf = a * (1 - Math.pow(e, 2)) / Math.sqrt(Math.pow((1 - Math.pow((e * Math.sin(Bf)), 2)), 3));
        double Nf = a / Math.sqrt(1 - Math.pow((e * Math.sin(Bf)), 2));//卯酉圈曲率半径
        double Tf = Math.pow((Math.tan(Bf)), 2);
        double D = (x - FE) / (k0 * Nf);

        double Cf = Math.pow(e1, 2) * Math.pow((Math.cos(Bf)), 2);

        double B = Bf - Nf * Math.tan(Bf) / Rf * (Math.pow(D, 2) / 2 - (5 + 3 * Tf + 10 * Cf - 9 * Tf * Cf - 4 * Math.pow(Cf, 2) - 9 * Math.pow(e1, 2)) * Math.pow(D, 4) / 24 + (61 + 90 * Tf + 45 * Math.pow(Tf, 2) - 256 * Math.pow(e1, 2) - 3 * Math.pow(Cf, 2)) * Math.pow(D, 6) / 720);
        double L = CentralMeridian * Math.PI / 180 + 1 / Math.cos(Bf) * (D - (1 + 2 * Tf + Cf) * Math.pow(D, 3) / 6 + (5 - 2 * Cf + 28 * Tf - 3 * Math.pow(Cf, 2) + 8 * Math.pow(e1, 2) + 24 * Math.pow(Tf, 2)) * Math.pow(D, 5) / 120);

        double Bangle = B * 180 / Math.PI;
        double Langle = L * 180 / Math.PI;
        tuple.setLdec(Langle);
        tuple.setBdec(Bangle + W0);
        return tuple;
    }
    /* 功能说明： 将绝对高斯坐标(y,x)转换成绝对的地理坐标(wd,jd)。
     * double y;   输入参数: 高斯坐标的横坐标，以米为单位
     *  double x;   输入参数: 高斯坐标的纵坐标，以米为单位
     * int  DH;  输入参数: 带号，表示上述高斯坐标是哪个带的
     * double LP 中央经线
     */
    public static Tuple GaussToGeo(double y, double x, int DH,double LP){
        double l0;    //  经差
        double tf;    //  tf = tg(Bf0),注意要将Bf转换成以弧度为单位
        double nf ;    //  n = y * sqrt( 1 + etf ** 2) / c, 其中etf = e'**2 * cos(Bf0) ** 2
        double t_l0;   //  l0，经差，以度为单位
        double t_B0;   //  B0，纬度，以度为单位
        double Bf0;    //  Bf0
        double etf;    //  etf,其中etf = e'**2 * cos(Bf0) ** 2
        double X_3 ;
        double Ldec;
        double Bdec;
        Tuple tuple = new Tuple();

        double PI = 3.14159265358979;
        double b_e2 = 0.0067385254147;
        double b_c = 6399698.90178271;

        X_3 = x / 1000000.00  - 3 ;      // 以兆米（1000000）为单位
        // 对于克拉索夫斯基椭球，计算Bf0
        Bf0 = 27.11115372595 + 9.02468257083 * X_3 - 0.00579740442 * Math.pow(X_3,2)
                - 0.00043532572 * Math.pow(X_3,3) + 0.00004857285 * Math.pow(X_3,4)
                + 0.00000215727 * Math.pow(X_3,5) - 0.00000019399 * Math.pow(X_3,6) ;
        tf = Math.tan(Bf0*PI/180);       //  tf = tg(Bf),注意这里将Bf转换成以弧度为单位
        etf = b_e2 * Math.pow(Math.cos(Bf0*PI/180),2);   //  etf = e'**2 * cos(Bf) ** 2
        nf = y * Math.sqrt( 1 + etf ) / b_c;     //  n = y * sqrt( 1 + etf ** 2) / c
        // 计算纬度，注意这里计算出来的结果是以度为单位的
        t_B0 = Bf0 - (1.0+etf) * tf / PI * (90.0 * Math.pow(nf,2)
                - 7.5 * (5.0 + 3 * Math.pow(tf,2) + etf - 9 * etf * Math.pow(tf,2)) * Math.pow(nf,4)
                + 0.25 * (61 + 90 * Math.pow(tf,2) + 45 * Math.pow(tf,4)) * pow(nf,6))     ;
        // 计算经差，注意这里计算出来的结果是以度为单位的
        t_l0 = (180 * nf - 30 * ( 1 + 2 * Math.pow(tf,2) + etf ) * Math.pow(nf,3)
                + 1.5 * (5 + 28 * Math.pow(tf,2) + 24 * Math.pow(tf,4)) * Math.pow(nf,5))
                / ( PI * Math.cos(Bf0*PI/180) ) ;
        l0 = t_l0;

        if (LP == -1000)
        {
        Ldec = (double)((DH * 6 - 3) + l0);  // 根据带号计算出以度为单位的绝对经度，返回指针
        }
        else
        {
            Ldec = LP + l0;  // 根据带号计算出以秒为单位的绝对经度，返回指针
        }
        //----------------------------------
        Bdec = t_B0 ;     //  返回指针
        tuple.setLdec(Ldec);
        tuple.setBdec(Bdec);
        return tuple;
    }

    /* 适用范围： 本函数适用于将地球东半球中北半球（即东经0度到东经180度，北纬0度至90度）范围
    内所有地理坐标到高斯坐标的转换            */
    /* 使用说明： 调用本函数后返回的结果应在满足精度的条件下进行四舍五入      */
// double jd;   输入参数: 地理坐标的经度，以度为单位
// double wd;   输入参数: 地理坐标的纬度，以度为单位
// short  DH;   输入参数: 三度带或六度带的带号
/*  六度带(三度带)的带号是这样得到的：从东经0度到东经180度自西向东按每6度(3度)顺序编号
 (编号从1开始)，这个顺序编号就称为六度带(三度带)的带号。因此，六度带的带号的范围是1-30，
 三度带的带号的范围是1-60。
  如果一个点在图号为TH的图幅中，那麽该点所处的六度带的带号就可以这样得到：将该图号的
 第3、4位组成的字符串先转换成数字，再减去30。例如某点在图幅06490701中，该点所在的带号就
 是49-30，即19。
  如果调用本函数去进行一般的从地理坐标到基于六度带高斯坐标的变换（非邻带转换），则参
 数DH的选取按前一段的方法去确定。
  如果调用本函数去进行基于六度带邻带转换，则参数DH的选取先按上述方法去确定，然后看是
 往前一个带还是后一个带进行邻带转换再确定是加1还是减1。         */
    private void GeoToGauss(double jd, double wd, short DH, short DH_width, double LP)
    {
        double t;     //  t=tgB
        double L;     //  中央经线的经度
        double l0;    //  经差
        double jd_hd,wd_hd;  //  将jd、wd转换成以弧度为单位
        double et2;    //  et2 = (e' ** 2) * (cosB ** 2)
        double N;     //  N = C / sqrt(1 + et2)
        double X;     //  克拉索夫斯基椭球中子午弧长
        double m;     //  m = cosB * PI/180 * l0
        double tsin,tcos;   //  sinB,cosB

        double PI=3.14159265358979;
        double b_e2=0.0067385254147;
        double b_c=6399698.90178271;

        jd_hd = jd * PI / 180.0 ;    // 将以秒为单位的经度转换成弧度
        wd_hd = wd * PI / 180.0 ;    // 将以秒为单位的纬度转换成弧度

        // 如果不设中央经线（缺省参数: -1000），则计算中央经线，
        // 否则，使用传入的中央经线，不再使用带号和带宽参数
        //L = (DH - 0.5) * DH_width ;      // 计算中央经线的经度
        if (LP == -1000)
        {
            L = (DH - 0.5) * DH_width ;      // 计算中央经线的经度
        }
        else
        {
            L = LP ;
        }

        l0 = jd - L  ;       // 计算经差
        tsin = sin(wd_hd);        // 计算sinB
        tcos = cos(wd_hd);        // 计算cosB
        // 计算克拉索夫斯基椭球中子午弧长X
        X = 111134.8611 * wd - (32005.7799 * tsin + 133.9238 * pow(tsin,3)
                + 0.6976 * pow(tsin,5) + 0.0039 * pow(tsin,7) ) * tcos;
        et2 = b_e2 * pow(tcos,2) ;      //  et2 = (e' ** 2) * (cosB ** 2)
        N  = b_c / sqrt( 1 + et2 ) ;      //  N = C / sqrt(1 + et2)
        t  = tan(wd_hd);         //  t=tgB
        m  = PI/180 * l0 * tcos;       //  m = cosB * PI/180 * l0
    double xr = X + N * t * ( 0.5 * pow(m,2)
            + (5.0 - pow(t,2) + 9.0 * et2 + 4 * pow(et2,2)) * pow(m,4)/24.0
            + (61.0 - 58.0 * pow(t,2) + pow(t,4)) * pow(m,6) / 720.0 ) ;
    double yr = N * ( m + ( 1.0 - pow(t,2) + et2 ) * pow(m,3) / 6.0
            + ( 5.0 - 18.0 * pow(t,2) + pow(t,4) + 14.0 * et2
            - 58.0 * et2 * pow(t,2) ) * pow(m,5) / 120.0 );
    }
}
