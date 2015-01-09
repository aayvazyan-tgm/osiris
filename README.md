00siris
======
[![Build Status](http://aayvazyan.bitnamiapp.com/jenkins/job/osiris/badge/icon)](http://aayvazyan.bitnamiapp.com/jenkins/job/osiris/)
Our matriculation project. TODO: describe

Development
===========
To start developing, you need to take a few steps.

Step 1: System Setup
===========
Make sure the following things are set up:
-----------
- Your android phone allows adb connection.

- via the SDK Manager as defined in ./osiris/build.gradle:
    - Android SDK 21
    - buildToolsVersion "21.1.1"
    - support library: appcompat-v7:21.0.2

The following programs should be in your Path variable
------------------------------------------------------
- git
- ADB (Android Debug Bridge)

Step 2: Clone the Repo
===========

Clone this repo.


Step 3: Prepare the Project
===========


Android sdk path
----------------
Create a file called local.properties in the project root and fill in the path to your SDK dir.
A sample config file can be found in `/sampleConfiguration/local.properties`

Alternatively you could set your ANDROID_HOME variable to this path.

`local.properties`

```
sdk.dir=E\:\\Android\\sdk\\
```

How to Compile a project:
=========================
This project uses Gradle as a build file.

The easiest way is to use the inbuilt function of your IDE/a gradle plugin for your IDE if it does not support gradle by default
Generate Jars:
--------------
gradlew :subProjectName:packJarWithLibs

Desktop App:
------------

gradlew :desktopApp:run to run the Application

Android Project:
----------------

Run assembleRelease to build the .apk

Run installDebug to install this app on your device (You need to start it manually)


Intellij
--------

- Finish The Project Setup Steps

- Import project -> Select the project destination.
Make sure "foo\osiris\build.gradle" is selected, and not "foo\osiris\gradle"

You are done!

Eclipse
--------
Do not use the android adt plugin, a default eclipse with the gradle plugin should work.

- Install the Gradle Plugin for Eclipse.
- Finish The Project Setup Steps
- In Eclipse, Import > Gradle Project
- Select the path to the project and press the "Build Model" button
- Make sure that auto-select subprojects is marked.
- You are done!

Step 4 - Setup the controller
===========

Instructions
------------

Before you can use the 00SIRIS robot, you need to connect the controller with the motors and sensors.

![The controller](http://i.imgur.com/XYklObC.png "The controller")

You need to connect the first motor(base motor) on the port M0 of the controller.
(WARNING: You need to check the direction the motor is plugged in. If the direction is wrong, you will not be able to move the motor correctly.)

The sensor from the base-motor has to be plugged into the port 0 of the controller.
(WARNING: Be very careful with choosing the correct sensor. A wrong sensor hinders the software from preventing positions, which might damage the robot.

The same goes with the motor of the first axis. This motor has to be plugged into the port M1 and his sensor into port 1 of the controller.

The same goes for the remaining motors and sensors.

Axis2 motor = M2

Sensor of Axis2 motor = 2

Axis3 motor = M3

...

Step 5 - Setup the Raspberry Pi + Hedgehog 
===========

Instructions
------------
To successfully link our Raspberry Pi- with the Hedgehog Hardware we have to make sure that the following buses are connected to each other:

A little glossary:

- The pin, which is responsible for any POWER, is identified by the name 5V (as 5 Voltage)

- The pin, which is responsible for transmitting signals/data, is identified by the name TX (Transmit)

- The pin, which is responsible for receiving signals/data, is identified by the name RX (Receive)

- The pin, which is responsible for grounding, is identified by the name GND (Ground)

Make sure to connect the following pins/hardware:
    
- The Hedgehog Controller is connected to the battery, the main source of power. (via the giant black-n-red cable stickin out)

- Both the HWC and PI 5V pins have to be connected to each other.

- The ReceiveX pin of the PI has got to be connected to the TransmitX pin of the HWC. (and vice versa)

- Both the HWC and PI GND pins have to be connected to each other.

Wash your hands before touching any contacts with your greasy fingers :)
    
![The connection between HWC and PI](http://i.imgur.com/BrGbwMM.jpg "The connection between HWC and PI")
    
