#include "include/helpers.hpp"

std::string numToString (long l) {
	std::stringstream ss;
    ss << l;
    std::string newString = ss.str();
	return newString;
}

std::string getCurrentDate() {
    time_t t = time(0);
    struct tm *now = localtime(&t);
    std::string date = numToString(now->tm_mday) + "/" + numToString(now->tm_mon + 1)
                       + "/" + numToString(now->tm_year + 1900);
    return date;
}