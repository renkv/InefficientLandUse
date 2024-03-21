package com.land.sys.core.util;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;
/**
 * @author mcrkw
 * @date 2021年06月22日 9:37
 */
public class Base64Util {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String str = "suolong2014version";
        System.out.println("测试明文[" + str + "]");
        String basecode = Base64Util.encodeBase64(str);
        System.out.println("加密后[" + basecode + "]");

        if (basecode != null) {
            String res = Base64Util.decodeBase64(basecode);
            System.out.println("解密后[" + res + "]");
        }
        String str2 = "bGhiQDEyMzQ=";
        System.out.println(str2);
        if (str2 != null) {
            String res = Base64Util.decodeBase64(str2);
            System.out.println("解密后[" + res + "]");
        }
        // ///
        System.out.println("");
        System.out.println("N次加密测试--------");
        String basecodeN = Base64Util.encodeBase64(str, 2);
        String resN = Base64Util.decodeBase64(basecodeN, 2);
        String basecodeN3 = Base64Util.encodeBase64(str, 5);
        String resN3 = Base64Util.decodeBase64(basecodeN3, 5);
    }

    // 提供加密N次
    public static String encodeBase64(String mingwen, int times) {
        int num = (times <= 0) ? 1 : times;
        String code = "";
        if (mingwen == null || mingwen.equals("")) {
        } else {
            code = mingwen;
            for (int i = 0; i < num; i++) {
                code = encodeBase64(code);
            }
        }
        return code;
    }

    // 对应提供解密N次
    public static String decodeBase64(String mi, int times) {
        int num = (times <= 0) ? 1 : times;
        String mingwen = "";
        if (mi == null || mi.equals("")) {
        } else {
            mingwen = mi;
            for (int i = 0; i < num; i++) {
                mingwen = decodeBase64(mingwen);
            }
        }
        return mingwen;
    }

    // /
    public static String encodeBase64(String mingwen) {
        String code = "";
        if (mingwen == null || mingwen.equals("")) {
        } else {
            Encoder encoder = Base64.getEncoder();
            try {
                code = encoder.encodeToString(mingwen.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return code;
    }

    public static String decodeBase64(String mi) {
        String mingwen = "";
        if (mi == null || mi.equals("")) {
        } else {
            //BASE64Decoder decoder = new BASE64Decoder();
            Decoder decoder = Base64.getDecoder();
            try {
                byte[] by = decoder.decode(mi);
                mingwen = new String(by,"UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mingwen;
    }
}
