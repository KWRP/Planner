/*====================================================================================================

				need to call the destructors once you are finished with an object
				
=====================================================================================================*/
#include <iostream>
#include "inlcude/Year.h"
#include "include/Month.h"
#include<string>

using namespace std;
/*need to change the methods parm for pointer*/
/*

Day getDay(Year year, string date) {
	return getMonth(year, date).getDay(date);
}

Day getDay(Month month, string date) {
	return(month.getDay(date));
}*/

Day* getDay(Week *week, string date) {
	return week->getDay(date);
}

Event* getEventnum(Day *day, string date, int numberOfEvent) {
	return day->getEvent(numberOfEvent - 1);
}
/*
void setEvent(Year year,string date, string title, string description, int time, int duration) {//need to change for pointer/ references
	Day day = getDay(year, date);
	day.addEvent(title, description, time, duration);
	cout << day.toString() << endl;
	day = day;
}*/

void setEvent(Week *week, string date, string title, string description, int time, int duration) {
    getDay(week, date)->addEvent(title, description, time, duration);

}


int main() {
	string quit;
	string date = "01/06/2017";
	string date2 = "30/05/2017";
	cout << "hello world" << endl;
	Month *month = new Month(date);
	cout <<"months get week "+ month->getWeek(date)->toString() << endl;
	//Week *week = new Week(date);
	//Day *day = new Day();
//	setEvent(week,date2, "test", "testing a", 3243, 2);
	//day->addEvent("test", "testing", 3456, 32);
	//cout << day->getEvent(1)->getTitle() << endl;
//	cout << day->toString() << endl;
//	cout << "\n week tostring "+week->toString()+"\n\n" +week->getDay(date2)->getEvent(1)->toString() <<endl;
	cin >> quit;
	while (quit != "q") {
		cin >> quit;
	}
	delete month;
	return 0;
}
