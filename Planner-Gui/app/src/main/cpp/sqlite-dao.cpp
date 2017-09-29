
// To create sql executable to look at the database files use the following:
// gcc -DSQLITE_THREADSAFE=0 -DSQLITE_OMIT_LOAD_EXTENSION shell.c sqlite3.c -o sqlite

// To compile this file use:
// gcc sqlite3.c test.c -o testDb
#include <sqlite3.h>
#include <sqlite-dao.hpp>
#include <time.h>
#include <stdlib.h>
#include <android/log.h>

int dayOfWeekI(int day, int month, int year) {

    struct tm time_in = {0, 0, 0, // second, minute, hour
                         day, month - 1,
                         year - 1900}; // 1-based day, 0-based month, year since 1900
    time_t time_temp = mktime(&time_in);

    struct tm const *time_out = localtime(&time_temp);
    int n = time_out->tm_wday + 6;
    n = n % 7;
    return n;
}

int callback(void *NotUsed, int argc, char **argv, char **azColName) {
    NotUsed = 0;
    int i;
    __android_log_print(ANDROID_LOG_INFO, "TEST SELECT SQL!!!", "%s", "got into callback");
    for (i = 0; i < argc; ++i) {
        //printf("%s = %s\n", azColName[i], argv[i] ? argv[i] : "NULL");
        //printf("info:: azColName = %s ; argv = %s \n", azColName[i], argv[i]);
        __android_log_print(ANDROID_LOG_INFO, "TEST Print DATABASE!!!", "%s = %s\n", azColName[i],
                            argv[i] ? argv[i] : "NULL");
    }
    printf("\n");
    return 0;
}

bool createTableQuery(const char *filepath) {
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;
    rc = sqlite3_open(filepath, &db);

    if (rc != SQLITE_OK) {
        sqlite3_free(zErrMsg);
        return false;
    }

    const char *createWeeklyQuery = "CREATE TABLE IF NOT EXISTS WEEKLY("
            "monday Boolean NOT NULL,"
            "tuesday Boolean NOT NULL,"
            "wednesday Boolean NOT NULL,"
            "thursday Boolean NOT NULL,"
            "friday Boolean NOT NULL,"
            "saturday Boolean NOT NULL,"
            "sunday Boolean NOT NULL,"
            "weekID INTEGER NOT NULL,"
            "PRIMARY KEY(weekID));";

    const char *createCALENDARQuery = "CREATE TABLE IF NOT EXISTS CALENDAR("
            "startDate TEXT NOT NULL,"
            "title TEXT NOT NULL,"
            "description TEXT NOT NULL,"
            "start TEXT NOT NULL,"
            "duration TEXT NOT NULL,"
            "repeatCycle INTEGER NOT NULL,"
            "endDate TEXT NOT NULL,"
            "eventID INTEGER NOT NULL,"
            "weekID INTEGER NOT NULL,"
            "PRIMARY KEY(eventID),"
            "FOREIGN KEY(weekID) REFERENCES WEEKLY(weekID) ON DELETE CASCADE);";

    bool weeklyResult = createDb(createWeeklyQuery, db);
    bool calendarResult = createDb(createCALENDARQuery, db);


    sqlite3_close(db);
    if (!weeklyResult || !calendarResult)
        return false;
    return true;
}

bool createDb(const char *createStatement, sqlite3 *db) {
    char *zErrMsg = 0;
    int rc;


    rc = sqlite3_exec(db, createStatement, callback, 0, &zErrMsg);
    if (rc != SQLITE_OK) {
        sqlite3_free(zErrMsg);
        return false;
    }

    return true;
}

