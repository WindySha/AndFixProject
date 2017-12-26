#include <jni.h>
#include <string>
#include <android/log.h>
#include "art_7_0.h"
#include <stdint.h>

#define  LOG_TAG    "AndFix"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

size_t art_method_length = 0;

extern "C"
JNIEXPORT void JNICALL
Java_com_wind_cache_andfixproject_AndFixManager_andFixMethod(JNIEnv *env, jobject instance,
                                                         jobject srcMethod, jobject dstMethod) {

    LOGD("start fix art_method!!!!");

    art::mirror::ArtMethod* meth = (art::mirror::ArtMethod*) env->FromReflectedMethod(srcMethod);
    art::mirror::ArtMethod* target = (art::mirror::ArtMethod*) env->FromReflectedMethod(dstMethod);

    target->declaring_class_ = meth->declaring_class_;
    LOGD("start fix art_method!!!  %d , %d ", target->access_flags_, meth->access_flags_);
    target->access_flags_ = meth->access_flags_;
//    target->access_flags_ = meth->access_flags_  | 0x0001;
    target->dex_code_item_offset_ = meth->dex_code_item_offset_;
    target->dex_method_index_ = meth->dex_method_index_;
    target->method_index_ = meth->method_index_;
    target->hotness_count_ = meth->hotness_count_;
    target->ptr_sized_fields_.dex_cache_resolved_types_ = meth->ptr_sized_fields_.dex_cache_resolved_types_;
    target->ptr_sized_fields_.dex_cache_resolved_methods_ = meth->ptr_sized_fields_.dex_cache_resolved_methods_;
    target->ptr_sized_fields_.entry_point_from_jni_ = meth->ptr_sized_fields_.entry_point_from_jni_;
    target->ptr_sized_fields_.entry_point_from_quick_compiled_code_ = meth->ptr_sized_fields_.entry_point_from_quick_compiled_code_;

    LOGD("finish replace_7_0: %d , %d",
         target->ptr_sized_fields_.entry_point_from_quick_compiled_code_,
         meth->ptr_sized_fields_.entry_point_from_quick_compiled_code_);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wind_cache_andfixproject_AndFixManager_hotFixMethod(JNIEnv *env, jobject instance,
                                                             jobject srcMethod, jobject dstMethod) {
    LOGD("start hotFixMethod  :  %d",
         art_method_length);
    jmethodID meth = env->FromReflectedMethod(srcMethod);
    jmethodID target = env->FromReflectedMethod(dstMethod);
    memcpy(target, meth, art_method_length);
    LOGD("end hotFixMethod  :  %d, %d , %d , %d",
         art_method_length, sizeof(meth), sizeof(target), sizeof(jmethodID));
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_wind_cache_andfixproject_AndFixManager_getArtMethoLength(JNIEnv *env, jobject instance,
                                                                  jobject method1,
                                                                  jobject method2) {

    if (art_method_length != 0) {
        return art_method_length;
    }
    size_t method1Ptr =  (size_t)env->FromReflectedMethod(method1);
    size_t method2Ptr =  (size_t)env->FromReflectedMethod(method2);
    art_method_length = method2Ptr - method1Ptr;
    LOGD("initArtMethoLength end:  %d , %d, %d",
         method1Ptr, method2Ptr, art_method_length);
    return art_method_length;
}