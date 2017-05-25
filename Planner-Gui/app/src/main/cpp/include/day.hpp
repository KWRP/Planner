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

public:
    std::string getDate() { return date; }
	int numOfEvents() { return events.size(); }
	Event *getEvent(int eventNumber);
	void addEvent(std::string title, std::string description, int time, int duration);//will change when the xml for event is working
    std::string toString();
	Day(std::string);
	Day();
	~Day();
};

#endif