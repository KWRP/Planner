#pragma once
#ifndef DAY_H
#define DAY_H

#include "helpers.hpp"
#include <vector>
#include <iostream>
#include <cstdlib>
#include <ctime>

class Event;

class Day {
private:
    std::string date;
    std::vector<Event*> events;
    const char *filepath;

public:
    std::string getDate() { return date; }
	int numOfEvents() { return events.size(); }
	Event *getEvent(int eventNumber);
	void addEvent(char*,std::string title, std::string description, int time, int duration);//will change when the xml for event is working
    void setEvent(int id, std::string title, std::string description, int time, int duration);
    std::string toString();
	Day(std::string, const char*);
	Day(const char*);
	~Day();
};

#endif