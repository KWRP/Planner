
// To create sql executable to look at the database files use the following:
// gcc -DSQLITE_THREADSAFE=0 -DSQLITE_OMIT_LOAD_EXTENSION shell.c sqlite3.c -o sqlite

// To compile this file use:
// gcc sqlite3.c test.c -o testDb.

#include <sqlite3.h>
#include <sqlite-dao.hpp>
#include <helpers.hpp>
#include <android/log.h>

/**
 * Logs each line of a SQL execution
 *
 * @param null pointer
 * @param argc for number of lines to log
 * @param argv is a character pointer for argument values
 * @param azColName is the name of the columns in the database
 * @return 0 once complete
 */
int callback(void *NotUsed, int argc, char **argv, char **azColName) {
    int i;
    __android_log_print(ANDROID_LOG_INFO, "TEST SELECT SQL!!!", "%s", "got into callback");
    for (i = 0; i < argc; ++i) {
        __android_log_print(ANDROID_LOG_INFO, "TEST Print DATABASE!!!", "%s = %s\n", azColName[i],
                            argv[i] ? argv[i] : "NULL");
    }
    printf("\n");
    return 0;
}
/**
 * Creates the initial database if not already created.
 *
 * @param filepath of the stored database
 * @return bool value of database creation
 */
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
/**
 * Creates the initial database if not already created.
 *
 * @param createStatement is the statement to be executed
 * @param db is the opened instance of the database to execute the statement in
 * @return bool value of database creation
 */
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
/**
 * Inserts an event into the database
 *
 * @param day portion of the start date
 * @param month portion of the start date
 * @param year portion of the start date
 * @param title of the event
 * @param description for the event
 * @param start time of the event
 * @param duration of the event
 * @param endDay portion of the end date
 * @param endMonth portion of the end date
 * @param endYear portion of the end date
 * @param repeatCycle of the event (0-4)
 * @param filepath of the stored database
 * @return bool value of database creation
 */
