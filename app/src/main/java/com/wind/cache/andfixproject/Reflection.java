package com.wind.cache.andfixproject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Reflection {

    public static Object call(Class<?> clazz, String className, String methodName, Object receiver,
                              Class[] types, Object[] params)  {
        try {
            if (clazz == null) clazz = Class.forName(className);
            Method method = clazz.getDeclaredMethod(methodName, types);
            method.setAccessible(true);
            return method.invoke(receiver, params);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static Object get(Class<?> clazz, String className, String fieldName, Object receiver) {
        try {
            if (clazz == null) clazz = Class.forName(className);
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(receiver);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
