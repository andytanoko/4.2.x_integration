rem %1 : DB vendor info
rem %2 : Path to vendor specific DB client
rem %3 : root password
rem %4 : indicate whether we should populate the script to the GT default db --> "gtdb". Cater for those DB using Schema
rem %5 : Path to sql script file

IF "postgres" == "%1" (
  GOTO postgres
)ELSE (
  GOTO mysql 
)


:postgres
IF "true" == "%4" (
REM needed when we create the database "gtdb"
  %2 --username postgres --port 35432 --file %5
) ELSE (
  %2 --username postgres --port 35432 --dbname gtdb --file %5
)
goto:eof


rem %1 : Path to vendor specific DB client
rem %2 : root password
rem %3 : indicate whether we should populate the script to the GT default db --> "gtdb". Cater for those DB using Schema
rem %4 : Path to sql script file

:mysql
%1 -u root -p%2 --port=3306 <%4
goto:eof