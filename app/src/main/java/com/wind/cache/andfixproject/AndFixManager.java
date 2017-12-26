package com.wind.cache.andfixproject;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;


public class AndFixManager {

    private static final String TAG = App.class.getSimpleName();

    /**
     * assets目录下的dex文件是通过{@link RightMethodClass}文件经过 javac命令生成class文件
     * 再通过dex命令生成dex文件而得到的
     */
    private static String fixDexPath = "file:///android_asset/fix.dex";

    static {
        System.loadLibrary("native-lib");
    }

    public static native void andFixMethod(Method srcMethod, Method dstMethod);

    public static native void hotFixMethod(Method srcMethod, Method dstMethod);

    public static native int getArtMethoLength(Method method1, Method method2);

    public static void startAndFix(Context context) {
        try {
            Class<?> clazz = loadRightMethodClass(context);
            Log.e(TAG,"loaded clazz is "+clazz);
            Method srcMethod = clazz.getMethod("fixGet", int.class, int.class);
            Log.e(TAG,"srcMethod = "+srcMethod);
            Method dstMethod = WrongMethodClass.class.getMethod("get", int.class, int.class);
            andFixMethod(srcMethod, dstMethod);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void startHotFix(Context context) {
        try {
            Method method1 = NativeArtMethodCalculator.class.getMethod("method1");
            Method method2 = NativeArtMethodCalculator.class.getMethod("method2");
            //计算native中ArtMethod的size
            getArtMethoLength(method1, method2);

            Class<?> clazz = loadRightMethodClass(context);
            Log.e(TAG,"loaded clazz is "+clazz);
            Method srcMethod = clazz.getMethod("fixGet", int.class, int.class);
            Log.e(TAG,"srcMethod = "+srcMethod);
            Method dstMethod = WrongMethodClass.class.getMethod("get", int.class, int.class);
            hotFixMethod(srcMethod, dstMethod);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    //通过Java方法来操作内存，将ArtMethod的Native指针进行替换
    public static void startFixByJava(Context context) {
        try {
            Method method1 = NativeArtMethodCalculator.class.getMethod("method1");
            Method method2 = NativeArtMethodCalculator.class.getMethod("method2");
            long method1Address = MemoryWrapper.getMethodAddress(method1);
            long method2Address = MemoryWrapper.getMethodAddress(method2);
            long sizeOfArtMethod = method2Address - method1Address;  //等同于调用JNI方法：sizeOfArtMethod = getArtMethoLength(method1, method2);

            Class<?> clazz = loadRightMethodClass(context);
            Method srcMethod = clazz.getMethod("fixGet", int.class, int.class);
            Log.e(TAG, "startFixByJava ---srcMethod= "+srcMethod);

            Method dstMethod = WrongMethodClass.class.getMethod("get", int.class, int.class);
            long dstAddress = MemoryWrapper.getMethodAddress(dstMethod);
            long srcAddress = MemoryWrapper.getMethodAddress(srcMethod);
            Log.e(TAG,"startFixByJava ---dstAddress= "+dstAddress+" srcAddress= "+srcAddress);
            Log.e(TAG,"startFixByJava ---method1Address= "+method1Address+" method2Address= "+method2Address);

            MemoryWrapper.memcpy(dstAddress, srcAddress, sizeOfArtMethod); //等同于调用JNI方法： memcpy(dstAddress, srcAddress, art_method_length);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    //从assets目录中加载dex文件中的正确方法的类
    private static Class<?> loadRightMethodClass(Context context) {
        DexClassLoader rightClassLoader = new DexClassLoader(fixDexPath, context.getDir("dex", Context.MODE_PRIVATE).getAbsolutePath(),
                null,
                context.getClassLoader());
        Class<?> clazz = null;
        try {
            clazz = rightClassLoader.loadClass("com.wind.cache.andfixproject.RightMethodClass");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }


}
