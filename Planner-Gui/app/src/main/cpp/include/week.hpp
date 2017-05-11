#pragma once
#ifndef WEEK_H
#define WEEK_H

#include "day.hpp"

class Week {
private:
	string date;
	Day* week [7]; //array of days,  should probable change to an array
protected:
	int dayOfWeek(string date);
public:
	string getDate() { return date; }
	void setDate(string date) { this->date = date; }
	Day* getDay(string day);
	Day* getDay(int day);
	string toString();
	Week();
	Week(string date);
	~Week();
	int numberOfDayInMonth(string date);
	string changeDate(string date, bool forward);
};
#endif