@ECHO OFF

if [%1] == [] (
  GOTO stopService
) ELSE (
  GOTO stopDB
)

:stopService
net stop pgsql-8.2
GOTO :EOF

:stopDB
REM %1 GRIDTALK_HOME
%1\pgsql\bin\pg_ctl stop -D %1\db
GOTO :EOF





