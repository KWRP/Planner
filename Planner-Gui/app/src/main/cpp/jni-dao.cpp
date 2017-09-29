#include "include/jni-dao.h"
#include "include/xml-dao.hpp"
#include "sqlite-dao.hpp"

using namespace std;
extern "C" {
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

        bool byDay = false;
        string s = nativeRepeat;
        if(s.length() > 1){
            if(s[1] == 0) byDay=true;
        }

        bool addEventToSql = insertToDb(day.c_str(),month.c_str(),year.c_str(),
                                        nativeTitle, nativeDescription, nativeStart, nativeFinish,
                                        endDay.c_str(),endMonth.c_str(),endYear.c_str(),
                                        atoi(nativeRepeat),byDay, nativepath);

        std::vector<std::vector<const unsigned char *>> select = selectFromDB(day.c_str(),
                                                                              month.c_str(),
                                                                              year.c_str(),
                                                                              nativepath);
        //if (!select) __android_log_print(ANDROID_LOG_INFO, "TEST Select from DATABASE!!!", "%s", "Select failed");
//        if (!displayDb(nativepath))  __android_log_print(ANDROID_LOG_INFO, "TEST Print DATABASE!!!", "%s", "Print failed");

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
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniRemoveDbEvent(
        JNIEnv *env, jobject /* this */, jstring filepath, jstring date, jstring eventId) {

    string confirm = "";
    try {

        const char *nativePath = env->GetStringUTFChars(filepath, 0);
        const char *nativeDate = env->GetStringUTFChars(date, 0);
        const char *nativeEventId = env->GetStringUTFChars(eventId, 0);

        string selectedDate = nativeDate;
        string delimiter = "/";
        string day = selectedDate.substr(0, selectedDate.find(delimiter));
        string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string month = rest.substr(0, selectedDate.find(delimiter));
        string year = rest.substr(selectedDate.find(delimiter) + 1);

        bool deleteEvent = removeEvent(nativePath, day.c_str(), month.c_str(), year.c_str(),
                                       nativeEventId);

        (env)->ReleaseStringUTFChars(filepath, nativePath);
        (env)->ReleaseStringUTFChars(date, nativeDate);
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

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetDayDb(
        JNIEnv *env, jobject /* this */, jstring dir, jstring date) {

    string dayString = "";

    try {

        const char *nativePath = env->GetStringUTFChars(dir, 0);
        const char *nativeDate = env->GetStringUTFChars(date, 0);
        //displayDb(nativePath);
        string selectedDate = nativeDate;
        string delimiter = "/";
        string days = selectedDate.substr(0, selectedDate.find(delimiter));
        string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string month = rest.substr(0, selectedDate.find(delimiter));
        string year = rest.substr(selectedDate.find(delimiter) + 1);

        std::vector<std::vector<const unsigned char *>> select = selectFromDB(days.c_str(),
                                                                              month.c_str(),
                                                                              year.c_str(),
                                                                              nativePath);

        Day *day = new Day(nativeDate, nativePath);
        dayString = day->toString();

        free(day);
        (env)->ReleaseStringUTFChars(dir, nativePath);
        (env)->ReleaseStringUTFChars(date, nativeDate);

    }
    catch (exception e) {
        throwJavaException(env, e.what());
    }
    return env->NewStringUTF(dayString.c_str());
}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DialogAction_jniCreateEventDb(
        JNIEnv *env, jobject /* this */,
        jstring title, jstring description, jstring start, jstring duration, jstring dir,
        jstring date) {

    string confirm = "";

    try {

        const char *nativeTitle = env->GetStringUTFChars(title, 0);
        const char *nativeDescription = env->GetStringUTFChars(description, 0);
        const char *nativeStart = env->GetStringUTFChars(start, 0);
        const char *nativeDuration = env->GetStringUTFChars(duration, 0);
        const char *nativePath = env->GetStringUTFChars(dir, 0);
        const char *nativeDate = env->GetStringUTFChars(date, 0);

        string selectedDate = nativeDate;
        string delimiter = "/";
        string day = selectedDate.substr(0, selectedDate.find(delimiter));
        string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string month = rest.substr(0, selectedDate.find(delimiter));
        string year = rest.substr(selectedDate.find(delimiter) + 1);

        (env)->ReleaseStringUTFChars(title, nativeTitle);
        (env)->ReleaseStringUTFChars(description, nativeDescription);
        (env)->ReleaseStringUTFChars(start, nativeStart);
        (env)->ReleaseStringUTFChars(duration, nativeDuration);
        (env)->ReleaseStringUTFChars(dir, nativePath);

        if (false) {
            confirm = "Event Created!!";
        } else {
            confirm = "Event Creation Failed!!!";
        }
    }
    catch (exception e) {
        throwJavaException(env, e.what());
    }
    return env->NewStringUTF(confirm.c_str());
}

// DisplayMonth JNI calls

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */) {
    string date = getCurrentDate();
    return env->NewStringUTF(date.c_str());
}

// XML Stuff


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniCreateXml(
        JNIEnv *env,
        jobject /* this */, jstring dir) {
    string confirm = "";

    try {
        const char *nativePath = env->GetStringUTFChars(dir, 0);

        bool createFile = createXml(nativePath);

        (env)->ReleaseStringUTFChars(dir, nativePath);


        if (createFile) {
            confirm = "File Created!!";
        } else {
            confirm = "File Creation Failed!!!";
        }
    } catch (exception e) {
        throwJavaException(env, e.what());
    }

    return env->NewStringUTF(confirm.c_str());
}


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */) {
    string date = getCurrentDate();
    return env->NewStringUTF(date.c_str());
}


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniCreateEvent(
        JNIEnv *env, jobject /* this */, jstring title, jstring description, jstring start,
        jstring duration, jstring filepath, jstring date) {

    string confirm = "";

    try {

        const char *nativeTitle = env->GetStringUTFChars(title, 0);
        const char *nativeDescription = env->GetStringUTFChars(description, 0);
        const char *nativeStart = env->GetStringUTFChars(start, 0);
        const char *nativeDuration = env->GetStringUTFChars(duration, 0);
        const char *nativePath = env->GetStringUTFChars(filepath, 0);
        const char *nativeDate = env->GetStringUTFChars(date, 0);

        string selectedDate = nativeDate;
        string delimiter = "/";
        string day = selectedDate.substr(0, selectedDate.find(delimiter));
        string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string month = rest.substr(0, selectedDate.find(delimiter));
        string year = rest.substr(selectedDate.find(delimiter) + 1);

        bool createEvent = addEvent(nativePath, day.c_str(), month.c_str(), year.c_str(),
                                    nativeTitle, nativeDescription, nativeStart, nativeDuration);
        bool check;
        check = createEvent && checkDate(nativePath, day.c_str(), month.c_str(), year.c_str());

        (env)->ReleaseStringUTFChars(title, nativeTitle);
        (env)->ReleaseStringUTFChars(description, nativeDescription);
        (env)->ReleaseStringUTFChars(start, nativeStart);
        (env)->ReleaseStringUTFChars(duration, nativeDuration);
        (env)->ReleaseStringUTFChars(filepath, nativePath);

        if (check) {
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

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniRemoveEvent(
        JNIEnv *env, jobject /* this */, jstring filepath, jstring date, jstring eventId) {

    string confirm = "";
    try {

        const char *nativePath = env->GetStringUTFChars(filepath, 0);
        const char *nativeDate = env->GetStringUTFChars(date, 0);
        const char *nativeEventId = env->GetStringUTFChars(eventId, 0);

        string selectedDate = nativeDate;
        string delimiter = "/";
        string day = selectedDate.substr(0, selectedDate.find(delimiter));
        string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string month = rest.substr(0, selectedDate.find(delimiter));
        string year = rest.substr(selectedDate.find(delimiter) + 1);


        bool deleteEvent = removeEvent(nativePath, day.c_str(), month.c_str(), year.c_str(),
                                       nativeEventId);


        (env)->ReleaseStringUTFChars(filepath, nativePath);
        (env)->ReleaseStringUTFChars(date, nativeDate);
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

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetDay(
        JNIEnv *env, jobject /* this */, jstring dir, jstring date) {

    string dayString = "";

    try {

        const char *nativePath = env->GetStringUTFChars(dir, 0);
        const char *nativeDate = env->GetStringUTFChars(date, 0);

        Day *day = new Day(nativeDate, nativePath);
        dayString = day->toString();

        free(day);
        (env)->ReleaseStringUTFChars(dir, nativePath);
        (env)->ReleaseStringUTFChars(date, nativeDate);

    }
    catch (exception e) {
        throwJavaException(env, e.what());
    }
    return env->NewStringUTF(dayString.c_str());
}

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DialogAction_jniCreateEvent(
        JNIEnv *env, jobject /* this */,
        jstring title, jstring description, jstring start, jstring duration, jstring dir,
        jstring date) {

    string confirm = "";

    try {

        const char *nativeTitle = env->GetStringUTFChars(title, 0);
        const char *nativeDescription = env->GetStringUTFChars(description, 0);
        const char *nativeStart = env->GetStringUTFChars(start, 0);
        const char *nativeDuration = env->GetStringUTFChars(duration, 0);
        const char *nativePath = env->GetStringUTFChars(dir, 0);
        const char *nativeDate = env->GetStringUTFChars(date, 0);

        string selectedDate = nativeDate;
        string delimiter = "/";
        string day = selectedDate.substr(0, selectedDate.find(delimiter));
        string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
        string month = rest.substr(0, selectedDate.find(delimiter));
        string year = rest.substr(selectedDate.find(delimiter) + 1);


        bool createEvent = addEvent(nativePath, day.c_str(), month.c_str(), year.c_str(),
                                    nativeTitle, nativeDescription, nativeStart, nativeDuration);

        bool check;
        check = createEvent && checkDate(nativePath, day.c_str(), month.c_str(), year.c_str());

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
    catch (exception e) {
        throwJavaException(env, e.what());
    }
    return env->NewStringUTF(confirm.c_str());
}
}