#include "include/day.hpp"
#include "include/event.hpp"
#include <android/log.h>

Day::Day(std::string date1, const char *filepath) {

    this->date = date1;
    const char *day = date1.substr(0, 2).c_str();
    const char *month = date1.substr(3, 2).c_str();
    const char *year = date1.substr(6, 4).c_str();

    this->filepath = filepath;

    /*std::string dayString = day;
    std::string monthString = month;
    if (std::atoi(day)< 10) {
        std::string dayString = "0";
        dayString.append(day);
    }
    if (std::atoi(month) < 10) {
        std::string monthString = "0";
        monthString.append(month);
    }
    date = dayString + "/" + monthString + "/" + year;
     */
    __android_log_print(ANDROID_LOG_INFO, "TEST C++!!! Day constructor", "%s :%s :%s :%s : %s",
                        this->date.c_str(), this->filepath, day, month, year);

    std::string delimiter = "/";
    std::string day1 = date.substr(0, date.find(delimiter));
    std::string rest1 = date.substr(date.find(delimiter) + 1);
    std::string month1 = rest1.substr(0, date.find(delimiter));

    std::string year1 = rest1.substr(date.find(delimiter) + 1);

    __android_log_print(ANDROID_LOG_INFO, "TEST C++!!! Day constructor", "%s :%s :%s :%s : %s",
                        this->date.c_str(), this->filepath, day1.c_str(), month1.c_str(),
                        year1.c_str());

    bool s = pullDay(this, filepath, day1.c_str(), month1.c_str(), year1.c_str());
}
Day::Day(const char* filepath) {
	std::string dayDate="";
	std::string monDate="";
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
    const char* day = date.substr(0,2).c_str();
    const char* month = date.substr(3,2).c_str();
    const char* year = date.substr(5,4).c_str();
    pullDay(this, filepath, day, month, year);
}

Day::~Day() {
	std::cout << "day of " + date + " destructor was called" << std::endl;
}

Event* Day::getEvent(int eventNumber) {
	return events.at(eventNumber-1);

}
std::string Day::toString() {
	std::string result = "";
	for(int i = 0; i < events.size(); i++){
        result += numToString(i + 1);
        result+= events[i]->toString();
    }
	return result;
}
void Day:: setEvent(int id, std::string title, std::string description, int startTime, int duration) {
    Event *eventx = new Event(title, description, startTime, duration);
    events.push_back(eventx);
}
void Day::addEvent(char* filepath, std::string title, std::string description, int time, int duration) {//will change when the xml for event is working
	Event *eventx = new Event(filepath, date, title, description, time, duration);
	events.push_back(eventx);
}