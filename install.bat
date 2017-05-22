@echo off
rem Change to current directory
cd /d %~dp0

cls
title Installing Project
rem Use [skipTests] instead of [maven.test.skip], to "skip running tests, but still compile them".
call mvn clean install -DskipTests=true