bool insertToDb(const char *day, const char *month, const char *year, const char *title,
                const char *description, const char *start, const char *duration,
                const char *endDay, const char *endMonth, const char *endYear,
                int repeatCycle, bool byDay, const char *filepath) {


    __android_log_print(ANDROID_LOG_INFO, "TEST Print DATABASE!!!", "%s", filepath);
    createTableQuery(filepath);
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc, i;
    int dayNum;
    sqlite3_stmt *stmt;

    /* Open database */
    rc = sqlite3_open(filepath, &db);
    if (rc != SQLITE_OK) {
        sqlite3_free(zErrMsg);
        return false;
    }

    const char *insertQueryWeekly = "INSERT INTO WEEKLY(monday,tuesday,wednesday,thursday,friday,saturday,"
            "sunday,weekID) VALUES(?,?,?,?,?,?,?,null);";
    const char *insertQueryCalendar = "INSERT INTO CALENDAR(startDate,title,description,start,"
            "duration,repeatCycle,endDate,eventID,weekID) "
            "VALUES(date(?),?,?,?,?,?,date(?),null,last_insert_rowid());";


    dayNum = dayOfWeekI(atoi(day), atoi(month), atoi(year));

    rc = sqlite3_prepare_v2(db, insertQueryWeekly, -1, &stmt, 0);
    if (rc == SQLITE_OK) {
        if (byDay || repeatCycle == 2) {
            for (i = 0; i < 7; i++) {
                if (dayNum == i) {
                    sqlite3_bind_int(stmt, i + 1, true);
                } else {
                    sqlite3_bind_int(stmt, i + 1, false);
                }
            }
        } else {
            for (i = 0; i < 7; i++) {
                sqlite3_bind_int(stmt, i + 1, true);
            }
        }
    } else {
        __android_log_print(ANDROID_LOG_INFO, "TEST Print DATABASE!!!", "%s", sqlite3_errmsg(db));
    }

    sqlite3_step(stmt);
    sqlite3_reset(stmt);

    char startDate[11];
    strcpy(startDate, year);
    strcat(startDate, "-");
    strcat(startDate, month);
    strcat(startDate, "-");
    strcat(startDate, day);

    char endDate[11];
    strcpy(endDate, endYear);
    strcat(endDate, "-");
    strcat(endDate, endMonth);
    strcat(endDate, "-");
    strcat(endDate, endDay);

    printf("start date: %s\n", startDate);
    printf("end date: %s\n", endDate);

    rc = sqlite3_prepare_v2(db, insertQueryCalendar, -1, &stmt, 0);
    if (rc == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, startDate, (int) strlen(startDate), 0);
        sqlite3_bind_text(stmt, 2, title, (int) strlen(title), 0);
        sqlite3_bind_text(stmt, 3, description, (int) strlen(description), 0);
        sqlite3_bind_text(stmt, 4, start, (int) strlen(start), 0);
        sqlite3_bind_text(stmt, 5, duration, (int) strlen(duration), 0);
        sqlite3_bind_int(stmt, 6, repeatCycle);
        sqlite3_bind_text(stmt, 7, endDate, (int) strlen(endDate), 0);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(db));
    }
    sqlite3_step(stmt);
    sqlite3_finalize(stmt);

    sqlite3_close(db);
    return true;
}

bool updateToDb(const char *day, const char *month, const char *year, const char *title,
                const char *description, const char *start, const char *duration,
                const char *endDay, const char *endMonth, const char *endYear,
                int eventID, int repeatCycle, bool byDay, const char *filepath) {
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;
    sqlite3_stmt *stmt;

    /* Open database */
    rc = sqlite3_open(filepath, &db);
    if (rc != SQLITE_OK) {
        sqlite3_free(zErrMsg);
        return false;
    }

    char startDate[11];
    strcpy(startDate, year);
    strcat(startDate, "-");
    strcat(startDate, month);
    strcat(startDate, "-");
    strcat(startDate, day);

    const char *updateQuery = "UPDATE CALENDAR SET endDate = ? Where eventID = ?;";

    rc = sqlite3_prepare_v2(db, updateQuery, -1, &stmt, 0);
    if (rc == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, startDate, (int) strlen(startDate), 0);
        sqlite3_bind_int(stmt, 2, eventID);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(db));
    }
    sqlite3_step(stmt);
    sqlite3_finalize(stmt);
    sqlite3_close(db);
    return insertToDb(day, month, year, title, description, start, duration,
                      endDay, endMonth, endYear, repeatCycle, byDay, filepath);
}

bool deleteFromDb(int eventID, const char *filepath) {
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;
    sqlite3_stmt *stmt;

    /* Open database */
    rc = sqlite3_open(filepath, &db);
    if (rc != SQLITE_OK) {
        sqlite3_free(zErrMsg);
        return false;
    }

    const char *deleteQuery = "DELETE FROM CALENDAR WHERE eventID = ?;";

    rc = sqlite3_prepare_v2(db, deleteQuery, -1, &stmt, 0);

    if (rc == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, eventID);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(db));
    }

    sqlite3_step(stmt);
    sqlite3_finalize(stmt);
    sqlite3_close(db);
    return true;
}

