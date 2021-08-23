# Capture Image from Ip camara

This project was created because we need to acquire image from whiteboard when we made notes or brainstorming in there. So everytime that we need to capture we used the mobile phone to take a photo of whiteboard and after put it in a shared folder or in a project manager to share with all members. So we had an idea capture whiteboard notes with a surveilence ip camera that had whiteboard on its angle view that we had in the office. We also had to do prespective correction because whiteboard was not in center of angle view camera. 

This is a **javafx** project and you can install it on your computer and configure it to use with keyboard shortcuts or you can use it in manual mode.

In this readme we will explain how you can build project, the requirements and how you can run it, and in another section we will see how we use it.

## Requirements

Before run it we should have the following tools dependencies:

*   FFMPEG
*   Convert

Both of them can solve it, installing [imagemagick](https://www.imagemagick.org/script/download.php). In the process of installationyou should check the ffmpeg and legacy convert.


## How compile and run project
The project was build with maven check pom.xml for more details, as plugins, dependencies etc... the maven version was
![Maven Version](/docs/mavenVersion.png "Maven version used")
All java code related is inside src directory i use Visual Code IDE, check https://code.visualstudio.com/docs/java/java-tutorial to learn how setting up vs code to java development. Check extension pack to better experirience.

I also put a javafx support extension to open fxml files in scenebuilder

So to this project i use this tools:

- **JDK16** from https://github.com/adoptium/temurin16-binaries/releases/download/jdk-16.0.2%2B7/OpenJDK16U-jdk_x64_windows_hotspot_16.0.2_7.msi

![Java Version](/docs/javaVersion.png "Used java version")

- **JAVAFX 16 SDK** check url https://gluonhq.com/products/javafx/

    - SDK https://gluonhq.com/download/javafx-16-sdk-windows/

    - JMODS https://gluonhq.com/download/javafx-16-jmods-windows/

    - SceneBuilder 16 https://gluonhq.com/products/scene-builder/

- **wixtoolset 3.11** to windows this is for generate installer with jpackage from jdk


## Usefull commands
Inside root project folder in the same level where pom.xml is localized

**to tun project without make jar file**
```
mvn clean javafx:run
```
**to compile and make jar file**
```
mvn clean compile assembly:single
```
or if you want skip unit tests 
```
mvn clean compile assembly:single -DskipTests=true
```


**Run in vscode without maven**
I you want run it from vscode without maven you should put in .vscode folder in root project a file with name launch.json this file gives the instructions to vscode to run your java main file

![Java Version](/docs/runFromVscodeWithoutMaven.png "Used java version")


```launch.json

{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Launch App",
            "request": "launch",
            "mainClass": "com.s2h.capture.App",
            "vmArgs": "--module-path D:/softwarePortable/JAVA/javafx-sdk-16/lib --add-modules javafx.controls,javafx.fxml",
            "projectName": "capture"
        }        
    ]
}
```

Of course you should change the path to your javafx sdk folder

**Generate jar without maven plugin maven-assembly-plugin** 
in root folder of project to generate jar
```
mvn clean compile jar:jar
```
to run it

from root folder
```
java --module-path D:/softwarePortable/JAVA/javafx-sdk-16/lib --add-modules javafx.controls,javafx.fxml -jar .\target\capture-1.0.jar
```

change to your javafx sdk folder

## How use it

To use first you should install it or run it from project. After start the icon it will be in task bar icons.

![Capture Started](/docs/startCapture.png "Capture started")

If you right click on icon you should see Menu where you can choose the options that you want or take a manual capture or adjust settings.

![Capture Menu](/docs/menuCapture.png "Menu Capture")


### Crop Settings

The crop settings is to adjust the device address, crop settings and prespective correction if you want

![Crop Settings Capture](/docs/cropSettingsCapture.png "Crop Settings Capture")

if you know you can put all values in manual way, but if you dont know you should click on "need help"

![Need Help Capture](/docs/needHelpCapture.png "Need Help Capture")

after put device ip address and get image you should see image captured from device, after you should select points area from image where you want crop and if you want made prespective correction.

![Select Points Area Capture](/docs/selectPointsAreaCapture.png "Select Points Area Capture")

After that you should click on crop button

![Crop and Save Configurations on capture](/docs/cropAndSaveConfigurationsCapture.png "Crop and Save Configurations on capture")

After save configurations, you can close need help window and crop settings window.

### Global Settings 

In global settings area you can set shortcuts to made captures more easy, you can do Capture All in this option you can
capture original image and cropped image. Capture Complete in this option you will only capture a original image from device. Capture Cropped option you will capture only cropped image. To all of options you can asign a keyboard shortcut a prefix to image and the folder where save captures.

![Global Settings capture](/docs/globalSettingsCapture.png "Global Settings capture")

After save configuration you should be able to capture image from devide with shortcut key.

### Manual Crop

In manual capture you can choose the folder where you want to save capture completed and cropped capture, after you can click in capture button to save captures in the folders that you choose. Pay attention because if in your crop setting you dont check crop image the manual capture only takes the complete capture.

![Manual capture](/docs/manualCapture.png "Manual capture")

### Take capture

If you click on take a capture it will do the same thing as manual crop the main diference is that it will do it automatic without
interface graphic. 

### Notes in SceneBuilder

In scenebuiler i had some libraries installed, check the image

![SceneBuilder Libraries](/docs/sceneBuilderLibraries.png "Libraries used in scenebuilder")



### troubleshooting

In scenebuilder i cant use jfxtextfield component from jfoenix-9.0.10 dependencie because this component has a problem with JDK 16

## Made Setup Installer

In the project there are a folder called installer where there are all scripts (powershell scripts) to generate installer to windows. You can generate also for mac and for linux using jpackage utility that came with jdk 16, i think that is available since jdk 14.

To generate windows installer its important install wixtoolset the version that i use was 11

So to generate MSI in several languages you should configure generateWindowsInstaller.ps1 and after call it
you should stay inside installer folder and call script

```
.\generateWindowsInstaller.ps1
```

You must wait a little until generate all the msi files that you want.

Inside installer folder there are a folder called resources-dir where you can overwrite files and icons, licences and translations.


## TODO

There are things todo like in almost all software products

in the setup generator for windows we shoud join setup localizations in only one setup and this setup should put language depending of sytem language

**notes**

& "C:\Program Files (x86)\WiX Toolset v3.11\bin\torch.exe" .\capture-1.0.msi .\capture-es.msi 
-o es-ES.mst

its necesary install windows10 sdk  
https://developer.microsoft.com/es-es/windows/downloads/windows-10-sdk/

cscript.exe .\wisubstg.vbs .\capture-1.0.msi .\es-ES.mst 1033






