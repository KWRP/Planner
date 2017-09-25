
// To create sql executable to look at the database files use the following:
// gcc -DSQLITE_THREADSAFE=0 -DSQLITE_OMIT_LOAD_EXTENSION shell.c sqlite3.c -o sqlite

// To compile this file use:
// gcc sqlite3.c test.c -o testDb

#include <sqlite3.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include "include/sqlite-dao.h"

static int callback(void *NotUsed, int argc, char **argv, char **azColName) {
    int i;
    for (i = 0; i < argc; ++i) {
        printf("%s = %s\n", azColName[i], argv[i] ? argv[i] : "NULL");
        //printf("info:: azColName = %s ; argv = %s \n", azColName[i], argv[i]);
    }
    printf("\n");
    return 0;
}

bool createTableQuery(const char *filepath) {

    const char *createWeeklyQuery = "CREATE TABLE IF NOT EXISTS WEEKLY(weeklyID AUTOINCREMENT NOT NULL, monday Boolean NOT NULL,"
            "tuesday Boolean NOT NULL,"
            "wenesday Boolean NOT NULL,"
            "thursday Boolean NOT NULL,"
            "friday Boolean NOT NULL,"
            "saturday Boolean NOT NULL,"
            "sunday Boolean NOT NULL,"
            "PRIMARY KEY (weeklyID));";

    const char *createCALENDARQuery = "CREATE TABLE IF NOT EXISTS CALENDAR("
            "eventID AUTOINCREMENT NOT NULL,"
            "date DATE NOT NULL,"
            "title text NOT NULL,"
            "description text NOT NULL,"
            "start time NOT NULL,"
            "duration int NOT NULL, "
            "repeatCycle int NOT NULL,"
            "endDate date NOT NULL,"
            "weeklyID NOT NULL, "
            "PRIMARY KEY (eventID)),"
            "FOREIGN KEY(weeklyID) REFERENCES WEEKLY(weeklyID)) ON DELETE CASCADE;";

    bool weeklyResult = createDb(createWeeklyQuery, filepath);
    bool calendarResult = createDb(createCALENDARQuery, filepath);

    if (!weeklyResult || !calendarResult)
        return false;
    return true;
}

bool createDb(const char *createStatment, const char *filepath) {
    /* Open database */
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;
    rc = sqlite3_open(filepath, &db);

    rc = sqlite3_exec(db, createStatment, callback, 0, &zErrMsg);
    if (rc != SQLITE_OK) {
        sqlite3_free(zErrMsg);
        return false;
    }
    sqlite3_close(db);
    return true;
}

bool insertToDb(const char *date, const char *title, const char *description, const char *start,
                const char *duration,
                int repeatCycle, const char *endDate, bool repeats[7], const char *filepath) {
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc, i, j;
    int repeater = repeats;
    sqlite3_stmt *stmt;

    /* Open database */
    rc = sqlite3_open(filepath, &db);

    char *insertQueryWeekly = "INSERT INTO WEEKLY(monday,tuesday,wednesday,thursday,friday,saturday,sunday) VALUES(?,?,?,?,?,?,?);";
    char *insertQueryCalendar = "INSERT INTO CALENDAR(date,title,description,start,duration,repeatCycle,endDate,weeklyID) VALUES(?,?,?,?,?,?,?,?,?,last_insert_rowid());";

    printf("Query %s\n", insertQueryWeekly);
    printf("Query %s\n", insertQueryCalendar);

    rc = sqlite3_prepare_v2(db, insertQueryWeekly, -1, &stmt, 0);
    if (rc == SQLITE_OK) {

        for (i = 0; i < 7; i++) {
            int days = sqlite3_bind_int(stmt, i + 1, repeats[i]);
        }

    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(db));
    }

    sqlite3_step(stmt);
    sqlite3_reset(stmt);

    rc = sqlite3_prepare_v2(db, insertQueryCalendar, -1, &stmt, 0);
    if (rc == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, date, strlen(date), 0);
        sqlite3_bind_text(stmt, 2, title, strlen(title), 0);
        sqlite3_bind_text(stmt, 3, description, strlen(description), 0);
        sqlite3_bind_text(stmt, 4, start, strlen(start), 0);
        sqlite3_bind_text(stmt, 5, duration, strlen(duration), 0);
        sqlite3_bind_text(stmt, 6, repeatCycle, strlen(repeatCycle), 0);
        sqlite3_bind_text(stmt, 7, endDate, strlen(endDate), 0);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(db));
    }
    sqlite3_step(stmt);
    sqlite3_finalize(stmt);

    sqlite3_close(db);
    return true;
}

