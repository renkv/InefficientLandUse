package com.land.utils;

import org.apache.poi.hpsf.Decimal;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mcrkw
 * @date 2021年06月30日 14:53
 */
public class TxtUtil {
    /**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */
    public static void readTxtFile(String filePath){
        try {
            String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                List<Map<String,Object>> dkList = new ArrayList<>();
                Map<String,Object> map = new HashMap<>();
                String numStr = "";
                while((lineTxt = bufferedReader.readLine()) != null){
                   // System.out.println(lineTxt);
                    if(lineTxt.contains("=")){

                    }else if(lineTxt.contains(",")){
                        String[] tmp = lineTxt.split(",");    //根据','将每行数据拆分成一个数组
                        if(tmp.length > 0){
                            if(tmp[0].startsWith("J")){
                                Map<String,Object> dkMap = (Map<String, Object>) map.get(numStr);
                                List<Tuple> list;
                                if(tmp[1].equals("1")){
                                    list = (List<Tuple>) dkMap.get("1");
                                }else{
                                    boolean contains=dkMap.containsKey(tmp[1]);
                                    if(contains){
                                         list = (List<Tuple>) dkMap.get(tmp[1]);
                                    }else{
                                        list = new ArrayList<>();
                                    }
                                }
                                String yStr = tmp[2];
                                String xStr = tmp[3].substring(2);
                                Tuple tuple = new Tuple();
                                tuple.setX(Double.parseDouble(xStr));
                                tuple.setY(Double.parseDouble(yStr));
                                list.add(tuple);
                                dkMap.put(tmp[1],list);
                            }else{
                                numStr = tmp[2];
                                List<Tuple> list = new ArrayList<>();
                                Map<String,Object> dkMap = new HashMap<>();
                                dkMap.put("1",list);
                                dkMap.put("area",tmp[1]);
                                map.put(numStr,dkMap);
                            }
                        }
                    }
                }
                System.out.println(map.size());
                for (String key : map.keySet()) {
                    Map<String,Object> m = (Map<String, Object>) map.get(key);
                    for (String k : m.keySet()) {
                        if(k.equals("1")){
                            List<Tuple> list = (List<Tuple>) m.get(k);
                            List<Location> locationList = new ArrayList<>();
                            List<double[]> points = new ArrayList<double[]> ();
                            for(Tuple t : list){
                                Tuple t1 = GaussGeoUtil.GKgetJW(2,t.getY(),t.getX(),114,0,500000,0);
                                double[] point = {t1.getLdec() , t1.getBdec()};
                                points.add(point);
                                Location l = new Location();
                                l.setLat(new BigDecimal(Double.toString(t1.getBdec())));
                                l.setLon(new BigDecimal(Double.toString(t1.getLdec())));
                                locationList.add(l);
                            }
                            PolygonAreaUtil tp = new PolygonAreaUtil();
                            double result = tp.calculateArea(points);
                            PolygonAreaUtil2 tp2 = new PolygonAreaUtil2();
                            result = tp2.calculatePolygonArea(locationList);
                            System.out.println("面积2=" + result);
                            result = AreaUtil.getArea(list);
                            System.out.println("面积3=" + result);
                        }else{
                            System.out.println("key=" + key + ", value=" + map.get(key));
                        }
                    }
                    System.out.println("key=" + key + ", value=" + map.get(key));
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

    }

    public static void main(String argv[]){
        String filePath = "D:\\7、赵县三期砖瓦窑坐标0527.txt";
        readTxtFile(filePath);
    }
}
