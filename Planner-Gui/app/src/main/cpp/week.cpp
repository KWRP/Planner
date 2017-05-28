#include "include/helpers.hpp"
#include "include/week.hpp"
#include "include/day.hpp"




Week::Week(std::string date, const char* filepath) {//might need to check that the substr is correct
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
    std::string thedate = date;
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
		Day *dayx = new Day(date, filepath);
		week[i] =dayx;
        dayx->toString();
	}
	this->date = thedate;
}
Week::Week(const char* filepath) {
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
    std::string thedate = date;
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
		Day *dayx = new Day(date, filepath);
   //     std::cout << "day to string " + dayx->toString() << std::endl;
		week[i] = dayx;
	}
	this->date = thedate;
}

Day* Week::getDay(std::string day) {
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
		mon = atoi(date.substr(3, 2).data());
		int numDays = numberOfDayInMonth(date);
		startDateOfWeek = numDays - theDay;
	}
	dayOfWeek = theDay - startDateOfWeek;
	if (dayOfWeek < 0) {
        std::cout <<date+ "please enter a valid date in the week " + toString() << std::endl;
	}
	return week[dayOfWeek];
}
Day* Week::getDay(int day) {
   return week[day];
}
std::string Week::toString() {
    std::string result = "";
	result = date +"\n";
	for (int i = 0; i < 7; i++) {
		result += week[i]->toString();
	}
	return result;
}
