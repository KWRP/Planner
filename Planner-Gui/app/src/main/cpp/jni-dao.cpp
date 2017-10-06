
#include "include/jni-dao.h"
#include "include/helpers.hpp"
#include "sqlite-dao.hpp"

using namespace std;
extern "C" {

/**
 * Get tbe date from system for display day.
 *
 * @return date     Returns a delimited date.
 */
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetCurrentDate(
        JNIEnv *env,
        jobject) {
    string date = getCurrentDate();
    return env->NewStringUTF(date.c_str());
}

/**
 * Get the date from system for dialog action.
 *
 * @return date     Returns a delimited date.
 */
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DialogAction_jniGetCurrentDate(
        JNIEnv *env,
        jobject) {
    string date = getCurrentDate();
    return env->NewStringUTF(date.c_str());
}

/**
 * Check if the database file exists otherwise create a new one.
 *
 * @return confirm      Confirms if the database was created.
 */
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniCreateDb(
        JNIEnv *env,
        jobject , jstring dir) {
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

/**
 * Check if the database file exists otherwise create a new one.
 *
 * @param dir       The location of the database file.
 *
 * @return confirm      Confirms if the database was created.
 */
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniCreateDb(
        JNIEnv *env,
        jobject, jstring dir) {
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


/**
 * Creates a new event from display day.
 *
 * @param title         The title of the event.
 * @param description   A description of the event.
 * @param start         The start time of the event.
 * @param finish        The start time of the event.
 * @param startDate     The start date of the event.
 * @param endDate       The end date of the event.
 * @param repeat        The repeat cycle for the event.
 * @param filepath      The location of the database file.
 *
 * @return confirm      Confirms if the event was addeded to the database.
 */
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniCreateDbEvent(
        JNIEnv *env, jobject, jstring title, jstring description, jstring start,
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

/**
 * Gets all the events on a day for a selected date for display day.
 *
 * @param dir       The location of the database file.
 * @param date      The date of the day to get the events for.
 *
 * @return dayString Returns a delimited string with all the day events.
 */
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetDayDb(
        JNIEnv *env, jobject, jstring dir, jstring date) {

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

/**
 * Gets the current date from the system.
 *
 * @return date     Returns a delimited date.
 */
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniGetCurrentDate(
        JNIEnv *env,
        jobject) {
    string date = getCurrentDate();
    return env->NewStringUTF(date.c_str());
}


/**
 * Updates an existing event within the database.
 *
 * @param title         The title of the event.
 * @param description   A description of the event.
 * @param start         The start time of the event.
 * @param finish        The start time of the event.
 * @param startDate     The start date of the event.
 * @param endDate       The end date of the event.
 * @param repeat        The repeat cycle for the event.
 * @param eventID       The id of an event in the database.
 * @param filepath      The location of the database file.
 *
 * @return confirm      Confirms if the event was updated.
 */
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniUpdateEventDb(
        JNIEnv *env, jobject, jstring title, jstring description, jstring start,
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

        bool updateEvent = updateToDb(day.c_str(), month.c_str(), year.c_str(),
                                      nativeTitle, nativeDescription, nativeStart, nativeFinish,
                                      endDay.c_str(), endMonth.c_str(), endYear.c_str(),
                                      atoi(nativeID),
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


/**
 * Removes a selected event from display day.
 *
 * @param eventId       The id of an event within the database.
 * @param filepath      The location of the database file.
 *
 * @return confirm      Confirms if an event was removed.
 */
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniRemoveEventDb(
        JNIEnv *env, jobject, jstring eventId, jstring filepath) {
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

/**
 * Gets the number of each day that has an event in a selected month.
 *
 * @param month     The month to search for events.
 * @param year      The year to search for events.
 * @param dir       The location of the database file.
 *
 * @return getMonth Returns a delimited string with all the day events.
 */
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniGetEventsDb(
        JNIEnv *env, jobject, jstring month, jstring year, jstring dir) {

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

/**
 * Creates a new event from display month.
 *
 * @param title         The title of the event.
 * @param description   A description of the event.
 * @param start         The start time of the event.
 * @param finish        The start time of the event.
 * @param startDate     The start date of the event.
 * @param endDate       The end date of the event.
 * @param repeat        The repeat cycle for the event.
 * @param filepath      The location of the database file.
 *
 * @return confirm      Confirms if the event was added to the database.
 */
JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniCreateDbEvent(
        JNIEnv *env, jobject, jstring title, jstring description, jstring start,
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