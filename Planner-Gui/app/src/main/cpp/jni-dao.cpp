#include <android/log.h>
#include "include/jni-dao.h"
#include "include/helpers.hpp"
#include "sqlite-dao.hpp"

using namespace std;
extern "C" {

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */) {
    string date = getCurrentDate();
    return env->NewStringUTF(date.c_str());
}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DialogAction_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */) {
    string date = getCurrentDate();
    return env->NewStringUTF(date.c_str());
}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniCreateDb(
        JNIEnv *env,
        jobject /* this */, jstring dir) {
    string confirm = "";

    try {
        const char *nativePath = env->GetStringUTFChars(dir, 0);

        bool createDb = createTableQuery(nativePath);

        (env)->ReleaseStringUTFChars(dir, nativePath);


        if (createDb) {
            confirm = "File Created!!";
        } else {
            confirm = "File Creation Failed!!!";
        }
    } catch (exception e) {
        throwJavaException(env, e.what());
    }

    return env->NewStringUTF(confirm.c_str());

}
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniCreateDb(
        JNIEnv *env,
        jobject /* this */, jstring dir) {
    string confirm = "";

    try {
        const char *nativePath = env->GetStringUTFChars(dir, 0);

        bool createDb = createTableQuery(nativePath);

        (env)->ReleaseStringUTFChars(dir, nativePath);

        if (createDb) {
            confirm = "File Created!!";
        } else {
            confirm = "File Creation Failed!!!";
        }
    } catch (exception e) {
        throwJavaException(env, e.what());
    }

    return env->NewStringUTF(confirm.c_str());

}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniCreateDbEvent(
        JNIEnv *env, jobject /* this */, jstring title, jstring description, jstring start,
        jstring finish, jstring startDate, jstring endDate, jstring repeat, jstring filepath) {

    string confirm = "";
    try {

        const char *nativeTitle = env->GetStringUTFChars(title, 0);
        const char *nativeDescription = env->GetStringUTFChars(description, 0);
        const char *nativeStart = env->GetStringUTFChars(start, 0);
        const char *nativeFinish = env->GetStringUTFChars(finish, 0);
        const char *nativepath = env->GetStringUTFChars(filepath, 0);
        const char *nativeDate = env->GetStringUTFChars(startDate, 0);
        const char *nativeEndDate = env->GetStringUTFChars(endDate, 0);
        const char *nativeRepeat = env->GetStringUTFChars(repeat, 0);

        string selectedDate = nativeDate;
        string delimiter = "/";
        string day = selectedDate.substr(0, selectedDate.find(delimiter));
        string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string month = rest.substr(0, selectedDate.find(delimiter));
        string year = rest.substr(selectedDate.find(delimiter) + 1);

        selectedDate = nativeEndDate;
        string endDay = selectedDate.substr(0, selectedDate.find(delimiter));
        string endRest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string endMonth = endRest.substr(0, selectedDate.find(delimiter));
        string endYear = endRest.substr(selectedDate.find(delimiter) + 1);

        bool addEventToSql = insertToDb(day.c_str(), month.c_str(), year.c_str(),
                                        nativeTitle, nativeDescription, nativeStart, nativeFinish,
                                        endDay.c_str(), endMonth.c_str(), endYear.c_str(),
                                        atoi(nativeRepeat), nativepath);
        displayDb(nativepath);

        (env)->ReleaseStringUTFChars(title, nativeTitle);
        (env)->ReleaseStringUTFChars(description, nativeDescription);
        (env)->ReleaseStringUTFChars(start, nativeStart);
        (env)->ReleaseStringUTFChars(finish, nativeFinish);
        (env)->ReleaseStringUTFChars(filepath, nativepath);
        (env)->ReleaseStringUTFChars(repeat, nativeRepeat);
        (env)->ReleaseStringUTFChars(startDate, nativeDate);
        (env)->ReleaseStringUTFChars(endDate, nativeEndDate);
        if (addEventToSql) {
            confirm = "Event Created!!";
        } else {
            confirm = "Event Deleted!!!";
        }
    }
    catch (exception e) {
        throwJavaException(env, e.what());
    }
    return env->NewStringUTF(confirm.c_str());

}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetDayDb(
        JNIEnv *env, jobject /* this */, jstring dir, jstring date) {

    string dayString = "";

    try {

        const char *nativePath = env->GetStringUTFChars(dir, 0);
        const char *nativeDate = env->GetStringUTFChars(date, 0);
        string selectedDate = nativeDate;
        string delimiter = "/";
        string days = selectedDate.substr(0, selectedDate.find(delimiter));
        string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string month = rest.substr(0, selectedDate.find(delimiter));
        string year = rest.substr(selectedDate.find(delimiter) + 1);

        vector<std::string> result = selectFromDB(days.c_str(), month.c_str(), year.c_str(),
                                                  nativePath);
        (env)->ReleaseStringUTFChars(dir, nativePath);
        (env)->ReleaseStringUTFChars(date, nativeDate);

        for (int i = 0; i < result.size(); i++) {
            dayString.append(result[i]).append(";");
        }


    } catch (exception e) {
        throwJavaException(env, e.what());
    }
    return env->NewStringUTF(dayString.c_str());
}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */) {
    string date = getCurrentDate();
    return env->NewStringUTF(date.c_str());
}


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniUpdateEventDb(
        JNIEnv *env, jobject /* this */, jstring title, jstring description, jstring start,
        jstring finish, jstring startDate, jstring endDate, jstring repeat, jstring eventID,
        jstring filepath) {

    string confirm = "";
    try {

        const char *nativeTitle = env->GetStringUTFChars(title, 0);
        const char *nativeDescription = env->GetStringUTFChars(description, 0);
        const char *nativeStart = env->GetStringUTFChars(start, 0);
        const char *nativeFinish = env->GetStringUTFChars(finish, 0);
        const char *nativepath = env->GetStringUTFChars(filepath, 0);
        const char *nativeDate = env->GetStringUTFChars(startDate, 0);
        const char *nativeEndDate = env->GetStringUTFChars(endDate, 0);
        const char *nativeRepeat = env->GetStringUTFChars(repeat, 0);
        const char *nativeID = env->GetStringUTFChars(eventID, 0);

        string selectedDate = nativeDate;
        string delimiter = "/";
        string day = selectedDate.substr(0, selectedDate.find(delimiter));
        string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string month = rest.substr(0, selectedDate.find(delimiter));
        string year = rest.substr(selectedDate.find(delimiter) + 1);

        selectedDate = nativeEndDate;
        string endDay = selectedDate.substr(0, selectedDate.find(delimiter));
        string endRest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string endMonth = endRest.substr(0, selectedDate.find(delimiter));
        string endYear = endRest.substr(selectedDate.find(delimiter) + 1);

        __android_log_print(ANDROID_LOG_INFO, "TEST Print DATABASE!!!", "%s", "Print failed");


        bool updateEvent = updateToDb(day.c_str(), month.c_str(), year.c_str(),
                                      nativeTitle, nativeDescription, nativeStart, nativeFinish,
                                      endDay.c_str(), endMonth.c_str(), endYear.c_str(),
                                      atoi(nativeID),
                                      atoi(nativeRepeat), nativepath);

        __android_log_print(ANDROID_LOG_INFO, "TEST Print DATABASE!!!", "%s", "Print failed");

        (env)->ReleaseStringUTFChars(title, nativeTitle);
        (env)->ReleaseStringUTFChars(description, nativeDescription);
        (env)->ReleaseStringUTFChars(start, nativeStart);
        (env)->ReleaseStringUTFChars(finish, nativeFinish);
        (env)->ReleaseStringUTFChars(filepath, nativepath);
        (env)->ReleaseStringUTFChars(repeat, nativeRepeat);
        (env)->ReleaseStringUTFChars(startDate, nativeDate);
        (env)->ReleaseStringUTFChars(endDate, nativeEndDate);
        (env)->ReleaseStringUTFChars(eventID, nativeID);

        if (updateEvent) {
            confirm = "Event Update!!";
        } else {
            confirm = "Event Update failed!!!";
        }
    }
    catch (exception e) {
        throwJavaException(env, e.what());
    }
    return env->NewStringUTF(confirm.c_str());

}


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniRemoveEventDb(
        JNIEnv *env, jobject /* this */, jstring eventId, jstring filepath) {
    string confirm = "";
    try {

        const char *nativePath = env->GetStringUTFChars(filepath, 0);
        const char *nativeEventId = env->GetStringUTFChars(eventId, 0);

        bool deleteEvent = deleteFromDb(atoi(nativeEventId), nativePath);

        (env)->ReleaseStringUTFChars(filepath, nativePath);
        (env)->ReleaseStringUTFChars(eventId, nativeEventId);

        if (deleteEvent) {
            confirm = "Event Deleted!!";
        } else {
            confirm = "Event Deletion Failed!!!";
        }
    }
    catch (exception e) {
        throwJavaException(env, e.what());
    }
    return env->NewStringUTF(confirm.c_str());
}
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniGetEventsDb(
        JNIEnv *env, jobject /* this */, jstring month, jstring year, jstring dir) {

    string getMonth = "";
    try {
        const char *nativePath = env->GetStringUTFChars(dir, 0);
        const char *nativeMonth = env->GetStringUTFChars(month, 0);
        const char *nativeYear = env->GetStringUTFChars(year, 0);

        getMonth = selectMonth(nativeMonth, nativeYear, nativePath);

        (env)->ReleaseStringUTFChars(dir, nativePath);
        (env)->ReleaseStringUTFChars(month, nativeMonth);
        (env)->ReleaseStringUTFChars(year, nativeYear);

    } catch (exception e) {
        throwJavaException(env, e.what());
    }

    return env->NewStringUTF(getMonth.c_str());
}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniCreateDbEvent(
        JNIEnv *env, jobject /* this */, jstring title, jstring description, jstring start,
        jstring finish, jstring startDate, jstring endDate, jstring repeat, jstring filepath) {

    string confirm = "";
    try {

        const char *nativeTitle = env->GetStringUTFChars(title, 0);
        const char *nativeDescription = env->GetStringUTFChars(description, 0);
        const char *nativeStart = env->GetStringUTFChars(start, 0);
        const char *nativeFinish = env->GetStringUTFChars(finish, 0);
        const char *nativepath = env->GetStringUTFChars(filepath, 0);
        const char *nativeDate = env->GetStringUTFChars(startDate, 0);
        const char *nativeEndDate = env->GetStringUTFChars(endDate, 0);
        const char *nativeRepeat = env->GetStringUTFChars(repeat, 0);

        string selectedDate = nativeDate;
        string delimiter = "/";
        string day = selectedDate.substr(0, selectedDate.find(delimiter));
        string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string month = rest.substr(0, selectedDate.find(delimiter));
        string year = rest.substr(selectedDate.find(delimiter) + 1);

        selectedDate = nativeEndDate;
        string endDay = selectedDate.substr(0, selectedDate.find(delimiter));
        string endRest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string endMonth = endRest.substr(0, selectedDate.find(delimiter));
        string endYear = endRest.substr(selectedDate.find(delimiter) + 1);

        bool addEventToSql = insertToDb(day.c_str(), month.c_str(), year.c_str(),
                                        nativeTitle, nativeDescription, nativeStart, nativeFinish,
                                        endDay.c_str(), endMonth.c_str(), endYear.c_str(),
                                        atoi(nativeRepeat), nativepath);

        (env)->ReleaseStringUTFChars(title, nativeTitle);
        (env)->ReleaseStringUTFChars(description, nativeDescription);
        (env)->ReleaseStringUTFChars(start, nativeStart);
        (env)->ReleaseStringUTFChars(finish, nativeFinish);
        (env)->ReleaseStringUTFChars(filepath, nativepath);
        (env)->ReleaseStringUTFChars(repeat, nativeRepeat);
        (env)->ReleaseStringUTFChars(startDate, nativeDate);
        (env)->ReleaseStringUTFChars(endDate, nativeEndDate);
        if (addEventToSql) {
            confirm = "Event Created!!";
        } else {
            confirm = "Event Deleted!!!";
        }
    }
    catch (exception e) {
        throwJavaException(env, e.what());
    }
    return env->NewStringUTF(confirm.c_str());

}
}