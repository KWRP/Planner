#pragma once
#ifndef HELPERS_H
#define HELPERS_H

#include <string>
#include <sstream>
#include <iostream>
#include <cstdlib>
#include <ctime>
#include <jni.h>

std::string numToString(long l);

std::string getCurrentDate();

int dayOfWeekI(int day, int mon, int year);
int maxDays(int mon, int year);

void throwJavaException(JNIEnv *env, const char *msg);

#endif	