std::vector<std::vector<const unsigned char *>> selectFromDB(const char *day, const char *month,
                                                             const char *year,
                                                             const char *filepath) {
    const char *days[7] = {"WEEKLY.monday", "WEEKLY.tuesday", "WEEKLY.wednesday",
                           "WEEKLY.thursday", "WEEKLY.friday", "WEEKLY.saturday", "WEEKLY.sunday"};

    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;
    sqlite3_stmt *stmt;
    std::vector<std::vector<const unsigned char *>> events;

    /* Open database */
    rc = sqlite3_open(filepath, &db);
    if (rc != SQLITE_OK) {
        sqlite3_free(zErrMsg);
    }

    char eventDate[11];
    strcpy(eventDate, year);
    strcat(eventDate, "-");
    strcat(eventDate, month);
    strcat(eventDate, "-");
    strcat(eventDate, day);

    int dayNum = dayOfWeekI(atoi(day), atoi(month), atoi(year));
    const char *selectQuery = "select * from CALENDAR "
            "join WEEKLY on CALENDAR.weekID = WEEKLY.weekID "
            "where CALENDAR.startDate <= date( ? ) ";
    "and CALENDAR.endDate >= date( ? ) ";
    "and ? = 0 ;";

    const char *dday = days[dayNum];

    rc = sqlite3_prepare_v2(db, selectQuery, -1, &stmt, 0);
    if (rc == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, eventDate, (int) strlen(eventDate), 0);
        sqlite3_bind_text(stmt, 2, eventDate, (int) strlen(eventDate), 0);
        sqlite3_bind_text(stmt, 3, dday, (int) strlen(dday), 0);
    } else {
        __android_log_print(ANDROID_LOG_INFO, "TEST SELECT SQL!!!", "%s", sqlite3_errmsg(db));
    }

    while (true) {
        int s = sqlite3_step(stmt);
        if (s == SQLITE_ROW) {
            std::vector<const unsigned char *> eventC;
            const unsigned char *startDate = sqlite3_column_text(stmt, 0);
            const unsigned char *title = sqlite3_column_text(stmt, 1);
            const unsigned char *description = sqlite3_column_text(stmt, 2);
            const unsigned char *start = sqlite3_column_text(stmt, 3);
            const unsigned char *duration = sqlite3_column_text(stmt, 4);
            //skip repeat cycle, dont want to know it
            const unsigned char *endDate = sqlite3_column_text(stmt, 6);
            const unsigned char *eventID = sqlite3_column_text(stmt, 7);
            __android_log_print(ANDROID_LOG_INFO, "TEST SELECT SQL!!!",
                                "\nStartDate: %s\n"
                                        "Title: %s\n"
                                        "Description: %s\n"
                                        "Start Time: %s\n"
                                        "Duration: %s\n"
                                        "End Date: %s\n"
                                        "EventID: %s\n",
                                startDate, title, description, start, duration, endDate, eventID);
            eventC.emplace_back(startDate);
            eventC.emplace_back(title);
            eventC.emplace_back(description);
            eventC.emplace_back(start);
            eventC.emplace_back(duration);
            eventC.emplace_back(endDate);
            eventC.emplace_back(eventID);

            events.emplace_back(eventC);

        } else if (s == SQLITE_DONE) {
            //__android_log_print(ANDROID_LOG_INFO, "TEST SELECT SQL!!!", "%s", "finished rows");
            sqlite3_finalize(stmt);
            sqlite3_close(db);
            break;
        } else {
            fprintf(stderr, "Failed.\n");
            __android_log_print(ANDROID_LOG_INFO, "TEST SELECT SQL!!!", "%s", "Failed to read row");
            sqlite3_close(db);

        }
    }
    return events;

}


bool displayDb(const char *filepath) {
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;

    /* Open database */
    rc = sqlite3_open(filepath, &db);
    if (rc != SQLITE_OK) {

        sqlite3_free(zErrMsg);
        return false;
    }

    const char *selectQuery = "select * from calendar join weeklyID on calendar.weeklyID = weekly.weeklyID ;";
    /* Execute SQL statement */
    rc = sqlite3_exec(db, selectQuery, callback, 0, &zErrMsg);

    if (rc != SQLITE_OK) {
        sqlite3_free(zErrMsg);
    }
    sqlite3_close(db);
    printf("\n\n");
    return true;
}