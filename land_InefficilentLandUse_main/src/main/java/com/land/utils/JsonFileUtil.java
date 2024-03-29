package com.land.utils;

import com.alibaba.fastjson.JSONObject;
import org.beetl.ext.simulate.JsonUtil;

import java.io.*;
import java.util.HashMap;

public class JsonFileUtil {
    public static void crateJson(JSONObject jsonObject,String filename) {
        try {

            String orcPath = "D:\\project\\tudi\\land_InefficientLandUse_System\\land_InefficilentLandUse_main\\target\\classes\\assets\\dist\\GeoJson\\"+filename+".json";

            String jsonString = jsonObject.toJSONString();
            // 生成json文件
            tempFile(orcPath, jsonString);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void tempFile(String filePath, String jsonData) throws IOException {
        // 保证创建一个新文件
        File file = new File(filePath);
        if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
            file.getParentFile().mkdirs();
        }
        if (file.exists()) { // 如果已存在,删除旧文件
            file.delete();
        }
        file.createNewFile();

        // 格式化json字符串
       // jsonData = JsonUtil.formatJson(jsonData);

        // 将格式化后的字符串写入文件
        Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        write.write(jsonData);
        write.flush();
        write.close();
    }

}
