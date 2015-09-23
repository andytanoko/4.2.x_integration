@echo off

call setenv.bat

%DB_HOME%\bin\psql -d %1 -e -f %2