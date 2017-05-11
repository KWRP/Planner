#include "include/week.hpp"
#include <string>

using namespace std;
int Week::dayOfWeek(string date) {
	int day = atoi(date.substr(0, 2).data());
	int mon = atoi(date.substr(3, 2).data());
	int year = atoi(date.substr(6, 4).data());
	std::tm time_in = { 0, 0, 0, // second, minute, hour
	day, mon-1, year - 1900 }; // 1-based day, 0-based month, year since 1900

	std::time_t time_temp = std::mktime(&time_in);

	// the return value from localtime is a static global - do not call
	// this function from more than one thread!
	std::tm const *time_out = std::localtime(&time_temp);
	int n = time_out->tm_wday + 6;
	n =n % 7;
//	std::cout << "I was born on (Sunday = 6) D.O.W. " << n << '\n';
	return n;
}
int Week::numberOfDayInMonth(string date) {
	int mon = atoi(date.substr(3, 2).data());
	int daysInMonth = 1;
	if (mon == 04 || mon == 06 || mon == 9 || mon == 11) {
		daysInMonth = 30;
	}
	else if (mon == 02) {
		int year = atoi(date.substr(6, 4).data());
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

string Week::changeDate(string date, bool forward) {
	int month = atoi(date.substr(3, 2).data());
	int year = atoi(date.substr(6, 4).data());
	if (forward) {
		if (month == 12) {//checking that if it's the end of the year
			year++;
			date = "01/01/" + numToString(year);
		}
		else {//checks if it's the end of the month
			if (month + 1 < 10)
				date = "01/0" + numToString(month + 1) + "/" + numToString(year);
			else
				date = "01/" + numToString(month + 1) + "/" + numToString(year);
		}
	}
	else {
		if (month == 1) {//checking that if it's the end of the year
			year--;
			date = "31/12/" + numToString(year);
		}
		else {
			int day = numberOfDayInMonth(date.replace(3, 2, numToString(month-1)));
			if (month - 1 < 10)
				date = numToString(day) + "/" + "0" + numToString(month - 1) + "/" + numToString(year);
			else
				date = numToString(day) + "/" + numToString(month - 1) + "/" + numToString(year);
		}
	}
	return date;
}
Week::Week(string date) {//might need to check that the substr is correct
		this->date = date;
		int dayChangeMon = atoi(date.substr(0, 2).data()) - dayOfWeek(date);
		if (dayChangeMon < 0) {
			date = changeDate(date, false);
			date = date.replace(0, 2, numToString(atoi(date.substr(0, 2).data()) + dayChangeMon));
		}
		else {
			if (dayChangeMon < 10) {
				date = date.replace(0, 2, "0" + numToString(dayChangeMon));
			}
			else {
				date = date.replace(0, 2, numToString(dayChangeMon));
			}
		}
		string thedate = date;
	int daysInMonth = numberOfDayInMonth(date);
	for (int i = 0; i < 7; i++) {
		int dateOfWeek = 0;
		if (i == 0) {
			 dateOfWeek = atoi(date.substr(0, 2).data());
		}
		else
			 dateOfWeek = (atoi(date.substr(0, 2).data()) + 1);
		if (dateOfWeek > daysInMonth) {
			date = changeDate(date, true);
		}
		else {
			date = date.erase(0, 2);
			if (dateOfWeek < 10)
				date = date.insert(0, "0" + numToString(dateOfWeek));
			else
				date = date.insert(0, numToString(dateOfWeek));
		}
		Day *dayx = new Day(date);
	//	cout << "day to string " + dayx->toString() << endl;
		week[i] =dayx;
	}
	this->date = thedate;
}
Week::Week() {
	time_t t = time(0);
	struct tm *now = localtime(&t);
	date = numToString(now->tm_mday) + "/" + numToString(1 + now->tm_mon) + "/" + numToString(now->tm_year + 1900);
	int dayChangeMon = atoi(date.substr(0, 2).data()) - dayOfWeek(date);
					if (dayChangeMon < 0) {
						date = changeDate(date, false);
						date = date.replace(0, 2, numToString(atoi(date.substr(0, 2).data()) - dayChangeMon));
					}
					else {
						if (dayChangeMon < 10) {
							date = date.replace(0, 2, "0" + numToString(dayChangeMon));
						}
						else {
							date = date.replace(0, 2, numToString(dayChangeMon));
						}
					}
					string thedate = date;
	int daysInMonth = numberOfDayInMonth(date);
	for (int i = 0; i < 7; i++) {
		int dateOfWeek = 0;
		if (i == 0) {
			dateOfWeek = atoi(date.substr(0, 2).data());
		}
		else
			dateOfWeek = (atoi(date.substr(0, 2).data()) + 1);
		if (dateOfWeek > daysInMonth) {
			date = changeDate(date, true);
		}
		else {
			date = date.erase(0, 2);
			if (dateOfWeek < 10)
				date = date.insert(0, "0" + numToString(dateOfWeek));
			else
				date = date.insert(0, numToString(dateOfWeek));
		}
		Day *dayx = new Day(date);
		cout << "day to string " + dayx->toString() << endl;
		week[i] = dayx;
	}
	this->date = thedate;
}
Week::~Week() {
	cout << "week of " + date + " destuctor was called" << endl;
	for (int i = 0; i < 7; i++) {
		delete week[i];
	}
}
Day* Week::getDay(string day) {
	int dayOfWeek = 0;
	int startDateOfWeek = atoi(date.substr(0,2).data());
	int theDay = atoi(day.substr(0,2).data());

	int mon = atoi(day.substr(4, 2).data());
	int startmon = atoi(date.substr(4, 2).data());

	int year = atoi(day.substr(6, 4).data());
	int startYear = atoi(date.substr(6, 4).data());
	
	if (startYear < year) {//?
		startDateOfWeek = 31 - theDay;
	}
	if (startmon < mon) {
		this->date = date;
		//int year = atoi(date.substr(6, 4).data());
		int mon = atoi(date.substr(3, 2).data());
		int numDays = numberOfDayInMonth(date);
		startDateOfWeek = numDays - theDay;
	}
	dayOfWeek = theDay - startDateOfWeek;
	if (dayOfWeek < 0) {
		cout << "please enter a valid date in the week " + toString() << endl;
	}
	return week[dayOfWeek];
}
Day* Week::getDay(int day) {
	for (int i=0; i < 7; i++) {
		if (atoi(week[i]->getDate().substr(0, 2).data()) == day)
			return week[i];
	}
	cout << "day not found" << endl;
}
string Week::toString() {
	string result = "";
	result = date +"\n";
	for (int i = 0; i < 7; i++) {
		result += week[i]->toString();
	}
	return result;
}
