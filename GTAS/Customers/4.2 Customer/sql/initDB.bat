REM Access Postgres home
SET POSTGRES_HOME=%GRIDTALK_HOME%\pgsql\
SET POSTGRES_DB_CMD=%POSTGRES_HOME%bin\psql --username postgres --port 35432
SET POSTGRES_GTDB_CMD=%POSTGRES_DB_CMD% --dbname gtdb

%POSTGRES_DB_CMD% --file create-db.sql

%POSTGRES_GTDB_CMD% --file create-schema.sql

REM Init APP DB
%POSTGRES_GTDB_CMD% --file appdb\appdb_post.sql
%POSTGRES_GTDB_CMD% --file appdb\insert-appdb-default-post.sql
%POSTGRES_GTDB_CMD% --file appdb\insert-appdb-metainfo-app-post.sql
%POSTGRES_GTDB_CMD% --file appdb\insert-appdb-metainfo-base-post.sql
%POSTGRES_GTDB_CMD% --file appdb\insert-appdb-metainfo-gtas-post.sql

REM Init USER DB
%POSTGRES_GTDB_CMD% --file userdb\first-create-userdb-app.sql
%POSTGRES_GTDB_CMD% --file userdb\first-create-userdb-base.sql
%POSTGRES_GTDB_CMD% --file userdb\first-create-userdb-gtas.sql
%POSTGRES_GTDB_CMD% --file userdb\insert-userdb-default-data.sql

REM Init JBOSS DB
%POSTGRES_GTDB_CMD% --file jbossdb\first-create-jbossdb.sql
%POSTGRES_GTDB_CMD% --file jbossdb\insert-jbossdb-default-data.sql

REM Init Archive DB
%POSTGRES_GTDB_CMD% --file archivedb\first-create-archivedb.sql