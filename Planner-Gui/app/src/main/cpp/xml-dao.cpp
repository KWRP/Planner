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

    // Print xml -------------------------------------

    XMLPrinter printer;
    xmlDoc.Accept(&printer);
    const char *xmlcstr = printer.CStr();
    __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "%s", xmlcstr);

    // -----------------------------------------------

    // find year
    elementYear = xmlDoc.FirstChildElement("planner")->FirstChildElement("year");
    while (!(elementYear->Attribute("YID", year))) {
        elementYear = elementYear->NextSiblingElement("year");
        if (elementYear == nullptr) return false;
    }

    //find month
    elementMonth = elementYear->FirstChildElement("month");
    while (!(elementMonth->Attribute("MID", month))) {
        elementMonth = elementMonth->NextSiblingElement("month");
        if (elementMonth == nullptr) return false;
    }

    // find day
    elementDay = elementMonth->FirstChildElement("day");
    while (!(elementDay->Attribute("DID", day))) {
        elementDay = elementDay->NextSiblingElement("day");
        if (elementDay == nullptr) return false;
    }


    eResult = xmlDoc.SaveFile(filePath);
    XMLCheckResult(eResult);
    return true;
}

bool createDate(const char *filePath, const char *day, const char *month, const char *year) {

    XMLElement *elementYear;
    XMLElement *elementMonth;
    XMLElement *elementDay;

    XMLDocument xmlDoc;
    XMLError eResult = xmlDoc.LoadFile(filePath);
    if (eResult != XML_SUCCESS) return false;
    int yearInt = atoi(year);
    int monthInt = atoi(month);
    int dayInt = atoi(day);
    bool datecheck = checkDate(day, month, year);

    if (yearInt <= 0 || monthInt <= 0 || monthInt > 12 || dayInt <= 0 || dayInt > 31) {
        return false;
    } else if (datecheck) {
        return true;
    } else {

        XMLElement *rootNode = xmlDoc.FirstChildElement("planner");

        // find year
        elementYear = xmlDoc.FirstChildElement("planner")->FirstChildElement("year");
        if (elementYear == nullptr) {
            elementYear = xmlDoc.NewElement("year");
            elementYear->SetAttribute("YID", year);
            rootNode->InsertEndChild(elementYear);
        }
        while (!(elementYear->Attribute("YID", year))) {
            elementYear = elementYear->NextSiblingElement("year");
            if (elementYear == nullptr) {
                elementYear = xmlDoc.NewElement("year");
                elementYear->SetAttribute("YID", year);
                rootNode->InsertEndChild(elementYear);
            }
        }

        //find month
        elementMonth = elementYear->FirstChildElement("month");
        if (elementMonth == nullptr) {
            elementMonth = xmlDoc.NewElement("month");
            elementMonth->SetAttribute("MID", month);
            elementYear->InsertEndChild(elementMonth);
        }
        while (!(elementMonth->Attribute("MID", month))) {
            elementMonth = elementMonth->NextSiblingElement("month");
            if (elementMonth == nullptr) {
                elementMonth = xmlDoc.NewElement("month");
                elementMonth->SetAttribute("MID", month);
                elementYear->InsertEndChild(elementMonth);
            }
        }

        // find day
        elementDay = elementMonth->FirstChildElement("day");
        if (elementDay == nullptr) {
            elementDay = xmlDoc.NewElement("day");
            elementDay->SetAttribute("DID", day);
            elementMonth->InsertEndChild(elementDay);
        }
        while (!(elementDay->Attribute("DID", day))) {
            elementDay = elementDay->NextSiblingElement("day");
            if (elementDay == nullptr) {
                elementDay = xmlDoc.NewElement("day");
                elementDay->SetAttribute("DID", day);
                elementMonth->InsertEndChild(elementDay);
            }
        }

    }
    eResult = xmlDoc.SaveFile(filePath);

    XMLCheckResult(eResult);
    return true;
}

bool createXml(const char *filePath) {

    XMLDocument *xmlDoc;

    XMLError eResult = xmlDoc->LoadFile(filePath);
    if (eResult != XML_SUCCESS) {
        xmlDoc = new XMLDocument();
        XMLNode *planner = xmlDoc->NewElement("planner");
        xmlDoc->InsertFirstChild(planner);
        XMLError eResult = xmlDoc->SaveFile(filePath);
        XMLCheckResult(eResult);
    }
    return true;
}

