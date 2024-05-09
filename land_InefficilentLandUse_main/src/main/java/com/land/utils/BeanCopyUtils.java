package com.land.utils;
import org.springframework.beans.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
public class BeanCopyUtils extends BeanUtils {

    public static void copyNotNullProperties(Object source, Object target, String[] ignoreProperties)
            throws BeansException
    {
        copyNotNullProperties(source, target, null, ignoreProperties);
    }

    public static void copyNotNullProperties(Object source, Object target, Class<?> editable)
            throws BeansException
    {
        copyNotNullProperties(source, target, editable, null);
    }

    public static void copyNotNullProperties(Object source, Object target)
            throws BeansException
    {
        copyNotNullProperties(source, target, null, null);
    }

    private static void copyNotNullProperties(Object source, Object target, Class<?> editable, String[] ignoreProperties)
            throws BeansException
    {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;

        for (PropertyDescriptor targetPd : targetPds)
            if ((targetPd.getWriteMethod() != null) && ((ignoreProperties == null) || (!ignoreList.contains(targetPd.getName())))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if ((sourcePd != null) && (sourcePd.getReadMethod() != null))
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source, new Object[0]);
                        if ((value != null) || (readMethod.getReturnType().getName().equals("java.lang.String"))) {
                            boolean isEmpty = false;
                            if ((value instanceof Set)) {
                                Set s = (Set)value;
                                if ((s == null) || (s.isEmpty()))
                                    isEmpty = true;
                            }
                            else if ((value instanceof Map)) {
                                Map m = (Map)value;
                                if ((m == null) || (m.isEmpty()))
                                    isEmpty = true;
                            }
                            else if ((value instanceof List)) {
                                List l = (List)value;
                                if ((l == null) || (l.size() < 1))
                                    isEmpty = true;
                            }
                            else if ((value instanceof Collection)) {
                                Collection c = (Collection)value;
                                if ((c == null) || (c.size() < 1)) {
                                    isEmpty = true;
                                }
                            }
                            if (!isEmpty) {
                                Method writeMethod = targetPd.getWriteMethod();
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, new Object[] { value });
                            }
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
            }
    }

    public static Object getNestedProperty(Object bean, String nestedProperty)
    {
        try
        {
            return PropertyUtils.getNestedProperty(bean, nestedProperty);
        }
        catch (Exception e) {
            e.printStackTrace();
        }return null;
    }

    public static void setPropertyValue(Object bean, String property, Object value)
    {
        try
        {
            PropertyUtils.setNestedProperty(bean, property, value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyPropertyValue(Object source, Object target)
    {
        try
        {
            Method[] sourceMethods = source.getClass().getMethods();
            Method[] targetMethods = target.getClass().getMethods();
            Method sourceMethod = null;
            String sourceMethodName = "";
            String targetMehodName = "";
            String str = "";
            String targetfield = "";
            for (int i = 0; i < sourceMethods.length; i++) {
                sourceMethod = sourceMethods[i];
                sourceMethodName = sourceMethod.getName();
                if ((sourceMethodName.startsWith("get")) && (!sourceMethodName.equals("getClass")))
                    for (int j = 0; j < targetMethods.length; j++) {
                        targetMehodName = targetMethods[j].getName();
                        if ((targetMehodName.startsWith("get")) &&
                                (sourceMethodName.equals(targetMehodName))) {
                            str = targetMehodName.substring(3, targetMehodName.length());
                            targetfield = str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
                            setPropertyValue(target, targetfield, sourceMethod.invoke(source, new Object[0]));
                        }
                    }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void setPropertyValue(Object bean, String property, Object value, boolean ignoreCase)
    {
        if (ignoreCase)
            try {
                PropertyDescriptor[] pds = getPropertyDescriptors(bean.getClass());
                for (PropertyDescriptor pd : pds)
                    if (pd.getName().equalsIgnoreCase(property))
                        PropertyUtils.setNestedProperty(bean, pd.getName(), value);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        else
            setPropertyValue(bean, property, value);
    }
}

