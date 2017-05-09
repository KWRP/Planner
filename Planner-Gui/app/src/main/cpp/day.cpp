#include "include/Day.h"
#include <string>

Day::Day(string date) {
	this->date = date;
}
Day::Day() {
		time_t t = time(0);
	struct tm *now = localtime(&t);
	date =  to_string(now->tm_mday) +"/"+ to_string(1+now->tm_mon) +"/"+ to_string(now->tm_year +1900);
}
Day::~Day() {
	cout << "day of " + date + " destructor was called" << endl;
}

Event* Day::getEvent(int eventNumber) {
	return events.at(eventNumber-1);

}
string Day::toString() {
	string result = "";
	result = "Date : " + date + "\nnumber of Events : " + to_string(events.size()) + "\n";
	return result;
}

void Day::addEvent(string title, string description, int time, int duration) {//will change when the xml for event is working
	Event *eventx = new Event(title, description, time, duration);
	events.push_back(eventx);
}