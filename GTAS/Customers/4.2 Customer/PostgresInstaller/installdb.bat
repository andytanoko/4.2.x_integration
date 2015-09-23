@echo off

REM %1 - specify the path to the postgresql installer package: postgresql-8.2-int.msi

call setenv.bat

msiexec /i %1 /qb! /log "%GRIDTALK_HOME%\logs\pg_inst.log" ADDLOCAL=server,psql,pgadmin INTERNALLAUNCH=1 CREATESERVICEUSER=1 SERVICEDOMAIN="%COMPUTERNAME%" SERVICENAME="%SVC_NAME%" SERVICEPASSWORD="%SVC_PASS%" SUPERPASSWORD="%DB_PASS%" ENCODING="UTF8" LISTENPORT=%DB_PORT% BASEDIR="%DB_HOME%" DATADIR="%DB_DATA%"

REM After installation, change pg_hba.conf:  "host    all         all         127.0.0.1/32          md5" --> 'md5' change to 'trust'