#include "include/day.hpp"
#include "include/event.hpp"

Day::Day(std::string date) {
	this->date = date;
}
Day::Day() {
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
}

Day::~Day() {
	std::cout << "day of " + date + " destructor was called" << std::endl;
}

Event* Day::getEvent(int eventNumber) {
	return events.at(eventNumber-1);

}
std::string Day::toString() {
	std::string result = "";
	result = "Date : " + date + "\nnumber of Events : " + numToString(events.size()) + "\n";
	return result;
}

void Day::addEvent(std::string title, std::string description, int time, int duration) {//will change when the xml for event is working
	Event *eventx = new Event(date, title, description, time, duration);
	events.push_back(eventx);
}