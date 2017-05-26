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
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "createDate: date invalid");
        return false;
    } else if (datecheck) {
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "createDate: date already exists");
        return true;
    } else {
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!",
                            "createDate: All good create new date");


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

    XMLPrinter printer;
    xmlDoc.Accept(&printer);
    const char *xmlcstr = printer.CStr();
    __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "%s", xmlcstr);

    eResult = xmlDoc.SaveFile(filePath);

    XMLCheckResult(eResult);
    return true;
}

bool createXml(const char *filePath) {

    XMLDocument *xmlDoc;
    xmlDoc = new XMLDocument();

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
    XMLError eResult = xml_doc.LoadFile(filePath);
    if (eResult != XML_SUCCESS) return false;

    bool newDate = createDate(day, month, year);

    if (newDate) {
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "addEvent: newDate was true");

        XMLElement *elementYear = xml_doc.FirstChildElement("planner")->FirstChildElement("year");
        XMLElement *elementMonth = elementYear->FirstChildElement("month");
        XMLElement *elementDay = elementMonth->FirstChildElement("day");
        XMLElement *elementEvent = elementDay->FirstChildElement("event");
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "addEvent: before year loop");
        while (!(elementYear->Attribute("YID", year))) {
            elementYear = elementYear->NextSiblingElement("year");
        }
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "addEvent: before month loop");
        while (!(elementMonth->Attribute("MID", month))) {
            elementMonth = elementMonth->NextSiblingElement("month");
        }
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "addEvent: before day loop");
        while (!(elementDay->Attribute("DID", day))) {
            elementDay = elementDay->NextSiblingElement("day");
        }

        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "addEvent: add new event!");
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

        XMLPrinter printer;
        xml_doc.Accept(&printer);
        const char *xmlcstr = printer.CStr();
        __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "%s", xmlcstr);


        eResult = xml_doc.SaveFile(filePath);
        XMLCheckResult(eResult);
        return true;
    }
    __android_log_print(ANDROID_LOG_INFO, "TEST C++!!!", "addEvent: createDate was false!");
    eResult = xml_doc.SaveFile(filePath);
    return false;
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

