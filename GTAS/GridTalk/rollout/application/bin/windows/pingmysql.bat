@echo off
call %GRIDTALK_HOME%\bin\setenv.bat

:pingmysql
start /D%DB_HOME% /MIN /WAIT bin\mysqladmin --port=3306 -u root -p%GRIDTALK_DB_PASSWORD% ping
if errorlevel 1 goto pingmysql

exit /b