bool updateToDb(const char *date, const char *title, const char *description, const char *start,
                const char *duration,
                int repeatCycle, const char *endDate, const char *finish, bool repeats[7],
                int eventID,
                const char *filepath) {
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;
    bool result;
    sqlite3_stmt *stmt;

    /* Open database */
    rc = sqlite3_open(filepath, &db);

    char *updateQuery = "UPDATE CALENDAR SET(endDate) VALUES(?) Where eventID = ?;";
    printf("Query: %s\n", updateQuery);

    rc = sqlite3_prepare_v2(db, updateQuery, -1, &stmt, 0);
    if (rc == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, date, strlen(date), 0);
        sqlite3_bind_text(stmt, 2, eventID, strlen(eventID), 0);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(db));
    }
    sqlite3_step(stmt);
    sqlite3_finalize(stmt);
    sqlite3_close(db);
    return insertToDb(date, title, description, start, duration, repeatCycle, endDate, repeats,
                      filepath);
}

bool deleteFromDb(int eventID, const char *filepath) {
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;
    sqlite3_stmt *stmt;

    /* Open database */
    rc = sqlite3_open(filepath, &db);

    char *deleteQuery = "DELETE FROM CALENDAR WHERE eventID = ?;";
    printf("Queury: %s\n", deleteQuery);

    rc = sqlite3_prepare_v2(db, deleteQuery, -1, &stmt, 0);

    if (rc == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, eventID, strlen(eventID), 0);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(db));
    }

    sqlite3_step(stmt);
    sqlite3_finalize(stmt);
    sqlite3_close(db);
    return true;
}

bool selectFromDB(const char *start, const char *end, bool repeats[7], const char *filepath) {
    printf("Database table:\n");
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;
    sqlite3_stmt *stmt;

    /* Open database */
    rc = sqlite3_open(filepath, &db);
//    string selectedDate = nativeDate;
//    string delimiter = "/";
//    string day = selectedDate.substr(0, selectedDate.find(delimiter));
//    string rest = selectedDate.substr(selectedDate.find(delimiter) + 1);
//    string month = rest.substr(0, selectedDate.find(delimiter));
//    string year = rest.substr(selectedDate.find(delimiter) + 1);


    const char *selectQuery = "select * from CALENDAR "
            "join weeklyID on CALENDAR.weeklyID = WEEKLY.weeklyID "
            "where CALENDAR.date = (?) and CALENDAR.endDate = (?) "
            "and CALENDAR.monday = (?) "
            "and CALENDAR.tuesday = (?) "
            "and CALENDAR.wednesday = (?) "
            "and CALENDAR.thursday = (?) "
            "and CALENDAR.friday = (?) "
            "and CALENDAR.saturday = (?) "
            "and CALENDAR.sunday = (?) ;";

    printf("Queury: %s\n\n", selectQuery);

    rc = sqlite3_prepare_v2(db, selectQuery, -1, &stmt, 0);
    if (rc == SQLITE_OK) {
        int binddate = sqlite3_bind_text(stmt, 1, start, strlen(start), 0);
        int bindenddate = sqlite3_bind_text(stmt, 2, end, strlen(end), 0);;
        int i;
        for (i = 0; i < 7; i++) {
            int days = sqlite3_bind_int(stmt, i + 3, repeats[i]);
        }
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(db));
    }

    while (1) {

        int s = sqlite3_step(stmt);
        if (s == SQLITE_ROW) {
            int bytes;
            const unsigned char *text;
            bytes = sqlite3_column_bytes(stmt, 0);
            text = sqlite3_column_text(stmt, 0);
            printf("%d: %s\n", bytes, text);
        } else if (s == SQLITE_DONE) {
            sqlite3_finalize(stmt);
            sqlite3_close(db);
            break;
        } else {
            fprintf(stderr, "Failed.\n");
            return (1);
        }
    }
    return true;
}


bool displayDb(const char *filepath) {
    printf("Database table:\n");
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;

    /* Open database */
    rc = sqlite3_open(filepath, &db);

    const char *selectQuery = "select * from calendar join weeklyID on calendar.weeklyID = weekly.weeklyID;";
    printf("Queury: %s\n\n", selectQuery);

    /* Execute SQL statement */
    rc = sqlite3_exec(db, selectQuery, callback, 0, &zErrMsg);

    if (rc != SQLITE_OK) {
        sqlite3_free(zErrMsg);
    }
    sqlite3_close(db);
    printf("\n\n");
    return true;
}