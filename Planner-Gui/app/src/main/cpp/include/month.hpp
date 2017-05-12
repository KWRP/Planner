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

    int numberOfDayInMonth(std::string date);

    std::string changeDate(std::string date, bool forward);

public:
    void setDate(std::string date) { this->date = date; }

    std::string getDate() { return date; }

    std::string toString();

    Day *getDay(std::string theDay);

    Week *getWeek(std::string theWeek);

    Month(std::string );

    Month();

    ~Month();
};

#endif