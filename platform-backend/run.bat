@echo off
rem Change to current directory
cd /d %~dp0

cls
title Installing WebCore Project
rem Use [skipTests] instead of [maven.test.skip], to "skip running tests, but still compile them".
call ../install.bat
if ERRORLEVEL 1 GOTO :ERR

set MAVEN_OPTS=%MAVEN_OPTS% -Xms512m -Xmx512m -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8887

:START
cd ./web
cls
title Running WebCore Project in Tomcat 7
rem Use Spring development profile to run on server. Should set [spring.profiles.active] or [spring.profiles.default] here to match database configurations.
call mvn tomcat7:run -Dmaven.test.skip=true -fail-never
GOTO :EOF

:ERR
echo.
echo. Project Installation has failed. See above for details.
pause>nul
