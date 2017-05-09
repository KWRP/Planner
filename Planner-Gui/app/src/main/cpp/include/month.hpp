#pragma once
#ifndef MONTH_H
#define MONTH_H
#include "Week.h"

class Month {
private:	Week *weeks[6];
			string date;
			int numberOfDayInMonth(string date);
			string changeDate(string date, bool forward);
public:
	void setDate(string date) { this->date = date; }
	string getDate() { return date; }
	string toString();
	Day* getDay(string theDay);
	Week* getWeek(string theWeek);

	Month(string );
	Month();
	~Month();
};

#endif