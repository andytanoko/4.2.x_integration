@echo off
call %GRIDTALK_HOME%\bin\setenv.bat
start /D%DB_HOME% /MIN bin\mysqladmin --port=3306 -u root -p%GRIDTALK_DB_PASSWORD% shutdown
exit /b

