#pragma once
#ifndef WEEK_H
#define WEEK_H

#include <iostream>
#include <stdlib.h>


using namespace tinyxml2;
#ifndef XMLCheckResult
#define XMLCheckResult(a_eResult) if (a_eResult != XML_SUCCESS) { printf("Error: %i\n", a_eResult); return a_eResult; }
#endif

bool checkDate(const char* day = 0, const char* month = 0, const char* year = 0);
bool test (std::string path);
bool createDate(const char* day = 0, const char* month = 0, const char* year = 0);
bool addEvent(const char* day = 0, const char* month = 0, const char* year = 0, const char* title = 0,
				const char* description = 0, const char* startTime = 0, const char* duration = 0, std::string path = "");
bool pullDay(const char* day = 0, const char* month = 0, const char* year = 0);
#endif