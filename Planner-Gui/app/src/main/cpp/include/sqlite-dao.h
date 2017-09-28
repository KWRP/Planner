//
// Created by willy on 25/06/2017.

#pragma once
#ifndef PLANNER_GUI_SQLITE_DAO_H
#define PLANNER_GUI_SQLITE_DAO_H


// To create sql executable to look at the database files use the following:
// gcc -DSQLITE_THREADSAFE=0 -DSQLITE_OMIT_LOAD_EXTENSION shell.c sqlite3.c -o sqlite

// To compile this file use:
// gcc sqlite3.c test.c -o testDb

#include <sqlite3.h>
#include <stdio.h>
#include <string.h>

#ifdef __cplusplus
extern "C"
{
#endif

bool createTableQuery(const char *filepath);

bool createDb(const char *stmt, const char *filepath);

bool insertToDb(const char *day, const char *month, const char *year, const char *title,
                const char *description, const char *start, const char *duration,
				const char *endDay, const char *endMonth, const char *endYear, int repeatCycle,
				bool byDay, const char *filepath);

bool updateToDb(const char *day, const char *month, const char *year, const char *title,
                const char *description, const char *start, const char *duration,
                const char *endDay, const char *endMonth, const char *endYear,int eventID, 
				int repeatCycle,bool byDay, const char *filepath);

bool deleteFromDb(int eventID, const char *filepath);

bool selectFromDB(const char *sday,const char *smonth,const char* syear,
				   const char *eday,const char *emonth,const char* eyear, const char *filepath);

bool displayDb(const char *nativeDb);

#endif //PLANNER_GUI_SQLITE_DAO_H
#ifdef __cplusplus
}
#endif
