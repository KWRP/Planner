#include "include/day.hpp"
#include <string>

Day::Day(string date) {
	this->date = date;
}
Day::Day() {
		time_t t = time(0);
	struct tm *now = localtime(&t);
	date =  numToString(now->tm_mday) +"/"+ numToString(1+now->tm_mon) +"/"+ numToString(now->tm_year +1900);
}
Day::~Day() {
	cout << "day of " + date + " destructor was called" << endl;
}

Event* Day::getEvent(int eventNumber) {
	return events.at(eventNumber-1);

}
string Day::toString() {
	string result = "";
	result = "Date : " + date + "\nnumber of Events : " + numToString(events.size()) + "\n";
	return result;
}

void Day::addEvent(string title, string description, int time, int duration) {//will change when the xml for event is working
	Event *eventx = new Event(title, description, time, duration);
	events.push_back(eventx);
}