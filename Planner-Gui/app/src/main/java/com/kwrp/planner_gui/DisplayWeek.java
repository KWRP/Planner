package com.kwrp.planner_gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class DisplayWeek extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_week);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_week);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_week, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_display_month) {
            Intent myIntent = new Intent(this, DisplayMonth.class); /** Class name here */
            startActivity(myIntent);
            finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog dialog = createSettingsDialog();
            dialog.show();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            AlertDialog dialog = createSyncDialog();
            dialog.show();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            AlertDialog dialog = createAboutDialog();
            dialog.show();
            return true;
        }

        if(id== R.id.action_exit){
            AlertDialog dialog = exitButtonAction();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog createAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayWeek.this);
        builder.setMessage("")
                .setTitle("About us")
                .setMessage("What the fuck did you just fucking type about me, you little bitch? I’ll have you know I graduated top of my class at MIT, and I’ve been involved in numerous secret raids with Anonymous, and I have over 300 confirmed DDoSes. I am trained in online trolling and I’m the top hacker in the entire world. You are nothing to me but just another virus host. I will wipe you the fuck out with precision the likes of which has never been seen before on the Internet, mark my fucking words. You think you can get away with typing that shit to me over the Internet? Think again, fucker. As we chat over IRC I am tracing your IP with my damn bare hands so you better prepare for the storm, maggot. The storm that wipes out the pathetic little thing you call your computer. You’re fucking dead, kid. I can be anywhere, anytime, and I can hack into your files in over seven hundred ways, and that’s just with my bare hands. Not only am I extensively trained in hacking, but I have access to the entire arsenal of every piece of malware ever created and I will use it to its full extent to wipe your miserable ass off the face of the world wide web, you little shit. If only you could have known what unholy retribution your little “clever” comment was about to bring down upon you, maybe you would have held your fucking fingers. But you couldn’t, you didn’t, and now you’re paying the price, you goddamn idiot. I will shit code all over you and you will drown in it. You’re fucking dead, kiddo.");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog createSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayWeek.this);
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

    private AlertDialog createSyncDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayWeek.this);
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

    private AlertDialog exitButtonAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayWeek.this);
        builder.setMessage("")
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                System.exit(0);
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }




}
