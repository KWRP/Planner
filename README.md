# Planner Application
UPlanner is a basic calendar application for the modern uni student.<br />
Asthetically pleasing to see a clean calander until you look into the upcoming days.<br />

## What it does:<br />

Display the current month by default.<br />
Stores events locally on the device.<br />
Displays the events in a selected day.<br />

## How to:<br />

- [x] Swipe left or right to change month.<br />

- [x] Tap the three dot icon in top right corner to display the menu,<br />
   - Settings: choose a colour scheme,<br />
   - Help: how to use the app,<br/>
   - About: information about us and the app.<br />

- [x] Tap the pencil to select multiple days,<br />
    Tap the tick when selected all desired days of the month,<br />
    Tap cancel to cancel the selection.<br />
  
- [x] Tap on a day to edit the events on that day.<br />
    Tap the pencil to add an event,<br />
    Tap an event to update or delete it.<br />


## Download and Install: <br />
Setting up Android Studio

1. Download Android Studio from: https://developer.android.com/studio/index.html. 
   If you are using windows, choose the download that already includes the SDK.
2. Install android studio and start the application.
3. On the startup menu, click on Configure and then select SDK Manager.

If you are plan to run the application on you own android device, then select its 
Androud version, otherwise just select the most recent API Level.

- Click on the tab that says SDK Tools and make sure the following are ticked:<br />
	- [x] Android SDK Build-Tools <br />
	- [x] CMake<br />
	- [x] LLDB<br />
	- [x] Android Emulator<br />
	- [x] Android SDK Platform-Tools<br />
	- [x] Android SDK Tools<br />
	- [x] Documentation for Android SDK<br />
	- [x] Intel x86 Emulator Accelerator (HAXM installer)<br />
	- [x] NDK<br />
	Under Support Repository:<br />
		- [x] ConstraintLayout for Android<br />
		- [x] Solver for ConstraintLayout<br />
		- [x] Android Support Repository<br />
		- [x] Google Repository<br />

	-After you have selected them all, click Apply and Accept the Terms and Conditions.

4. Once the extra components have been installed, click on 'Check out project from 
   Version Control' and select Github, then Clone the directory 
   https://github.com/KWRP/Planner.git

5. Once it has been cloned, if any pop-ups come up select no and instead select
   'Open an existing Android Studio project and locate the Planner-Gui project 
    in the cloned repository. 

6. Once the project build you may get some errors about modules can't be loaded. 
   Select to remove these files. If it fails to build, then you might not have
   selected all of the SDK components. 
   To fix this, just select the link to install the missing components below the error message.

7. Once the build is complete you can either plug in your android device and allow debugging mode,
   on it, or you can create an emulator from one of the downloaded mobile device images.


### External library dependancies:

tinyxml2: https://github.com/leethomason/tinyxml2 <br />
Display month: https://github.com/jrdnull/Android-Calendar-GridView-Adapter/blob/master/MonthAdapter.java; <br />


