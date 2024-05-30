package com.land.utils;

import com.alibaba.fastjson.JSONObject;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.dsig.TransformException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.land.utils.ShapeFileUtil.getFeatureCollectionByShpFile;

/**
 * @author soulmate丶
 * @date 2021-10-26
 */
public class ZipUtil {

    private static final Logger log = LoggerFactory.getLogger(ZipUtil.class);
    /**
     * 保存zip文件到本地并调用解压方法并返回解压出的文件的路径集合
     *
     * @param file 文件
     * @return list //解压出的文件的路径合集
     */
    private static String zipPath = "f:/shpfile/";//zip根路径

    /**
     * zip解压
     *
     * @param srcFile     zip源文件
     * @param destDirPath 解压后的目标文件夹
     * @return list 解压文件的路径合集
     * @throws RuntimeException 解压失败会抛出运行时异常
     */
    public static List<String> unZipFiles(File srcFile, String destDirPath) throws RuntimeException {
        List<String> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
        }
        // 开始解压
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile, Charset.forName("GBK"));
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                log.info("解压" + entry.getName());
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    String dirPath = destDirPath + File.separator + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(destDirPath + File.separator + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    log.info(destDirPath + entry.getName());
                    list.add(destDirPath + entry.getName());
                    if (!targetFile.getParentFile().exists()) {
                        log.info("父文件不存在");
                    }
                    targetFile.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    // 关流顺序，先打开的后关闭
                    fos.close();
                    is.close();
                }
            }
            long end = System.currentTimeMillis();
            log.info("解压完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("unzip error from ZipUtils", e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * @param filePath 临时文件的删除
     *                 删除文件夹里面子目录
     *                 再删除文件夹
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if ((!file.exists()) || (!file.isDirectory())) {
            log.info("file not exist");
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (filePath.endsWith(File.separator)) {
                temp = new File(filePath + tempList[i]);
            } else {
                temp = new File(filePath + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                deleteFiles(filePath + "\\" + tempList[i]);
            }
        }
        // 空文件的删除
        file.delete();
    }

    /**
     * @param zipFile:
     * @return JSONObject
     * @author pangshicheng
     * @description 通过shp压缩文件，将其转换为GeoJson格式
     * @date 2023/7/18 16:04
     */
    public static JSONObject shpToGeoJson(File zipFile) {
        FeatureJSON fjson = new FeatureJSON();
        JSONObject geoJsonObject=new JSONObject();
        geoJsonObject.put("type","FeatureCollection");
        try {
            // 获取FeatureCollection
            FeatureCollection collection = getFeatureCollectionByShpFile(zipFile);
            FeatureIterator iterator = collection.features();
            List<JSONObject> array  = new ArrayList<JSONObject>();
            MathTransform transform = projectTransform("EPSG:4526","EPSG:4490");
            //遍历feature转为json对象
            while (iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                Geometry geometry22 = JTS.transform(geometry,transform);
                feature.setDefaultGeometry(geometry22);
                StringWriter writer = new StringWriter();
                fjson.writeFeature(feature, writer);
                String temp = writer.toString();
                /*byte[] b = temp.getBytes("iso8859-1");
                temp = new String(b, "UTF-8");*/
                JSONObject json = JSONObject.parseObject(temp);
                array.add(json);
            }
            iterator.close();
            //添加到geojsonObject
            geoJsonObject.put("features",array);
            iterator.close();

        }catch (Exception e){
            e.getMessage();
        }
        return geoJsonObject;
    }

    public static JSONObject shpToGeoJsonByKey(File zipFile,String key,String value) {
        FeatureJSON fjson = new FeatureJSON();
        JSONObject geoJsonObject=new JSONObject();
        geoJsonObject.put("type","FeatureCollection");
        try {
            // 获取FeatureCollection
            FeatureCollection collection = getFeatureCollectionByShpFile(zipFile);
            FeatureIterator iterator = collection.features();
            List<JSONObject> array  = new ArrayList<JSONObject>();
            MathTransform transform = projectTransform("EPSG:4526","EPSG:4490");
            //遍历feature转为json对象
            while (iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                Geometry geometry22 = JTS.transform(geometry,transform);
                feature.setDefaultGeometry(geometry22);
                StringWriter writer = new StringWriter();
                fjson.writeFeature(feature, writer);
                String temp = writer.toString();
                /*byte[] b = temp.getBytes("iso8859-1");
                temp = new String(b, "UTF-8");*/
                JSONObject json = JSONObject.parseObject(temp);
                for(String k : json.keySet()){
                    if(k.equals("properties")){
                        JSONObject job = (JSONObject) json.get(k);
                        for(String ky : job.keySet()){
                            if(ky.equals(key) && job.get(ky).equals(value)){
                                array.add(json);
                            }
                        }
                    }
                }
            }
            iterator.close();
            //添加到geojsonObject
            geoJsonObject.put("features",array);
            iterator.close();

        }catch (Exception e){
            e.getMessage();
        }
        return geoJsonObject;

    }

    /**
     * 投影转换， lon=经度，lat=纬度，ESPG格式（例）：EPSG:4610  epsgSource 4526  epsgTarget 4490
     */
    public static MathTransform projectTransform(
                                         String epsgSource, String epsgTarget) throws FactoryException,
            MismatchedDimensionException, TransformException {

        // 定义转换前和转换后的投影，可以用ESPG或者wkt
        // "PROJCS[\"Xian_1980_3_Degree_GK_CM_111E\",GEOGCS[\"GCS_Xian_1980\",DATUM[\"D_Xian_1980\",SPHEROID[\"Xian_1980\",6378140.0,298.257]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Gauss_Kruger\"],PARAMETER[\"False_Easting\",500000.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",111.0],PARAMETER[\"Scale_Factor\",1.0],PARAMETER[\"Latitude_Of_Origin\",0.0],UNIT[\"Meter\",1.0]]";
        // CoordinateReferenceSystem mercatroCRS = CRS.parseWKT(strWKTMercator);
        CoordinateReferenceSystem crsSource = CRS.decode(epsgSource,true);
        CoordinateReferenceSystem crsTarget = CRS.decode(epsgTarget,true);
        // 投影转换
        MathTransform transform = CRS.findMathTransform(crsSource, crsTarget,true);

        return transform;
    }

    public static void main(String[] args) {
        File file = new File("F:\\desktop\\低效用地\\11.zip");
        try {
            JSONObject obj = ZipUtil.shpToGeoJson(file);
           // JSONObject obj = ZipUtil.shpToGeoJsonByKey(file,"DKBH","CA33");
            System.out.println(obj.toJSONString());
//            System.out.println(obj.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

