#pragma once
#ifndef MONTH_H
#define MONTH_H

#include <string>

class Week;
class Day;

class Month {
private:
    Week *weeks[6];
    std::string date;

protected:
    int numberOfDayInMonth(std::string date);
    std::string changeDate(std::string date, bool forward);

public:
    std::string getDate() { return date; }
    std::string toString();
    Day *getDay(std::string theDay);
    Week *getWeek(std::string theWeek);
    Week *getWeek(int theWeek) {return weeks[theWeek];}
    Month(std::string );
    Month();
    ~Month();
};

#endif