package com.land.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GetAndSetField {
    /**
     * 获取属性值
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getFieldValueByName(Object obj,String fieldName){
        try{
            String firstLetter = fieldName.substring(0,1).toUpperCase();
            String getter = "get"+firstLetter +  fieldName.substring(1);
            Method method = obj.getClass().getMethod(getter,new Class[]{});
            Object value = method.invoke(obj,new Object[]{});
            return value;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    /**
     * 设置属性值
     */
    public static void setFieldValueByName(Object object,String fieldName,Object value){
        try{
            Class c = object.getClass();
            Field field = c.getDeclaredField(fieldName);
            field.setAccessible(true); // 使私有字段可访问
            field.set(object, value);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
