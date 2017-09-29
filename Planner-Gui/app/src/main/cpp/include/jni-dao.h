//
// Created by William Sanson on 5/29/17.
//
#include <jni.h>

#ifndef PLANNER_GUI_JNI_DAO_H_H
#define PLANNER_GUI_JNI_DAO_H_H

#ifdef __cplusplus
extern "C"
{
#endif

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayMonth_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */);

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniCreateXml(
        JNIEnv *env,
        jobject /* this */, jstring dir);


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetCurrentDate(
        JNIEnv *env,
        jobject /* this */);


JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniCreateEvent(
        JNIEnv *env, jobject /* this */, jstring title, jstring description, jstring start,
        jstring duration, jstring filepath, jstring date);

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniRemoveEventDb(
        JNIEnv *env, jobject /* this */, jstring eventId, jstring filepath);

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DisplayDay_jniGetDayDb(
        JNIEnv *env, jobject /* this */, jstring dir, jstring date);

JNIEXPORT jstring JNICALL Java_com_kwrp_planner_1gui_DialogAction_jniCreateEvent(
        JNIEnv *env, jobject /* this */,
        jstring title, jstring description, jstring start, jstring duration, jstring dir,
        jstring date);

#ifdef __cplusplus
}
#endif
#endif //PLANNER_GUI_JNI_DAO_H_H
