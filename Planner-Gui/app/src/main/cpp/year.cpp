#include "include/helpers.hpp"
#include "include/year.hpp"
#include "include/month.hpp"

Year::Year(std::string date) {
	this->date = date;
	for (int i = 0; i < 12; i++) {//changing though the months
		std::string month = "";
		if (i < 9)
			month = "0" + numToString(i+1);
		else
			month = numToString(i+1);
		date = date.erase(3, 2);
		date = date.insert(3, month);
		Month monthx(date);
		months.push_back(monthx);
	}
}
Year::~Year() {}
//Month Year::getMonth(string date) {
	//int mon = atoi(date.substr(3, 2).data());
	//return months.at(mon - 1);
//}
std::string Year::toString() {
    std::string result = "";
	result = "The year is " + date + " there are " + numToString(months.size()) + "months in a year.";
	return result;
}