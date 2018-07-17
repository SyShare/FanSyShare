@if "%DEBUG%" == "" @echo off

@rem ##########################################################################
@rem
@rem  Walle package script for Windows
@rem
@rem ##########################################################################

cd %~dp0
cd ..

.\gradlew.bat --stacktrace clean assembleDebug -PchannelList=preRelease
