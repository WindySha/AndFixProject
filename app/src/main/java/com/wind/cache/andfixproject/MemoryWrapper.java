package com.wind.cache.andfixproject;
import android.util.Log;

import java.lang.reflect.Method;

public class MemoryWrapper {

    private static final String UNSAFE_CLASS = "sun.misc.Unsafe";
    private static Object THE_UNSAFE;
    private static boolean is64Bit;

    static {
        THE_UNSAFE = Reflection.get(null, UNSAFE_CLASS, "THE_ONE", null);
        Object runtime = Reflection.call(null, "dalvik.system.VMRuntime", "getRuntime", null, null, null);
        is64Bit = (Boolean) Reflection.call(null, "dalvik.system.VMRuntime", "is64Bit", runtime, null, null);
    }

    // libcode.io.Memory#peekByte
    private static byte peekByte(long address) {
        return (Byte) Reflection.call(null, "libcore.io.Memory", "peekByte", null, new Class[]{long.class}, new Object[]{address});
    }

    static void pokeByte(long address, byte value) {
        Reflection.call(null, "libcore.io.Memory", "pokeByte", null, new Class[]{long.class, byte.class}, new Object[]{address, value});
    }

    public static void memcpy(long dst, long src, long length) {
        for (long i = 0; i < length; i++) {
            pokeByte(dst, peekByte(src));
            dst++;
            src++;
        }
    }

    public static long getMethodAddress(Method method) {
        Object mirrorMethod = Reflection.get(Method.class.getSuperclass(), null, "artMethod", method);
        if (mirrorMethod.getClass().equals(Long.class)) {
            return (Long) mirrorMethod;
        }
        return getObjectAddress(mirrorMethod);
    }

    public static long getObjectAddress(Object o) {
        Object[] objects = {o};
        Integer baseOffset = (Integer) Reflection.call(null, UNSAFE_CLASS,
                "arrayBaseOffset", THE_UNSAFE, new Class[]{Class.class}, new Object[]{Object[].class});
        return ((Number) Reflection.call(null, UNSAFE_CLASS, is64Bit ? "getLong" : "getInt", THE_UNSAFE,
                new Class[]{Object.class, long.class}, new Object[]{objects, baseOffset.longValue()})).longValue();
    }
}
