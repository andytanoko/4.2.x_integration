@echo off

REM RUN THIS BATCH FILE TO UPDATE THE SERVICEASSIGNMENT ENTITY IN USERDB DATABASE.
REM THE WEBSERVICEUIDS FIELD DATA IS CONVERTED FROM SET TO STRING VALUEIS

:startdb
echo starting database
call %GRIDTALK_HOME%\bin\startmysql.bat

:patching database 
java -jar patch-serviceassignment.jar

CALL :updatetable .\patch-serviceassignment.sql

:stopdb
echo shutting down database
call %GRIDTALK_HOME%\bin\stopmysql.bat

:end
exit

:updatetable
SET sqlscript=%~fs1
REM SAVE CURRENT DIRECTORY AND CD TO DB_HOME
PUSHD %DB_HOME%
ECHO.
ECHO Altering serviceassignment table
bin\mysql < %sqlscript%

REM CD TO ORIGINAL DIRECTORY
POPD
GOTO end_

:end_
endlocal


