@echo off
rem %1: Path to sql script file

call ..\setenv.bat

:startdb
echo starting database
call ..\%DB_STARTUP_CMD%
call ..\%DB_CHECK_CMD%

:patching database 
echo patching database with script file %1
..\..\pgsql\bin\psql --username postgres --port 35432 --dbname gtdb --file %1

:stopdb
echo shutting down database
call ..\%DB_SHUTDOWN_CMD%
