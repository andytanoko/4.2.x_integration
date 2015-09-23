REM %2 Database home

RD /s /Q %2\data\appdb
RD /s /Q %2\data\userdb
RD /s /Q %2\data\jbossdb
RD /s /Q %2\data\archivedb

REM EXIT 0;