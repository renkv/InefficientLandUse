package com.land.utils;


import javax.validation.constraints.NotNull;

/**
 * @author mcrkw
 * @date 2021年06月29日 10:51
 */
public class JavaRtkUtils {
    private  double p = 206264.80624709636D;
    @NotNull
    public  Tuple xyTowgs84(double x, double y, double L0) {
        double a = 6378137.0D;
        double efang = 0.0066943799901413D;
        double e2fang = 0.0067394967422764D;
        y = y - (double)500000;
        double m0 = 0.0D;
        double m2 = 0.0D;
        double m4 = 0.0D;
        double m6 = 0.0D;
        double m8 = 0.0D;
        m0 = a * ((double)1 - efang);
        m2 = 1.5D * efang * m0;
        m4 = efang * m2 * 5.0D / 4.0D;
        m6 = efang * m4 * 7.0D / 6.0D;
        m8 = efang * m6 * 9.0D / 8.0D;
        double a0 = 0.0D;
        double a2 = 0.0D;
        double a4 = 0.0D;
        double a6 = 0.0D;
        double a8 = 0.0D;
        a0 = m0 + m2 / 2.0D + m4 * 3.0D / 8.0D + m6 * 5.0D / 16.0D + m8 * 35.0D / 128.0D;
        a2 = m2 / 2.0D + m4 / 2.0D + m6 * 15.0D / 32.0D + m8 * 7.0D / 16.0D;
        a4 = m4 / 8.0D + m6 * 3.0D / 16.0D + m8 * 7.0D / 32.0D;
        a6 = m6 / 32.0D + m8 / 16.0D;
        a8 = m8 / 128.0D;
        double FBf = 0.0D;
        double Bf0 = x / a0;

        for(double Bf1 = 0.0D; Bf0 - Bf1 >= 1.0E-4D; Bf0 = (x - FBf) / a0) {
            Bf1 = Bf0;
            FBf = -a2 * Math.sin((double)2 * Bf0) / (double)2 + a4 * Math.sin((double)4 * Bf0) / (double)4 - a6 * Math.sin((double)6 * Bf0) / (double)6 + a8 * Math.sin((double)8 * Bf0) / (double)8;
        }

        double Wf = Math.sqrt((double)1 - efang * Math.sin(Bf0) * Math.sin(Bf0));
        double Nf = a / Wf;
        double Mf = a * ((double)1 - efang) / Math.pow(Wf, 3.0D);
        double nffang = e2fang * Math.cos(Bf0) * Math.cos(Bf0);
        double tf = Math.tan(Bf0);
        double B = Bf0 - tf * y * y / ((double)2 * Mf * Nf) + tf * ((double)5 + (double)3 * tf * tf + nffang - (double)9 * nffang * tf * tf) * Math.pow(y, 4.0D) / ((double)24 * Mf * Math.pow(Nf, 3.0D)) - tf * ((double)61 + (double)90 * tf * tf + (double)45 * Math.pow(tf, 4.0D)) * Math.pow(y, 6.0D) / ((double)720 * Mf * Math.pow(Nf, 5.0D));
        double l = y / (Nf * Math.cos(Bf0)) - ((double)1 + (double)2 * tf * tf + nffang) * Math.pow(y, 3.0D) / ((double)6 * Math.pow(Nf, 3.0D) * Math.cos(Bf0)) + ((double)5 + (double)28 * tf * tf + (double)24 * Math.pow(tf, 4.0D)) * Math.pow(y, 5.0D) / ((double)120 * Math.pow(Nf, 5.0D) * Math.cos(Bf0));
        double L = l + L0;
        double[] array_B = this.rad2dms(B);
        double[] array_L = this.rad2dms(L);
        double Bdec = this.dms2dec(array_B);
        double Ldec = this.dms2dec(array_L);
        Tuple tuple = new  Tuple();
        tuple.setBdec(Bdec);
        tuple.setLdec(Ldec);
        return tuple;
    }
    public  double gaussLongToDegreen(double B, double L, int N) {
        double L00 = (double)Math.round(L / (double)3) * (double)3;
        return L00 / (double)180 * 3.1415926D;
    }
    @NotNull
    public  double[] rad2dms(double rad) {
        double[] a = new double[]{0.0D, 0.0D, 0.0D};
        double dms = rad * p;
        a[0] = Math.floor(dms / 3600.0D);
        a[1] = Math.floor((dms - a[0] * (double)3600) / 60.0D);
        a[2] = (double)((int)Math.floor(dms - a[0] * (double)3600)) - a[1] * (double)60;
        return a;
    }

