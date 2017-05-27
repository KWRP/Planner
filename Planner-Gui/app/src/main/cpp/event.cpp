#include "include/event.hpp"

Event::Event(char *filepath, std::string date, std::string title, std::string description, int startTime, int duration) {//will change when the xml for event is working
 //   char* filepath = new char[100];
  //  strcpy(filepath, "/Users/paulo/Documents");
	this->description = description;
	this->title = title;
	this->startTime = startTime;
	this->duration = duration;
    char *titlec = new char[100];
    strcpy(titlec, title.c_str());
    char* descriptionc = new char[1000];
    strcpy(descriptionc, description.c_str());
    char *startTimec = new char[5];
    strcpy(startTimec, numToString(startTime).c_str());
    char* durationc = new char[9];
    strcpy(durationc, numToString(duration).c_str());

    char* day = new char[3];
    strcpy(day, date.substr(0,2).c_str());
    char* month = new char[3];
    strcpy(month, date.substr(3,2).c_str());
    char *year = new char [5];
    strcpy(year, date.substr(6,4).c_str());
	if(checkDate(day, month, year)){
		addEvent( filepath,day, month, year, titlec, descriptionc, startTimec, durationc);
	}
	else{
		createDate(day, month, year);
        addEvent(filepath, day, month, year, titlec, descriptionc, startTimec, durationc);
	}
    free(day);
    free(month);
    free(year);
    free(titlec);
    free(descriptionc);
    free(startTimec);
    free(durationc);
}
Event::Event(int id, std::string title, std::string description, int startTime, int duration){
    this->title = title;
    this->description = description;
    this->startTime = startTime;
    this->duration = duration;
}
std::string Event::toString() {
	std::string result = "";
	result = ";" + title + ";" + description + ";" +
			numToString(startTime) + ";"+ numToString(duration) +":";//will change when the xml for event is working
	return result;
}