#include <jni.h>
#include <string>
#include <year.hpp>
#include <iostream>
#include "include/tinyxml2.h"
#include "include/xml-dao.hpp"

// DisplayMonth JNI calls
extern "C"
JNIEXPORT jstring JNICALL
Java_com_kwrp_planner_1gui_DisplayMonth_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "1";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_testJNI(
        JNIEnv *env,
        jobject /* this */) {
    Year year("29/10/1991");
    std::string date = year.getDate();
    return env->NewStringUTF(date.c_str());
}


// DisplayDay JNI calls
extern "C"
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */) {
    Day *date = new Day();
    return env->NewStringUTF((date->getDate()).data());
}
// DisplayDay JNI calls
extern "C"
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */) {
    Day *date = new Day();
    return env->NewStringUTF((date->getDate()).data());
}


extern "C"
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetEvents(
        JNIEnv *env,
        jobject /* this */) {

    std::string e = "Hello World";
    return env->NewStringUTF(e.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetDay(
        JNIEnv *env,
        jobject /* this */) {
    Day *day = new Day();
    std::string e = day->getDate();
    return env->NewStringUTF(e.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniCreateEvent(
        JNIEnv *env, jobject t,
        jstring title, jstring description, jstring start, jstring duration, jstring dir) {

    const char *nativeTitle = env->GetStringUTFChars(title, 0);
    const char *nativeDescription = env->GetStringUTFChars(description, 0);
    const char *nativeStart = env->GetStringUTFChars(start, 0);
    const char *nativeDuration = env->GetStringUTFChars(duration, 0);
    const char *nativePath = env->GetStringUTFChars(dir, 0);

    bool addDate = createXml(nativePath);
    bool getDate = checkDate(nativePath, "5", "5", "2017");

    bool newEvent = addEvent(nativePath, "6", "5", "2017", nativeTitle,
                             nativeDescription, nativeStart, nativeDuration);

    //bool getDate2 = checkDate(nativePath, "6", "5", "2017");

    (env)->ReleaseStringUTFChars(title, nativeTitle);
    (env)->ReleaseStringUTFChars(description, nativeDescription);
    (env)->ReleaseStringUTFChars(start, nativeStart);
    (env)->ReleaseStringUTFChars(duration, nativeDuration);
    (env)->ReleaseStringUTFChars(dir, nativePath);

    std::string confirm = "";
    if (newEvent) {
        confirm = "Event Created!!";
    } else {
        confirm = "Event Creation Failed!!!";
    }

    return env->NewStringUTF(confirm.c_str());
}