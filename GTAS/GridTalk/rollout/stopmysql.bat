@echo off
call setenv.bat
start /D%DB_HOME% /MIN bin\mysqladmin --port=3306 -u root -p%1 shutdown 
exit /b

