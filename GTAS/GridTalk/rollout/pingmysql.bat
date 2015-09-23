@echo off
call setenv.bat

:pingmysql
start /D%DB_HOME% /MIN /WAIT bin\mysqladmin --port=3306 -u root -p%1 ping
if errorlevel 1 goto pingmysql

exit /b
