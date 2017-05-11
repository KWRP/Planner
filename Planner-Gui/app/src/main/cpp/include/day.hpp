#pragma once
#ifndef DAY_H
#define DAY_H
#include<vector>
#include <iostream>
#include <cstdlib>
//#include <string>
#include "event.hpp"
#include "helpers.hpp"
#include<ctime>

using namespace std;

class Day {
private:
	string date;
	vector<Event*> events; 

public:
	string getDate() { return date; }
	void setDate(string date) { this->date = date; }
	int numOfEvents() { return events.size(); }
	Event *getEvent(int eventNumber);
	void addEvent(string title, string description, int time, int duration);//will change when the xml for event is working
	string toString();
	Day(string);
	Day();
	~Day();
};

#endif