@echo off
REM %1 Backup Dir
REM %2 Path to the patch's log file
REM %3 Backout command file

SET JBOSS_HOME=%GRIDTALK_HOME%\jboss-4.2.2.GA
SET JBOSS_BACKUP=%1

echo.
echo Upgrading the GridTalk 4.2.2 environment to GridTalk 4.2.3
echo This script will:
echo a. install the new jboss 4.2.3
echo b. move the existing jboss 4.2.2 folder to %JBOSS_BACKUP%
echo.

echo create backup folder

IF EXIST %GRIDTALK_HOME%\jboss-4.2.2.GA (
  echo BACKUP the existing JBOSS under %JBOSS_BACKUP% >> %2
  move /Y %JBOSS_HOME% %JBOSS_BACKUP%
)

echo copying new JBOSS to %GRIDTALK_HOME% ...
xcopy /E /Y /H /I .\env\* %GRIDTALK_HOME% >> %2

REM Backup the setenv.bat
mkdir %1\bin
copy %GRIDTALK_HOME%\bin\setenv.bat %1\bin

REM change the setenv.bat to point to the new JBOSS server
SET WORKING_DIRECTORY=%cd%

cd %GRIDTALK_HOME%\bin
mtr -b-ncyki:%WORKING_DIRECTORY%\patch\exec\updateJboss.txt setenv.bat
cd %WORKING_DIRECTORY%
REM Completed update to setenv.bat

REM add to backout list so that the new jboss can be backout
echo rmdir /S /Q %GRIDTALK_HOME%\jboss-4.2.3.GA >> %3
REM echo SET WORKING_DIRECTORY=%1 >> %3
REM echo cd %GRIDTALK_HOME%\bin >> %3
REM echo mtr -b-ncyki:%WORKING_DIRECTORY%\patch\exec\revertJboss.txt setenv.bat >> %3
REM echo cd %WORKING_DIRECTORY% >> %3

echo environment upgrade complete >>%2
GOTO :EOF

:EOF