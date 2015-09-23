@echo off
REM Check whether the postgres server is started.

:isPostgresStarted
%GRIDTALK_HOME%\pgsql\bin\pg_ctl status -D %GRIDTALK_HOME%\db\
if errorlevel 1 goto isPostgresStarted
GOTO :EOF


