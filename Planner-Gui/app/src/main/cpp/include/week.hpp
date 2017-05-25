#pragma once
#ifndef WEEK_H
#define WEEK_H

#include <string>


class Day;
class Week {
private:
	std::string date;
	Day* week [7]; //array of days,  should probable change to an array
protected:
	int dayOfWeek(std::string date);
public:
	std::string getDate() { return date; }
	Day* getDay(std::string day);
	Day* getDay(int day);
	std::string toString();
	Week();
	Week(std::string date);
	~Week();
	int numberOfDayInMonth(std::string date);
	std::string changeDate(std::string date, bool forward);
};
#endif