#pragma once
#ifndef DAY_H
#define DAY_H

#include "helpers.hpp"
#include <vector>
#include <iostream>
#include <cstdlib>
#include <ctime>

class Event;

class Day {
private:
    std::string date;
    std::vector<Event *> events;
    const char *filepath;

public:
    std::string getDate() { return date; }

    int numOfEvents() { return (int)events.size(); }

    Event *getEvent(int eventNumber);

    void addEvent(std::string title, std::string description, int time, int duration);

    void setEvent(std::string title, std::string description, int time, int duration);

    std::string toString();

    Day(std::string, const char *);

    Day(const char *);

    ~Day();
};

#endif