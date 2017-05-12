#include "include/xml-dao.hpp"
#include "include/tinyxml2.h"
#include <android/log.h>

using namespace tinyxml2;
#ifndef XMLCheckResult
#define XMLCheckResult(a_eResult) if (a_eResult != XML_SUCCESS) { printf("Error: %i\n", a_eResult); return a_eResult; }
#endif

bool checkDate(const char *filePath, const char *day, const char *month, const char *year) {

    XMLDocument xmlDoc;
    XMLElement *elementYear;
    XMLElement *elementMonth;
    XMLElement *elementDay;

    XMLError eResult = xmlDoc.LoadFile(filePath);
    if (eResult != XML_SUCCESS) return false;

    // find year
    elementYear = xmlDoc.FirstChildElement("planner")->FirstChildElement("year");
    while (!elementYear && !(elementYear->Attribute("MID", month))) {
        elementYear = elementYear->NextSiblingElement("year");
        if (!elementYear) return false;
    }

    //find month
    elementMonth = elementYear->FirstChildElement("month");
    while (!elementMonth  && !(elementMonth->Attribute("MID", month))) {
        elementMonth = elementMonth->NextSiblingElement("month");
        if (!elementMonth) return false;
    }

    // find day
    elementDay = elementMonth->FirstChildElement("day");
    while (!elementDay && !(elementDay->Attribute("DID", day))) {
        elementDay = elementDay->NextSiblingElement("day");
        if (!elementDay) return false;
    }

    eResult = xmlDoc.SaveFile(filePath);
    XMLCheckResult(eResult);
    return true;
}

bool createDate(const char *filePath, const char *day, const char *month, const char *year) {

    XMLDocument xmlDoc;

    XMLError eResult = xmlDoc.LoadFile(filePath);
    if (eResult != XML_SUCCESS) return false;

    int yearInt = atoi(year);
    int monthInt = atoi(month);
    int dayInt = atoi(day);

    if (yearInt <= 0 || monthInt <= 0 || monthInt > 12 || dayInt <= 0 || dayInt > 31) {
        return false;
    } else if (checkDate(day, month, year)) {
        return true;
    } else {
        XMLElement *elementYear = xmlDoc.NewElement("year");
        elementYear->SetAttribute("YID", year);
        xmlDoc.InsertFirstChild(elementYear);

        XMLElement *elementMonth = xmlDoc.NewElement("month");
        elementMonth->SetAttribute("MID", month);
        elementYear->InsertEndChild(elementMonth);

        XMLElement *elementDay = xmlDoc.NewElement("day");
        elementDay->SetAttribute("DID", day);
        elementMonth->InsertEndChild(elementDay);
    }

    eResult = xmlDoc.SaveFile(filePath);

    XMLCheckResult(eResult);
    return true;
}

bool createXml(const char *filePath) {

    XMLDocument *xmlDoc;

    xmlDoc = new XMLDocument();
    //createDate("6","5","2017");
    XMLNode *planner = xmlDoc->NewElement("planner");
    xmlDoc->InsertFirstChild(planner);

    XMLElement *year = xmlDoc->NewElement("year");
    XMLElement *month = xmlDoc->NewElement("month");
    XMLElement *day = xmlDoc->NewElement("day");
    XMLElement *event = xmlDoc->NewElement("event");
    XMLElement *title = xmlDoc->NewElement("title");
    XMLElement *description = xmlDoc->NewElement("description");
    XMLElement *startTime = xmlDoc->NewElement("startTime");
    XMLElement *duration = xmlDoc->NewElement("duration");

    year->SetAttribute("YID", "2017");
    month->SetAttribute("MID", "5");
    day->SetAttribute("DID", "5");
    event->SetAttribute("EID", "1");
    title->SetText("Awesome test Event");
    description->SetText("I lied it is only an assignment!!");
    startTime->SetText("12");
    duration->SetText("1");

    planner->InsertEndChild(year);
    year->InsertEndChild(month);
    month->InsertEndChild(day);
    day->InsertEndChild(event);

    event->InsertEndChild(title);
    event->InsertEndChild(description);
    event->InsertEndChild(startTime);
    event->InsertEndChild(duration);

    XMLError eResult = xmlDoc->SaveFile(filePath);
    XMLCheckResult(eResult);
    return true;
}

bool addEvent(const char *filePath, const char *day, const char *month, const char *year,
              const char *title, const char *description, const char *startTime,
              const char *duration) {

    XMLDocument xml_doc;
    __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "test directory = %s", filePath);

    XMLError eResult = xml_doc.LoadFile(filePath);
    if (eResult != XML_SUCCESS) return false;
    //createDate(day, month, year);

    XMLElement *elementYear = xml_doc.FirstChildElement("year");
    XMLElement *elementMonth = elementYear->FirstChildElement("month");
    XMLElement *elementDay = elementMonth->FirstChildElement("day");
    XMLElement *elementEvent = elementDay->FirstChildElement("event");

    int eventNum = elementDay->LastChildElement("event")->IntAttribute("EID");
    eventNum += 1;
    elementEvent->SetAttribute("EID", eventNum);

    XMLElement *elementTit = xml_doc.NewElement("title");
    elementTit->SetText(title);
    elementEvent->InsertEndChild(elementTit);

    XMLElement *elementDesc = xml_doc.NewElement("description");
    elementDesc->SetText(description);
    elementEvent->InsertEndChild(elementDesc);

    XMLElement *elementST = xml_doc.NewElement("startTime");
    elementST->SetText(startTime);
    elementEvent->InsertEndChild(elementST);

    XMLElement *elementDur = xml_doc.NewElement("duration");
    elementDur->SetText(duration);
    elementEvent->InsertEndChild(elementDur);

    eResult = xml_doc.SaveFile(filePath);
    XMLCheckResult(eResult);
    return true;
}

bool pullDay(const char *filePath, const char *day, const char *month, const char *year) {

    XMLDocument xmlDoc;

    XMLError eResult = xmlDoc.LoadFile(filePath);
    if (eResult != XML_SUCCESS) return false;

    XMLElement *elementYear = xmlDoc.FirstChildElement("year");
    while (elementYear != nullptr && !(elementYear->Attribute("YID", year))) {
        elementYear->NextSiblingElement("year");
        if (elementYear == nullptr) return false;
    }

    XMLElement *elementMonth = elementYear->FirstChildElement("month");
    while (elementMonth != nullptr && !(elementMonth->Attribute("MID", month))) {
        elementMonth = elementMonth->NextSiblingElement("month");
        if (elementMonth == nullptr) return false;
    }

    XMLElement *elementDay = elementMonth->FirstChildElement("day");
    while (elementDay != nullptr && !(elementDay->Attribute("DID", day))) {
        elementDay = elementDay->NextSiblingElement("day");
        if (elementDay == nullptr) return false;
    }

    eResult = xmlDoc.SaveFile(filePath);
    XMLCheckResult(eResult);
    return true;
}

