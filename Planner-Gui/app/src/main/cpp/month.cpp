#include "include/helpers.hpp"
#include "include/month.hpp"
#include "include/week.hpp"
#include "include/day.hpp"


using namespace std;
int Month::numberOfDayInMonth(string date) {
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

string Month::changeDate(string date, bool forward) {
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
			int day = numberOfDayInMonth(date.replace(3, 2, numToString(month - 1)));
			if (month - 1 < 10)
				date = numToString(day) + "/" + "0" + numToString(month - 1) + "/" + numToString(year);
			else
				date = numToString(day) + "/" + numToString(month - 1) + "/" + numToString(year);
		}
	}
	return date;
}
Month::Month(string date) {
    date= date.replace(0, 2, "01");
	this->date = date;
    string theDate = date;
	for (int i = 0; i < 6; i++) {//need to change date for the weeks so date +7 for each week.
		//cout << "num weeks " + numToString(i) + " date = " + date + "\n" << endl;
		weeks[i] = new Week(date);
	//	cout << "after week call\n" << endl;
		if (atoi(date.substr(0, 2).data()) + 7 < 10) {
			date = date.replace(0, 2, "0" + numToString(atoi(date.substr(0, 2).data()) + 7));
		}
		else
			date = date.replace(0, 2, numToString(atoi(date.substr(0, 2).data()) + 7));
		int daysInMonth = numberOfDayInMonth(date);
		if (daysInMonth < atoi(date.substr(0, 2).data())) {
			int dayDiffOfMonth = atoi(date.substr(0, 2).data())- daysInMonth;
			date = changeDate(date, true);
			date = date.replace(0, 2, "0" + numToString(atoi(date.substr(0, 2).data()) + dayDiffOfMonth));
		}
	}
	this->date = theDate;
}

Month::Month() {
	std::string dayDate="";
	std::string monDate="";
	time_t t = time(0);
	struct tm *now = localtime(&t);
	if (now->tm_mday < 10) {
		dayDate = "0" + numToString(now->tm_mday);
	} else
		dayDate = numToString(now->tm_mday);

	if (now->tm_mon + 1 < 10) {
		monDate = "0" + numToString(1 + now->tm_mon);
	} else
		monDate = numToString(1 + now->tm_mon);
	date = dayDate + "/" + monDate + "/" + numToString(now->tm_year + 1900);

	date= date.replace(0, 2, "01");
	std::string theDate = date;
	for (int i = 0; i < 6; i++) {//need to change date for the weeks so date +7 for each week.
		//cout << "num weeks " + numToString(i) + " date = " + date + "\n" << endl;
		weeks[i] = new Week(date);
		//	cout << "after week call\n" << endl;
		if (atoi(date.substr(0, 2).data()) + 7 < 10) {
			date = date.replace(0, 2, "0" + numToString(atoi(date.substr(0, 2).data()) + 7));
		}
		else
			date = date.replace(0, 2, numToString(atoi(date.substr(0, 2).data()) + 7));
		int daysInMonth = numberOfDayInMonth(date);
		if (daysInMonth < atoi(date.substr(0, 2).data())) {
			int dayDiffOfMonth = atoi(date.substr(0, 2).data())- daysInMonth;
			date = changeDate(date, true);
			date = date.replace(0, 2, "0" + numToString(atoi(date.substr(0, 2).data()) + dayDiffOfMonth));
		}
	}
	this->date = theDate;
}
Month::~Month() {
	cout << "months of " + date + " destructor was called" << endl;
	for (int i = 0; i < 6; i++) {
		delete weeks[i];
	}
}

Day* Month::getDay(string date) {
	for (int week = 0; week < 6; week++) {
		for (int day=0; day < 7 ; day++) {
         //   cout << "date = "+ date<<endl;
			if (weeks[week]->getDay(day)->getDate() == date) {//something is wrong here with getDay()
				return weeks[week]->getDay(day);
			}
		}
	}
	cout << "NO day found" << endl;
	return nullptr;
}

Week* Month::getWeek(string date) {
	for (int week = 0; week < 6; week++) {
        if(weeks[week]->getDay(date) != NULL )
            return weeks[week];
	//	for (int day = 0; day < 7; day++) {
		//	if (weeks[week]->getDay(day)->getDate() == date)
		//		return weeks[week];
		//}
	}
	cout << "week not found" << endl;
    return nullptr;
}

string Month::toString() {
	string result = "";
	result = "Date is " +date +"\n";
//	result += "hi";
	for (int i = 0; i < 6; i++) {
		result +=weeks[i]->toString() ;
	}

	return result;
}
