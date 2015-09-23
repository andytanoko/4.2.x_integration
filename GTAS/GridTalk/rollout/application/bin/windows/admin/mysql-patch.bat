@echo off
rem %1: Path to sql script file

:startdb
echo starting database
call %GRIDTALK_HOME%\bin\startmysql.bat

call %GRIDTALK_HOME%\bin\pingmysql.bat

:patching database 
echo patching database with script file %1
%GRIDTALK_HOME%\mysql\bin\mysql -u root -p%GRIDTALK_DB_PASSWORD% --port=3306 <%1

:stopdb
echo shutting down database
call %GRIDTALK_HOME%\bin\stopmysql.bat