bool insertToDb(const char *day, const char *month, const char *year, const char *title,
                const char *description, const char *start, const char *duration,
                const char *endDay, const char *endMonth, const char *endYear,
                int repeatCycle, const char *filepath) {


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
    __android_log_print(ANDROID_LOG_INFO, "TEST update MONTH SELECT SQL!!!", "%s %s %s %d", day,
                        month, year, repeatCycle);

    const char *insertQueryWeekly = "INSERT INTO WEEKLY(monday,tuesday,wednesday,thursday,friday,saturday,"
            "sunday,weekID) VALUES(?,?,?,?,?,?,?,null);";
    const char *insertQueryCalendar = "INSERT INTO CALENDAR(startDate,title,description,start,"
            "duration,repeatCycle,endDate,eventID,weekID) "
            "VALUES(date(?),?,?,?,?,?,date(?),null,last_insert_rowid());";


    dayNum = dayOfWeekI(atoi(day), atoi(month), atoi(year));

    rc = sqlite3_prepare_v2(db, insertQueryWeekly, -1, &stmt, 0);
    if (rc == SQLITE_OK) {
        if (repeatCycle == 2) {
            for (i = 0; i < 7; i++) {
                if (dayNum == i) {
                    sqlite3_bind_int(stmt, i + 1, 0);
                } else {
                    sqlite3_bind_int(stmt, i + 1, 1);
                }
            }
        } else {
            for (i = 0; i < 7; i++) {
                sqlite3_bind_int(stmt, i + 1, 0);
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
/**
 * updates the end date of an already existing event, then recalls insert for the new event.
 *
 * @param day portion of the start date of the new event,
 *          used to derive the end date of the old event
 * @param month portion of the start date of the new event,
 *          used to derive the end date of the old event
 * @param year portion of the start date of the new event,
 *          used to derive the end date of the old event
 * @param title of the event
 * @param description for the event
 * @param start time of the event
 * @param duration of the event
 * @param endDay portion of the end date
 * @param endMonth portion of the end date
 * @param endYear portion of the end date
 * @param repeatCycle of the event (0-4)
 * @param filepath of the stored database
 * @return bool value of database creation
 */
bool updateToDb(const char *sday, const char *smonth, const char *syear, const char *title,
                const char *description, const char *start, const char *duration,
                const char *endDay, const char *endMonth, const char *endYear,
                int eventID, int repeatCycle, const char *filepath) {
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;
    sqlite3_stmt *stmt;
    const char *days[31] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                            "13", "14", "15", "16",
                            "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
                            "29", "30", "31"};

    const char *months[31] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                              "12"};

    const char *day;
    const char *month;

    /* Open database */
    rc = sqlite3_open(filepath, &db);
    if (rc != SQLITE_OK) {
        sqlite3_free(zErrMsg);
        return false;
    }
    __android_log_print(ANDROID_LOG_INFO, "TEST update MONTH SELECT SQL!!!", "%s %s %s",sday,smonth,syear);
    __android_log_print(ANDROID_LOG_INFO, "TEST update MONTH SELECT SQL!!!", "%s %s %s",endDay,endMonth,endYear);

    if (atoi(sday) == 1) {
        if (atoi(smonth) > 1) {
            day = "31";
        } else {
            day = "0"; //may break database, must test
        }
        month = months[atoi(smonth)];
    } else {
        day = days[atoi(sday)-2];
        month = smonth;
    }

    char startDate[11];

    strcpy(startDate, syear);
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
    return insertToDb(sday, smonth, syear, title, description, start, duration,
                      endDay, endMonth, endYear, repeatCycle, filepath);
}
/**
 * Removes a database entry with the given ID
 *
 * @param eventID is the ID of the desired event to be deleted
 * @param filepath of the stored database
 * @return bool value of database creation
 */
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
/**
 * Selects and returns all events that occur on a given date.
 *
 * @param day portion of date
 * @param month portion of date
 * @param year portion of date
 * @param filepath of the stored database
 * @return vector of strings with the information of each event
 */
std::vector<std::string> selectFromDB(const char *day, const char *month,
                                                   const char *year,
                                                   const char *filepath) {
    const char* days[7] = {"WEEKLY.monday", "WEEKLY.tuesday", "WEEKLY.wednesday",
                           "WEEKLY.thursday", "friday", "WEEKLY.saturday", "WEEKLY.sunday"};

    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;
    sqlite3_stmt *stmt;
    std::vector<std::string> events;

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

    std::string query = "select * from CALENDAR "
            "join WEEKLY on CALENDAR.weekID = WEEKLY.weekID "
            "where CALENDAR.startDate <= date( ? ) "
            "and CALENDAR.endDate >= date( ? ) and ";
    query.append(days[dayNum]);
    query.append(" = 0;");

    rc = sqlite3_prepare_v2(db, query.c_str(), -1, &stmt, 0);
    if (rc == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, eventDate, (int) strlen(eventDate), 0);
        sqlite3_bind_text(stmt, 2, eventDate, (int) strlen(eventDate), 0);
    } else {
        __android_log_print(ANDROID_LOG_INFO, "TEST SELECT SQL!!!", "%s", sqlite3_errmsg(db));
    }

    while (true) {
        int s = sqlite3_step(stmt);
        if (s == SQLITE_ROW) {
            std::string eventC = "";
            const unsigned char *startDate = sqlite3_column_text(stmt, 0);
            const unsigned char *title = sqlite3_column_text(stmt, 1);
            const unsigned char *description = sqlite3_column_text(stmt, 2);
            const unsigned char *start = sqlite3_column_text(stmt, 3);
            const unsigned char *duration = sqlite3_column_text(stmt, 4);
            const unsigned char *repeat = sqlite3_column_text(stmt, 5);
            const unsigned char *endDate = sqlite3_column_text(stmt, 6);
            const unsigned char *eventID = sqlite3_column_text(stmt, 7);
            __android_log_print(ANDROID_LOG_INFO, "TEST SELECT SQL!!!",
                                "\nStartDate: %s\n"
                                        "Title: %s\n"
                                        "Description: %s\n"
                                        "Start Time: %s\n"
                                        "Duration: %s\n"
                                        "End Date: %s\n"
                                        "EventID: %s\n"
                                        "repeatCycle: %s\n",
                                startDate, title, description, start, duration, endDate, eventID,repeat);

            eventC.append((std::string) (char *) startDate).append("__");
            eventC.append((std::string) (char *) title).append("__");
            eventC.append((std::string) (char *) description).append("__");
            eventC.append((std::string) (char *) start).append("__");
            eventC.append((std::string) (char *) duration).append("__");
            eventC.append((std::string) (char *) endDate).append("__");
            eventC.append((std::string) (char *) eventID).append("__");


            std::string selectedDate = (std::string) (char *) startDate;
            std::string yearstr = selectedDate.substr(0, 4);
            std::string monthstr = selectedDate.substr(5, 2);
            std::string daystr = selectedDate.substr(8, 2);

            const char *eday = daystr.c_str();
            const char *emonth = monthstr.c_str();
            const char *eyear = yearstr.c_str();

            __android_log_print(ANDROID_LOG_INFO, "TEST SELECT SQL!!!", "DATE IS: %s\n"
                                        "eday is: %s to %d\n"
                                        "day is: %d\n"
                                        "month is %s to %d\n"
                                        "year is %s to %d\n"
                                        "repeatCycle is %d\n",
                                selectedDate.c_str(), eday, atoi(eday), atoi(day), emonth,
                                atoi(month),
                                eyear, atoi(eyear), atoi((char *) repeat));

            int lastDay = maxDays(atoi(month),atoi(year));
            if (atoi((char *) repeat) == 3) {//monthly
                if(atoi(eday) == atoi(day)){
                    events.emplace_back(eventC);
                }else if(atoi(eday) > lastDay && atoi(day)== lastDay){
                    events.emplace_back(eventC);
                }
            } else if (atoi((char *) repeat) == 4) {//yearly
                if(atoi(eday) == atoi(day) && atoi(emonth) == atoi(month)){
                    events.emplace_back(eventC);
                }else if(atoi(emonth) == atoi(month) && atoi(eday) > lastDay && atoi(day) == lastDay ){
                    events.emplace_back(eventC);
                }
            }else{//weekly daily never
                events.emplace_back(eventC);
            }

        } else if (s == SQLITE_DONE) {
            sqlite3_finalize(stmt);
            sqlite3_close(db);
            break;
        } else {
            fprintf(stderr, "Failed.\n");
            __android_log_print(ANDROID_LOG_INFO, "TEST SELECT SQL!!!", "%s", "Failed to read row");
            sqlite3_close(db);
            break;
        }
    }
    return events;

}

/**
 * logs the entire database
 *
 * @param filepath of the stored database
 * @return bool value of database manipulations
 */
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
/**
 * Calls select for each day of a given month in a given year
 *
 * @param month to select from
 * @param year of the given month
 * @param filepath of the stored database
 * @return bool value of database manipulations
 */
std::string selectMonth(const char *month, const char *year, const char *filepath){
    int lastDay = maxDays(atoi(month),atoi(year));
    const char* days[31] = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16",
                            "17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    std::string result = "";
    std::string monthT = "";
    if(strlen(month)<=1){
        monthT.append("0");
    }
    monthT.append(month);


    for(int j =0; j<lastDay; j++){
        std::vector<std::string> events = selectFromDB(days[j],monthT.c_str(),year,filepath);
        if(events.size() > 0){
            result.append(days[j]).append("__");
        }
    }

    return result;
}