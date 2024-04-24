package com.land.utils;
import net.sourceforge.pinyin4j.PinyinHelper;
public class PinyinUtil {

        /**
         * @Description: 提取每个字符的首字母(大写)
         * @DateTime: 17:20 2023/4/27
         * @Params:
         * @Return
         */
        public static String getPinYinHeadChar(String str) {
            if (str == null || str.trim().equals("")) {
                return "";
            }
            String convert = "";
            for (int j = 0; j < str.length(); j++) {
                char word = str.charAt(j);
                // 提取字符的首字母
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
                if (pinyinArray != null) {
                    convert += pinyinArray[0].charAt(0);
                } else {
                    convert += word;
                }
            }
//        去除字符中包含的空格
          convert = convert.replace(" ","");
//        字符转小写
//        convert.toLowerCase();
            return convert.toUpperCase();
        }

    public static void main(String[] args) {
        String data = "我是  谁";
        System.out.println(getPinYinHeadChar(data));
    }
}
