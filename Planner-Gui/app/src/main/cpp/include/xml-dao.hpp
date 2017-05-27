#pragma once
#ifndef XML_DAO_H
#define XML_DAO_H

#include <iostream>
#include <stdlib.h>
#include "day.hpp"

#ifndef XMLCheckResult
#define XMLCheckResult(a_eResult) if (a_eResult != XML_SUCCESS) { printf("Error: %i\n", a_eResult); return a_eResult; }
#endif


bool checkDate(const char* filePath, const char* day = 0, const char* month = 0, const char* year = 0);
bool createXml (const char* filePath);
bool createDate(const char* filePath, const char* day = 0, const char* month = 0, const char* year = 0);
bool addEvent(const char* filePath, const char* day = 0, const char* month = 0, const char* year = 0, const char* title = 0,
				const char* description = 0, const char* startTime = 0, const char* duration = 0);

bool pullDay(Day *dayObj, const char *filePath, const char *day = 0, const char *month = 0,
             const char *year = 0);
#endif