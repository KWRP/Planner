#include "include/helpers.hpp"

/**
 * Converts a given long into a string.
 *
 * @param long intended to be converted to string
 * @return the converted string
 */
std::string numToString(long l) {
    std::stringstream ss;
    ss << l;
    std::string newString = ss.str();
    return newString;
}

/**
 * collects and formats the date to the format DD/MM/YYYY
 *
 * @return the formatted date
 */
std::string getCurrentDate() {
    std::string dayDate = "";
    std::string monDate = "";
    std::string date = "";
    time_t t = time(0);
    struct tm *now = localtime(&t);
    if (now->tm_mday < 10) {
        dayDate = "0" + numToString(now->tm_mday);
    } else
        dayDate = numToString(now->tm_mday);

    if (now->tm_mon + 1 < 10) {
        monDate = "0" + numToString(1 + now->tm_mon);
    } else
        monDate = numToString(1 + now->tm_mon);
    date = dayDate + "/" + monDate + "/" + numToString(now->tm_year + 1900);
    return date;
}
/**
 * Finds the day of the week a given date falls on.
 *
 * @param day portion of date
 * @param month portion of date
 * @param year portion of date
 * @return the day of the week
 */
int dayOfWeekI(int day, int month, int year) {

    struct tm time_in = {0, 0, 0, // second, minute, hour
                         day, month - 1,
                         year - 1900}; // 1-based day, 0-based month, year since 1900
    time_t time_temp = mktime(&time_in);

    struct tm const *time_out = localtime(&time_temp);
    int n = time_out->tm_wday + 6;
    n = n % 7;
    return n;
}
/**
 * Finds the maximum days for a given month in a given year
 *
 * @param month
 * @param year
 * @return the total days in the given month
 */
int maxDays(int mon, int year) {
    int daysInMonth = 0;
    if (mon == 04 || mon == 06 || mon == 9 || mon == 11) {
        daysInMonth = 30;
    } else if (mon == 02) {
        if (year % 4 == 0 && year % 100 && year % 400) {
            daysInMonth = 29;
        } else {
            daysInMonth = 28;
        }
    } else {
        daysInMonth = 31;
    }
    return daysInMonth;
}
/**
 * Throws a Java exception
 *
 * @param JNI environment
 * @param message to be thrown
 */
void throwJavaException(JNIEnv *env, const char *msg) {
    jclass c = env->FindClass("java/lang/RuntimeException");

    if (NULL == c) {
        c = env->FindClass("java/lang/NullPointerException");
    }
    env->ThrowNew(c, msg);
}