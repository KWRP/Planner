#include "include/xml-dao.hpp"
#include <android/log.h>
#include <jni.h>

// DisplayMonth JNI calls
extern "C" {
JNIEXPORT jstring JNICALL
Java_com_kwrp_planner_1gui_DisplayMonth_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    return env->NewStringUTF(NULL);
}


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_testJNI(
        JNIEnv *env,
        jobject /* this */) {
    return env->NewStringUTF(NULL);
}


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */) {
    std::string date = getCurrentDate();
    return env->NewStringUTF(date.c_str());
}


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */) {
    std::string date = getCurrentDate();
    return env->NewStringUTF(date.c_str());
}


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetEvents(
        JNIEnv *env,
        jobject /* this */) {
    return env->NewStringUTF(NULL);
}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniCreateEvent(
        JNIEnv *env, jobject t,
        jstring title, jstring description, jstring start, jstring duration, jstring dir) {

    const char *nativeTitle = env->GetStringUTFChars(title, 0);
    const char *nativeDescription = env->GetStringUTFChars(description, 0);
    const char *nativeStart = env->GetStringUTFChars(start, 0);
    const char *nativeDuration = env->GetStringUTFChars(duration, 0);
    const char *nativePath = env->GetStringUTFChars(dir, 0);


    bool createFile = createXml(nativePath);
    bool createEvent = addEvent(nativePath, "10", "10", "2017", nativeTitle,
                                nativeDescription, nativeStart, nativeDuration);
    bool cd = checkDate(nativePath, "10", "10", "2017");

    (env)->ReleaseStringUTFChars(title, nativeTitle);
    (env)->ReleaseStringUTFChars(description, nativeDescription);
    (env)->ReleaseStringUTFChars(start, nativeStart);
    (env)->ReleaseStringUTFChars(duration, nativeDuration);
    (env)->ReleaseStringUTFChars(dir, nativePath);


    std::string confirm = "";
    if (cd) {
        confirm = "Event Created!!";
    } else {
        confirm = "Event Creation Failed!!!";
    }

    return env->NewStringUTF(confirm.c_str());
}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetDay(
        JNIEnv *env,
        jobject obj, jstring dir) {

    const char *nativePath = env->GetStringUTFChars(dir, 0);

    Day *day = new Day("10/10/2017", nativePath);

    __android_log_print(ANDROID_LOG_INFO, "TEST C++!!! GetDay", "%s", day->toString().c_str());

    (env)->ReleaseStringUTFChars(dir, nativePath);

    return env->NewStringUTF(day->toString().c_str());
}
}