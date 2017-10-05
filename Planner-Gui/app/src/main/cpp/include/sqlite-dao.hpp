
#pragma once
#ifndef PLANNER_GUI_SQLITE_DAO_H
#define PLANNER_GUI_SQLITE_DAO_H

#include "sqlite3.h"
#include <stdio.h>
#include <stdbool.h>
#include <vector>
#include <string>

#ifdef __cplusplus
extern "C"
{
#endif

bool createTableQuery(const char *filepath);

bool createDb(const char *createStatement, sqlite3 *db);

bool insertToDb(const char *day, const char *month, const char *year, const char *title,
                const char *description, const char *start, const char *duration,
                const char *endDay, const char *endMonth, const char *endYear, int repeatCycle,
                const char *filepath);

bool updateToDb(const char *day, const char *month, const char *year, const char *title,
                const char *description, const char *start, const char *duration,
                const char *endDay, const char *endMonth, const char *endYear, int eventID,
                int repeatCycle, const char *filepath);

bool deleteFromDb(int eventID, const char *filepath);

std::vector<std::string> selectFromDB(const char *day, const char *month,
                                                   const char *year,
                                                   const char *filepath);

std::string selectMonth(const char *month, const char *year, const char *filepath);

void displayDb(const char *nativeDb);

#endif //PLANNER_GUI_SQLITE_DAO_H
#ifdef __cplusplus
}
#endif
