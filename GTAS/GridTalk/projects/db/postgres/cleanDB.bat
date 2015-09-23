REM %1 Location of "runas" command or the equivalent command
REM %2 Location of the DB
REM %3 Location of the GridTalk
REM %4 Param (1) for "runas" command
REM %5 Param (2) for "runas" command
REM %6 Param (3) for "runas" command
REM %7 Param (4) for "runas" command
REM %8 Param (5) for "runas" command

REM Access Postgres home
SET POSTGRES_HOME=%2
SET POSTGRES_DB_CMD=%POSTGRES_HOME%\bin\psql --username postgres --port 35432
SET POSTGRES_GTDB_CMD=%POSTGRES_DB_CMD% --dbname gtdb

%POSTGRES_DB_CMD% -c "DROP DATABASE IF EXISTS gtdb";

%POSTGRES_DB_CMD% -c "DROP OWNED BY appdb CASCADE";
%POSTGRES_DB_CMD% -c "DROP OWNED BY userdb CASCADE";
%POSTGRES_DB_CMD% -c "DROP OWNED BY jbossdb CASCADE";
%POSTGRES_DB_CMD% -c "DROP OWNED BY archivedb CASCADE";

%POSTGRES_DB_CMD% -c "DROP ROLE appdb";
%POSTGRES_DB_CMD% -c "DROP ROLE userdb";
%POSTGRES_DB_CMD% -c "DROP ROLE jbossdb";
%POSTGRES_DB_CMD% -c "DROP ROLE archivedb";


REM EXIT 0;