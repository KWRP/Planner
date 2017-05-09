#include "include/Month.h"
#include <string>

int Month::numberOfDayInMonth(string date) {
	int mon = stoi(date.substr(3, 2));
	int daysInMonth = 1;
	if (mon == 04 || mon == 06 || mon == 9 || mon == 11) {
		daysInMonth = 30;
	}
	else if (mon == 02) {
		int year = stoi(date.substr(6, 4));
		if (year % 4 == 0 && year % 100 && year % 400) {
			daysInMonth = 29;
		}
		else {
			daysInMonth = 28;
		}
	}
	else {
		daysInMonth = 31;
	}
	return daysInMonth;
}

string Month::changeDate(string date, bool forward) {
	int month = stoi(date.substr(3, 2));
	int year = stoi(date.substr(6, 4));
	if (forward) {
		if (month == 12) {//checking that if it's the end of the year
			year++;
			date = "01/01/" + to_string(year);
		}
		else {//checks if it's the end of the month
			if (month + 1 < 10)
				date = "01/0" + to_string(month + 1) + "/" + to_string(year);
			else
				date = "01/" + to_string(month + 1) + "/" + to_string(year);
		}
	}
	else {
		if (month == 1) {//checking that if it's the end of the year
			year--;
			date = "31/12/" + to_string(year);
		}
		else {
			int day = numberOfDayInMonth(date.replace(3, 2, to_string(month - 1)));
			if (month - 1 < 10)
				date = to_string(day) + "/" + "0" + to_string(month - 1) + "/" + to_string(year);
			else
				date = to_string(day) + "/" + to_string(month - 1) + "/" + to_string(year);
		}
	}
	return date;
}
Month::Month(string date) {
	this->date = date;
	for (int i = 0; i < 6; i++) {//need to change date for the weeks so date +7 for each week.
		cout << "num weeks " + to_string(i) + " date = " + date + "\n" << endl;
		weeks[i] = new Week(date);
		cout << "after week call\n" << endl;
		if (stoi(date.substr(0, 2)) + 7 < 10) {
			date = date.replace(0, 2, "0" + to_string(stoi(date.substr(0, 2)) + 7));
		}
		else
			date = date.replace(0, 2, to_string(stoi(date.substr(0, 2)) + 7));
		int daysInMonth = numberOfDayInMonth(date);
		if (daysInMonth < stoi(date.substr(0, 2))) {
			int dayDiffOfMonth = stoi(date.substr(0, 2))- daysInMonth;
			date = changeDate(date, true);
			date = date.replace(0, 2, "0" + to_string(stoi(date.substr(0, 2)) + dayDiffOfMonth));
		}
	}
	this->date = date;
}
Month::Month() {
	time_t t = time(0);
	struct tm *now = localtime(&t);
	date = to_string(now->tm_mday) + "/" + to_string(1 + now->tm_mon) + "/" + to_string(now->tm_year + 1900);
	Month(date);
}
Month::~Month() {
	cout << "months of " + date + " destructor was called" << endl;
	for (int i = 0; i < 6; i++) {
		delete weeks[i];
	}
}
Day* Month::getDay(string date) {
	bool foundDay = false;
	for (int week = 0; week < 6|| foundDay; week++) {
		for (int day=0; day < 7 ||foundDay; day++) {
			if (weeks[week]->getDay(day)->getDate() == date) {
				foundDay = true;
				return weeks[week]->getDay(day);
			}
		}
	}
	cout << "NO day found" << endl;
}
Week* Month::getWeek(string date) {
	for (int week = 0; week < 6; week++) {
		for (int day = 0; day < 7; day++) {
			if (weeks[week]->getDay(day)->getDate() == date)
				return weeks[week];
		}
	}
	cout << "week not found" << endl;
}
string Month::toString() {
	string result = "";
	result = "Date is ";// +date;
	result = +"\n\n";
	for (int i = 0; i < 7; i++) {
		//result =+ "\n" + weeks[i]->toString();
	}
	return result;
}
