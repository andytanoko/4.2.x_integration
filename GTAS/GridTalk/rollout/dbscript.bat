rem %1 : Path to vendor specific DB client
rem %2 : indicate whether we should populate the script to the GT default db --> "gtdb". Cater for those DB using Schema
rem %3 : Path to sql script file


:postgres
IF "true" == "%2" (
REM needed when we create the database "gtdb"
  %1 --username postgres --port 35432 --file %3
) ELSE (
  %1 --username postgres --port 35432 --dbname gtdb --file %3
)