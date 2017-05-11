#include <iostream>
#include "include/event.hpp"
#include <string>

Event::Event(string title, string description, int startTime, int duration) {//will change when the xml for event is working
	this->description = description;
	this->title = title;
	this->startTime = startTime;
	this->duration = duration;
}
string Event::toString() {
	string result = "";
	result = "title :" + title + "\ndescription :" + description + "\nstart time :" + numToString(startTime) + "\nduration :"+ numToString(duration);//will change when the xml for event is working
	return result;
}