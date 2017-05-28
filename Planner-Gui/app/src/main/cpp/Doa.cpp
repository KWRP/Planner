
#include <iostream>
#include "include/month.hpp"
#include "include/event.hpp"
#include "include/week.hpp"

using namespace std;

int main(){
    string s = "events.xml";
    bool xml = createXml(s.c_str());
	string quit;
	string date = "29/06/2017";
	string date2 = "30/05/2017";
    Month *month =  new Month(date, s.c_str());
    for(int i =0; i<6; i++){
        for(int j = 0; j<7; j++){
            month->getWeek(i)->getDay(j)->addEvent("test week "+numToString(i)+" day "+numToString(j), "test", 3, 4 );
        }
    }
    cout<<"month get week " + month->getWeek(date)->toString()<<endl;
    cout<<"month get day " + month->getDay(date2)->toString()<<endl;
    cout<<"week get day "<< month->getWeek(date2)->getDay(date2)->numOfEvents()<<endl;
    cout<<"month toString " <<month->toString()<<endl;

   //Month *month = new Month(date);
//    Month *month = new Month(date2, s.c_str());
//    month->getDay(date)->addEvent("testing", "thetest", 324,4534);
//    cout<<"month tostring " << month->toString()<<endl;

  //  const char* ss = s.c_str();
    //Day *day = new Day(date2, ss);
     //Week *week = new Week(date2, ss);
  //  week->getDay(2)->addEvent("test", "testing", 324 ,43);
  //  cout<< "week toString " << week->toString()<<endl;
    //cin >> quit;
    //	while (quit != "q") {
    //	cin >> quit;
                //	}
    //	delete month;
	return 0;
}
