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
public:
    std::string getDate() { return date; }

    std::string toString();

    Day *getDay(std::string theDay);

    Week *getWeek(std::string theWeek);

    Week *getWeek(int theWeek) { return weeks[theWeek]; }

    Month(std::string, const char *);

    Month(const char *);

    ~Month();
};

#endif
