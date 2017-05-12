#include "include/event.hpp"

Event::Event(std::string title, std::string description, int startTime, int duration) {//will change when the xml for event is working
	this->description = description;
	this->title = title;
	this->startTime = startTime;
	this->duration = duration;
}
std::string Event::toString() {
	std::string result = "";
	result = "title :" + title + "\ndescription :" + description + "\nstart time :" +
			numToString(startTime) + "\nduration :"+ numToString(duration);//will change when the xml for event is working
	return result;
}