    public  double dms2dec(@NotNull double[] dms) {
        double dec = 0.0D;
        dec = dms[0] + dms[1] / 60.0D + dms[2] / 3600.0D;
        return dec;
    }

    @NotNull
    public  Tuple GetXY(double B, double L, double degree) {
        double[] xy = new double[]{0.0D, 0.0D};
        double a = 6378137.0D;
        double b = 6356752.314245179D;
        double e = 0.081819190842621D;
        double eC = 0.0820944379496957D;
        double L0 = 0.0D;
        int n;
        if (degree == 6.0D) {
            n = (int)Math.round((L + degree / (double)2) / degree);
            L0 = degree * (double)n - degree / (double)2;
        } else {
            n = (int)Math.round(L / degree);
            L0 = degree * (double)n;
        }

        double radB = B * 3.141592653589793D / (double)180;
        double radL = L * 3.141592653589793D / (double)180;
        double deltaL = (L - L0) * 3.141592653589793D / (double)180;
        double N = a * a / b / Math.sqrt((double)1 + eC * eC * Math.cos(radB) * Math.cos(radB));
        double C1 = 1.0D + 0.75D * e * e + 0.703125D * Math.pow(e, 4.0D) + 0.68359375D * Math.pow(e, 6.0D) + 0.67291259765625D * Math.pow(e, 8.0D);
        double C2 = 0.75D * e * e + 0.9375D * Math.pow(e, 4.0D) + 1.025390625D * Math.pow(e, 6.0D) + 1.07666015625D * Math.pow(e, 8.0D);
        double C3 = 0.234375D * Math.pow(e, 4.0D) + 0.41015625D * Math.pow(e, 6.0D) + 0.538330078125D * Math.pow(e, 8.0D);
        double C4 = 0.068359375D * Math.pow(e, 6.0D) + 0.15380859375D * Math.pow(e, 8.0D);
        double C5 = 0.00240325927734375D * Math.pow(e, 8.0D);
        double t = Math.tan(radB);
        double eta = eC * Math.cos(radB);
        double X = a * ((double)1 - e * e) * (C1 * radB - C2 * Math.sin((double)2 * radB) / (double)2 + C3 * Math.sin((double)4 * radB) / (double)4 - C4 * Math.sin((double)6 * radB) / (double)6 + C5 * Math.sin((double)8 * radB));
        xy[0] = X + N * Math.sin(radB) * Math.cos(radB) * Math.pow(deltaL, 2.0D) * ((double)1 + Math.pow(deltaL * Math.cos(radB), 2.0D) * ((double)5 - t * t + (double)9 * eta * eta + (double)4 * Math.pow(eta, 4.0D)) / (double)12 + Math.pow(deltaL * Math.cos(radB), 4.0D) * ((double)61 - (double)58 * t * t + Math.pow(t, 4.0D)) / (double)360) / (double)2;
        xy[1] = N * deltaL * Math.cos(radB) * ((double)1 + Math.pow(deltaL * Math.cos(radB), 2.0D) * ((double)1 - t * t + eta * eta) / (double)6 + Math.pow(deltaL * Math.cos(radB), 4.0D) * ((double)5 - (double)18 * t * t + Math.pow(t, 4.0D) - (double)14 * eta * eta - (double)58 * eta * eta * t * t) / (double)120) + (double)500000;
        Tuple tuple = new  Tuple();
        tuple.setBdec(xy[0]);
        tuple.setLdec(xy[1]);
        return tuple;
    }

    public static void main(String[] args) {
        JavaRtkUtils rtkUtils=new JavaRtkUtils();
        Tuple tuple1=rtkUtils.GetXY(37.203267527777776, 115.40381543333334, 3.0);
        double degreen1 = rtkUtils.gaussLongToDegreen(37.203267527777776, 115.40381543333334, 3);
        Tuple xyTowgs841 = rtkUtils.xyTowgs84(4119992.7554638153,624625.9309849278, degreen1);
        Tuple xyTowgs8412 = rtkUtils.xyTowgs84(1015085.547,109975.560, degreen1);
        System.out.println(xyTowgs841.getBdec()+","+xyTowgs841.getLdec());
        System.out.println(xyTowgs8412.getBdec()+","+xyTowgs8412.getLdec());
    }
}
