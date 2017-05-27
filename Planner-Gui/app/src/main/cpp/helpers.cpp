#include "include/helpers.hpp"

std::string numToString (long l) {
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