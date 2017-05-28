#include "include/event.hpp"

Event::Event(const char *filepath, std::string date, std::string title, std::string description,
             int startTime, int duration) {
    this->description = description;
    this->title = title;
    this->startTime = startTime;
    this->duration = duration;
    std::string time = numToString(startTime);
    std::string durations = numToString(duration); 
   
 
    const char* day = date.substr(0,2).c_str();
    const char* month = date.substr(3,2).c_str();
    const char *year =date.substr(6,4).c_str();
    if(checkDate(day, month, year)){
        bool q = addEvent( filepath,day, month, year, title.c_str(), description.c_str(), time.c_str(), durations.c_str());
    }
    else{
        bool x = createDate(day, month, year);
        bool y = addEvent(filepath, day, month, year, title.c_str(), description.c_str(), time.c_str(), durations.c_str());
    }
        
}
Event::Event(std::string title, std::string description, int startTime, int duration){

    this->title = title;
    this->description = description;
    this->startTime = startTime;
    this->duration = duration;
}
std::string Event::toString() {
	std::string result = "";
	result = ";" + title + ";" + description + ";" +
			numToString(startTime) + ";"+ numToString(duration) +":";
	return result;
}
