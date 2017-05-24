#include "include/xml-dao.hpp"
#include "include/year.hpp"
#include "include/day.hpp"
#include <jni.h>

// DisplayMonth JNI calls
extern "C" {
JNIEXPORT jstring JNICALL
Java_com_kwrp_planner_1gui_DisplayMonth_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "1";
    return env->NewStringUTF(hello.c_str());
}


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_testJNI(
        JNIEnv *env,
        jobject /* this */) {
    Year year("29/10/1991");
    std::string date = year.getDate();
    return env->NewStringUTF(date.c_str());
}


// DisplayDay JNI calls

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */) {
    Day *date = new Day();
    return env->NewStringUTF((date->getDate()).data());
}
// DisplayDay JNI calls

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */) {
    Day *date = new Day();
    return env->NewStringUTF((date->getDate()).data());
}



JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetEvents(
        JNIEnv *env,
        jobject /* this */) {

    std::string e = "Hello World";
    return env->NewStringUTF(e.c_str());
}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetDay(
        JNIEnv *env,
        jobject /* this */) {
    Day *day = new Day();
    std::string e = day->getDate();
    return env->NewStringUTF(e.c_str());
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
    //bool newDate2 = createDate(nativePath, "3", "5", "2017");
    bool newDate = createDate(nativePath, "1", "5", "2017");
    bool newDate1 = createDate(nativePath, "2", "8", "2017");
    bool newDate3 = createDate(nativePath, "3", "5", "2016");
    bool checkDateCreated = checkDate(nativePath, "1", "5", "2017");
    // bool createEvent = addEvent(nativePath, "6", "6", "2017", nativeTitle,
    // nativeDescription, nativeStart, nativeDuration);


    (env)->ReleaseStringUTFChars(title, nativeTitle);
    (env)->ReleaseStringUTFChars(description, nativeDescription);
    (env)->ReleaseStringUTFChars(start, nativeStart);
    (env)->ReleaseStringUTFChars(duration, nativeDuration);
    (env)->ReleaseStringUTFChars(dir, nativePath);

    std::string confirm = "";
    if (checkDateCreated) {
        confirm = "Event Created!!";
    } else {
        confirm = "Event Creation Failed!!!";
    }

    return env->NewStringUTF(confirm.c_str());
}
}