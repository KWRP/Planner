/*====================================================================================================

				need to call the destructors once you are finished with an object
				
=====================================================================================================*/
#include <iostream>
#include "include/year.hpp"
#include "include/month.hpp"
#include "include/event.hpp"
#include "include/day.hpp"
#include "include/week.hpp"

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

void setEvent(Month *month,string date, string title, string description, int time, int duration) {
	month->getDay(date)->addEvent(title, description, time, duration);
}

void setEvent(Week *week, string date, string title, string description, int time, int duration) {
    getDay(week, date)->addEvent(title, description, time, duration);

}


int main() {
	string quit;
	string date = "29/06/2017";
	string date2 = "30/05/2017";
  /*  Month *month =  new Month(date);
    cout<<"month get week " + month->getWeek(date)->toString()<<endl;
    cout<<"month get day " + month->getDay(date2)->toString()<<endl;
    cout<<"week get day "<< month->getWeek(date2)->getDay(date2)->numOfEvents()<<endl;
*/

	cin >> quit;
	while (quit != "q") {
		cin >> quit;
	}
//	delete month;
	return 0;
}
