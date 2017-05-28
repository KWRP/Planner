#include "include/helpers.hpp"
#include "include/month.hpp"

#include "include/week.hpp"
#include "include/day.hpp"


using namespace std;

Month::Month(string date, const char* filepath) {
    date= date.replace(0, 2, "01");
	this->date = date;
    string theDate = date;
	for (int i = 0; i < 6; i++) {
		weeks[i] = new Week(date, filepath);
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

Month::Month(const char* filepath) {
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
		weeks[i] = new Week(date, filepath);
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


Day* Month::getDay(string date) {
	for (int week = 0; week < 6; week++) {
		for (int day=0; day < 7 ; day++) {
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
	}
	cout << "week not found" << endl;
    return nullptr;
}

string Month::toString() {
	string result = "";
	result = "Date is " +date +"\n";
	for (int i = 0; i < 6; i++) {
		result +=weeks[i]->toString();
	}

	return result;
}