bool addEvent(const char *filePath, const char *day, const char *month, const char *year,
              const char *title, const char *description, const char *startTime,
              const char *duration) {

    bool newDate = createDate(filePath, day, month, year);

    XMLDocument xmlDoc;
    XMLError eResult = xmlDoc.LoadFile(filePath);
    if (eResult != XML_SUCCESS) return false;

    if (newDate) {
        XMLElement *elementYear = xmlDoc.FirstChildElement("planner")->FirstChildElement("year");
        while (elementYear != nullptr && !(elementYear->Attribute("YID", year))) {
            elementYear = elementYear->NextSiblingElement("year");
        }


        XMLElement *elementMonth = elementYear->FirstChildElement("month");
        while (elementMonth != nullptr && !(elementMonth->Attribute("MID", month))) {
            elementMonth = elementMonth->NextSiblingElement("month");
        }

        XMLElement *elementDay = elementMonth->FirstChildElement("day");
        while (elementDay != nullptr && !(elementDay->Attribute("DID", day))) {
            elementDay = elementDay->NextSiblingElement("day");
        }

        int eventNum = 0;
        XMLElement *firstEvent = elementDay->FirstChildElement("event");
        if (firstEvent == nullptr) {
            eventNum = 0;
        }else {
            eventNum = elementDay->LastChildElement("event")->IntAttribute("EID");
            eventNum += 1;
        }

        XMLElement *elementEvent = xmlDoc.NewElement("event");

        elementEvent->SetAttribute("EID", eventNum);

        XMLElement *elementTit = xmlDoc.NewElement("title");
        elementTit->SetText(title);
        elementEvent->InsertEndChild(elementTit);

        XMLElement *elementDesc = xmlDoc.NewElement("description");
        elementDesc->SetText(description);
        elementEvent->InsertEndChild(elementDesc);

        XMLElement *elementST = xmlDoc.NewElement("startTime");
        elementST->SetText(startTime);
        elementEvent->InsertEndChild(elementST);

        XMLElement *elementDur = xmlDoc.NewElement("duration");
        elementDur->SetText(duration);
        elementEvent->InsertEndChild(elementDur);

        elementDay->InsertEndChild(elementEvent);

        eResult = xmlDoc.SaveFile(filePath);
        XMLCheckResult(eResult);
        return true;
    }
    eResult = xmlDoc.SaveFile(filePath);
    XMLCheckResult(eResult);
    return false;
}

bool
pullDay(Day *dayObj, const char *filepath, const char *day, const char *month, const char *year) {

    XMLDocument xmlDoc;

    XMLError eResult = xmlDoc.LoadFile(filepath);
    if (eResult != XML_SUCCESS) return false;

    bool dateExist = checkDate(filepath, day, month, year);

    if (!dateExist){
        return false;
    }

    XMLElement *elementYear = xmlDoc.FirstChildElement("planner")->FirstChildElement("year");
    while (elementYear != nullptr && !(elementYear->Attribute("YID", year))) {
        elementYear->NextSiblingElement("year");
    }

    XMLElement *elementMonth = elementYear->FirstChildElement("month");
    while (elementMonth != nullptr && !(elementMonth->Attribute("MID", month))) {
        elementMonth = elementMonth->NextSiblingElement("month");
    }


    XMLElement *elementDay = elementMonth->FirstChildElement("day");
    while (elementDay != nullptr && !(elementDay->Attribute("DID", day))) {
        elementDay = elementDay->NextSiblingElement("day");
    }


    XMLElement* elementEventCurr = elementDay->FirstChildElement("event");

    if (elementEventCurr == nullptr)return false;

    int eventCurr = elementDay->FirstChildElement("event")->IntAttribute("EID");
    int eventLast = elementDay->LastChildElement("event")->IntAttribute("EID");

    while (eventCurr <= eventLast) {

        const char* title = elementEventCurr->FirstChildElement("title")->GetText();
        const char* description = elementEventCurr->FirstChildElement("description")->GetText();
        const char* startTime = elementEventCurr->FirstChildElement("startTime")->GetText();
        const char* duration = elementEventCurr->FirstChildElement("duration")->GetText();

        dayObj->setEvent(eventCurr,title,description,atoi(startTime),atoi(duration));
        elementEventCurr = elementEventCurr->NextSiblingElement("event");

        if (elementEventCurr == nullptr) {
            break;
        }
        eventCurr = elementEventCurr->IntAttribute("EID");

    }

    eResult = xmlDoc.SaveFile(filepath);
    XMLCheckResult(eResult);

    return true;
}

