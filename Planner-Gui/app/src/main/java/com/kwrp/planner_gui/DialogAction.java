package com.kwrp.planner_gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by KurtsPC on 9/05/2017.
 */

public class DialogAction {

    public static AlertDialog createAboutDialog(AppCompatActivity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setMessage("")
                .setTitle("About us")
                .setMessage("Cyka Blyat Idi Nahoi");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog createSettingsDialog(AppCompatActivity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setMessage("")
                .setTitle("Settings")
                .setMessage("Change Colour scheme what else?");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog createSyncDialog(AppCompatActivity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setMessage("")
                .setTitle("Synchronise")
                .setMessage("Synchronise with:\n-Evision\nGoogle Calendar\n-Hotmail Calendar\n-Facebook Calendar");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

//    public static AlertDialog exitButtonAction(AppCompatActivity parent) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
//        builder.setMessage("")
//                .setTitle("Exit")
//                .setMessage("Are you sure you want to exit?");
//
//        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
//            }
//        });
//
//        builder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
////                parent.finish();
//                System.exit(0);
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        return dialog;
//    }




}
