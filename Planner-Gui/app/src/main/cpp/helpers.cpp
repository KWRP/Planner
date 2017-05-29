#include "include/helpers.hpp"

std::string numToString(long l) {
    std::stringstream ss;
    ss << l;
    std::string newString = ss.str();
    return newString;
}

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

int dayOfWeek(std::string date) {
    int day = atoi(date.substr(0, 2).data());
    int mon = atoi(date.substr(3, 2).data());
    int year = atoi(date.substr(6, 4).data());
    std::tm time_in = {0, 0, 0, // second, minute, hour
                       day, mon - 1, year - 1900}; // 1-based day, 0-based month, year since 1900
    std::time_t time_temp = std::mktime(&time_in);

    std::tm const *time_out = std::localtime(&time_temp);
    int n = time_out->tm_wday + 6;
    n = n % 7;
    return n;
}

int numberOfDayInMonth(std::string date) {
    int mon = atoi(date.substr(3, 2).data());
    int daysInMonth = 1;
    if (mon == 04 || mon == 06 || mon == 9 || mon == 11) {
        daysInMonth = 30;
    } else if (mon == 02) {
        int year = atoi(date.substr(6, 4).data());
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

std::string changeDate(std::string date, bool forward) {
    int month = atoi(date.substr(3, 2).data());
    int year = atoi(date.substr(6, 4).data());
    if (forward) {
        if (month == 12) {//checking that if it's the end of the year
            year++;
            date = "01/01/" + numToString(year);
        } else {//checks if it's the end of the month
            if (month + 1 < 10)
                date = "01/0" + numToString(month + 1) + "/" + numToString(year);
            else
                date = "01/" + numToString(month + 1) + "/" + numToString(year);
        }
    } else {
        if (month == 1) {//checking that if it's the end of the year
            year--;
            date = "31/12/" + numToString(year);
        } else {
            int day = numberOfDayInMonth(date.replace(3, 2, numToString(month - 1)));
            if (month - 1 < 10)
                date = numToString(day) + "/" + "0" + numToString(month - 1) + "/" +
                       numToString(year);
            else
                date = numToString(day) + "/" + numToString(month - 1) + "/" + numToString(year);
        }
    }
    return date;
}

void throwJavaException(JNIEnv *env, const char *msg) {
    jclass c = env->FindClass("java/lang/RuntimeException");

    if (NULL == c) {
        c = env->FindClass("java/lang/NullPointerException");
    }
    env->ThrowNew(c, msg);
}