00siris
======
[![Build Status](http://aayvazyan.bitnamiapp.com/jenkins/job/osiris/badge/icon)](http://aayvazyan.bitnamiapp.com/jenkins/job/osiris/)
Our matriculation project. TODO: describe

Development
===========
To start developing, you need to take a few steps.

Step 1: System Setup
--------------------
Make sure the following things are set up:
- Your android phone allows adb connection.
- ADB is installed.

- via the SDK Manager as defined in osiris/build.gradle:
    - Android SDK 17, 19
    - buildToolsVersion 19.1.0
    - support library: appcompat-v7:20.0.0
    
Step 2: Clone the Repo
----------------------

Clone this repo.


Step 3: Project Setup
---------------------

The following programs should be in your Path variable
------------------------------------------------------
- git

Dependencies
------------
To compile the linker, you need to add the linkjvm.jar manually to the libs folder of the linker subproject.

osiris/linker/libs/linkjvm.jar.

Android sdk path
----------------
Create a file called local.properties in the project root and fill in the path to your SDK dir.

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
gradlew :subProjectName:jar

Desktop App:
------------

gradlew :desktopApp:run to run the Application

Android Project:
----------------

Run assembleRelease to build the .apk

Run installDebug to install this app on your device (You need to start it manually)


Intellij
--------

Follow the 3 setup steps.

Import project -> Select the project destination.

Make sure "foo\osiris\build.gradle" is selected, and not "foo\osiris\gradle"

You are done!

Eclipse
--------
Do not use the android adt plugin, a default eclipse with the gradle plugin should work.

- Install the Gradle Plugin for Eclipse.
- Pull the project
- Create the following file in the project, to the android-sdk

`local.properties`

```
sdk.dir=E\:\\Android\\sdk\\
```
- In Eclipse, Import > Gradle Project
- Select the path to the project and press the "Build Model" button
- Make sure that auto-select subprojects is marked.
- To compile the linker, you need to add the linkjvm.jar manually to the libs folder of the linker subproject.

    osiris/linker/libs/linkjvm.jar.
    
- Finished


Setup the controller
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
