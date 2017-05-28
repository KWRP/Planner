#pragma once
#ifndef HELPERS_H
#define HELPERS_H

#include <string>
#include <sstream>
#include <iostream>
#include <cstdlib>
#include <ctime>

std::string numToString (long l);

std::string getCurrentDate();

int dayOfWeek(std::string date);

int numberOfDayInMonth(std::string date);

std::string changeDate(std::string date, bool forward);
#endif	