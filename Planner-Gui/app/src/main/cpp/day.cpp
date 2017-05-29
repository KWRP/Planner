/**
 * Day is used to hold a vector of events, and hold the date for
 * that day.
 *
 */


#include "include/day.hpp"
#include "include/event.hpp"

Day::Day(std::string date1, const char *filepath) {
    this->date = date1;
    this->filepath = filepath;

    std::string delimiter = "/";
    std::string day1 = date.substr(0, date.find(delimiter));
    std::string rest1 = date.substr(date.find(delimiter) + 1);
    std::string month1 = rest1.substr(0, date.find(delimiter));
    std::string year1 = rest1.substr(date.find(delimiter) + 1);

    bool getDay = pullDay(this, filepath, day1.c_str(), month1.c_str(), year1.c_str());
}

Day::Day(const char *filepath) {
    std::string dayDate = "";
    std::string monDate = "";
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
    this->date = date;
    const char *day = date.substr(0, 2).c_str();
    const char *month = date.substr(3, 2).c_str();
    const char *year = date.substr(5, 4).c_str();
    pullDay(this, filepath, day, month, year);
}

Event *Day::getEvent(int eventNumber) {
    unsigned number = eventNumber - 1;
    return events[number];

}

std::string Day::toString() {
    std::string result = "";
    for (int i = 0; i < events.size(); i++) {
        result += numToString(i);
        result += events[i]->toString();
    }
    return result;
}

void
Day::setEvent(std::string title, std::string description, int startTime, int duration) {
    Event *eventx = new Event(title, description, startTime, duration);
    events.push_back(eventx);

}

void Day::addEvent(std::string title, std::string description, int time, int duration) {
    Event *eventx = new Event(filepath, date, title, description, time, duration);
    events.push_back(eventx);
}

Day::~Day() {
    int size = events.size();
    for (int i = size; i > 0; i--) {
        free(events[i]);
    }
}
