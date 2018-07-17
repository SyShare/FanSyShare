@if "%DEBUG%" == "" @echo off

@rem ##########################################################################
@rem
@rem  Walle CLI script for Windows
@rem
@rem ##########################################################################

cd /d %~dp0
set PATH=%CD%;%PATH%
java -jar -Duser.language=en "%~dp0\walle-cli-all.jar" %1 %2 %3 %4 %5 %6 %7 %8 %9
