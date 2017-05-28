#include "include/xml-dao.hpp"
#include <android/log.h>
#include <jni.h>

void throwJavaException(JNIEnv *env, const char *msg) {
    // You can put your own exception here
    jclass c = env->FindClass("java/lang/RuntimeException");

    if (NULL == c) {
        //B plan: null pointer ...
        c = env->FindClass("java/lang/NullPointerException");
    }

    env->ThrowNew(c, msg);
}


// DisplayMonth JNI calls
extern "C" {
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */) {
    std::string date = getCurrentDate();
    return env->NewStringUTF(date.c_str());
}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniCreateXml(
        JNIEnv *env,
        jobject jobject1, jstring dir) {
    std::string confirm = "";

    try {
        const char *nativePath = env->GetStringUTFChars(dir, 0);
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "createFile before");
        bool createFile = createXml(nativePath);
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "createFile before");

        (env)->ReleaseStringUTFChars(dir, nativePath);


        if (createFile) {
            confirm = "File Created!!";
        } else {
            confirm = "File Creation Failed!!!";
        }
    } catch (std::exception e) {
        throwJavaException(env, e.what());
    }

    return env->NewStringUTF(confirm.c_str());
}


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetCurrentDate(
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
        jstring title, jstring description, jstring start, jstring duration, jstring dir,
        jstring date) {

    std::string confirm = "";

    try {

        const char *nativeTitle = env->GetStringUTFChars(title, 0);
        const char *nativeDescription = env->GetStringUTFChars(description, 0);
        const char *nativeStart = env->GetStringUTFChars(start, 0);
        const char *nativeDuration = env->GetStringUTFChars(duration, 0);
        const char *nativePath = env->GetStringUTFChars(dir, 0);
        const char *nativeDate = env->GetStringUTFChars(date, 0);

        std::string selectedDate = nativeDate;
        std::string delimiter = "/";
        std::string day = selectedDate.substr(0, selectedDate.find(delimiter));
        std::string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        std::string month = rest.substr(0, selectedDate.find(delimiter));
        std::string year = rest.substr(selectedDate.find(delimiter) + 1);


        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "%s", day.c_str());
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "%s", month.c_str());
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "%s", year.c_str());

        bool createEvent = addEvent(nativePath, day.c_str(), month.c_str(), year.c_str(),
                                    nativeTitle,  nativeDescription, nativeStart, nativeDuration);


        bool check = checkDate(nativePath, day.c_str(), month.c_str(), year.c_str());

        (env)->ReleaseStringUTFChars(title, nativeTitle);
        (env)->ReleaseStringUTFChars(description, nativeDescription);
        (env)->ReleaseStringUTFChars(start, nativeStart);
        (env)->ReleaseStringUTFChars(duration, nativeDuration);
        (env)->ReleaseStringUTFChars(dir, nativePath);


        if (check) {
            confirm = "Event Created!!";
        } else {
            confirm = "Event Creation Failed!!!";
        }
    }
    catch (std::exception e) {
        throwJavaException(env, e.what());
    }
    return env->NewStringUTF(confirm.c_str());

}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetDay(
        JNIEnv *env, jobject obj, jstring dir, jstring date) {
    std::string dayString = "";
    try {

        const char *nativePath = env->GetStringUTFChars(dir, 0);
        const char *nativeDate = env->GetStringUTFChars(date, 0);

        //bool createFile = createXml(nativePath);
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "%s", nativeDate);
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "%s", nativePath);

        Day *day = new Day(nativeDate, nativePath);
        dayString = day->toString();

        free(day);
        (env)->ReleaseStringUTFChars(dir, nativePath);
        (env)->ReleaseStringUTFChars(date, nativeDate);

        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!! GetDay", "%s", dayString.c_str());
    }
    catch (std::exception e) {
        throwJavaException(env, e.what());
    }
    return env->NewStringUTF(dayString.c_str());
}
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DialogAction_jniCreateEvent(
        JNIEnv *env, jobject t,
        jstring title, jstring description, jstring start, jstring duration, jstring dir,
        jstring date) {

    std::string confirm = "";

    try {

        const char *nativeTitle = env->GetStringUTFChars(title, 0);
        const char *nativeDescription = env->GetStringUTFChars(description, 0);
        const char *nativeStart = env->GetStringUTFChars(start, 0);
        const char *nativeDuration = env->GetStringUTFChars(duration, 0);
        const char *nativePath = env->GetStringUTFChars(dir, 0);
        const char *nativeDate = env->GetStringUTFChars(date, 0);

        std::string selectedDate = nativeDate;
        std::string delimiter = "/";
        std::string day = selectedDate.substr(0, selectedDate.find(delimiter));
        std::string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        std::string month = rest.substr(0, selectedDate.find(delimiter));
        std::string year = rest.substr(selectedDate.find(delimiter) + 1);


        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "%s", day.c_str());
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "%s", month.c_str());
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "%s", year.c_str());

        bool createEvent = addEvent(nativePath, day.c_str(), month.c_str(), year.c_str(),
                                    nativeTitle,  nativeDescription, nativeStart, nativeDuration);


        bool check = checkDate(nativePath, day.c_str(), month.c_str(), year.c_str());

        (env)->ReleaseStringUTFChars(title, nativeTitle);
        (env)->ReleaseStringUTFChars(description, nativeDescription);
        (env)->ReleaseStringUTFChars(start, nativeStart);
        (env)->ReleaseStringUTFChars(duration, nativeDuration);
        (env)->ReleaseStringUTFChars(dir, nativePath);


        if (check) {
            confirm = "Event Created!!";
        } else {
            confirm = "Event Creation Failed!!!";
        }
    }
    catch (std::exception e) {
        throwJavaException(env, e.what());
    }
    return env->NewStringUTF(confirm.c_str());

}
}