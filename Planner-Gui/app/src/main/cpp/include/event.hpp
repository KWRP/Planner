#pragma once
#ifndef EVENT_H
#define EVENT_H

#include "helpers.hpp"
#include "xml-dao.hpp"


class Event {
private:
    std::string title;
    std::string description;
    int startTime;
    int duration;

public:
    /*void setTitle(std::string title) { this->title = title; }
    void setDecription(std::string description) { this->description = decription; }
    void setStartTime(int startTime) { this->startTime = startTime; }
    void setDuration(int duration) { this->duration = duration; }
    std::string getTitle() const { return title; }
    std::string getDescription() const { return description; }
    int getStartTime() const { return startTime; }
    int getDuration() const { return duration; } */
    std::string toString();

    Event(const char *, std::string, std::string, std::string, int, int);

    Event(std::string, std::string, int, int);
};//will be changed with the xml api. 

#endif