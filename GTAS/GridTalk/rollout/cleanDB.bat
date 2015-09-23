REM %1 Location of the postgres

REM Access Postgres home
SET POSTGRES_HOME=%1
SET POSTGRES_DB_CMD=%POSTGRES_HOME%\bin\psql --username postgres --port 35432
SET POSTGRES_GTDB_CMD=%POSTGRES_DB_CMD% --dbname gtdb

echo debug "droping database gtdb"
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