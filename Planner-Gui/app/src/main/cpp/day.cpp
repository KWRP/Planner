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
	for(int i = 0; i < events.size(); i++){
        result+= numToString(+1);
        result+= ";";
        result+= events[i]->toString();
    }
	return result;
}
void Day:: setEvent(int id, std::string title, std::string description, int startTime, int duration) {
    Event *eventx = new Event(title, description, startTime, duration);
    events.at(id) = (eventx);
}
void Day::addEvent(char* filepath, std::string title, std::string description, int time, int duration) {//will change when the xml for event is working
	Event *eventx = new Event(filepath, date, title, description, time, duration);
	events.push_back(eventx);
}