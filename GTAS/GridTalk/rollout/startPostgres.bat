@ECHO OFF

if [%1] == [] (
  GOTO startService
) ELSE (
  GOTO startDB
)

:startService
net start pgsql-8.2
GOTO :EOF

:startDB
REM %1 GRIDTALK_HOME
%1\pgsql\bin\pg_ctl start -D %1\db
GOTO :EOF





