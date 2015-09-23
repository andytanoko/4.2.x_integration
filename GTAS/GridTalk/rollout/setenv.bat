@echo off
set NOPAUSE=YES

call :shortName "%GRIDTALK_HOME%"
set GRIDTALK_HOME=%shortname%

call :shortName  %GRIDTALK_HOME%\jboss-4.2.2.GA
set APPSERVER_HOME=%shortname%

call :shortName  %GRIDTALK_HOME%\jre150_20
set JAVA_HOME=%shortname%

call :shortName %GRIDTALK_HOME%\..\jakarta-ant-1.5.1-bin
set ANT_HOME=%shortname%

call :shortName  %GRIDTALK_HOME%\pgsql
set DB_HOME=%shortname%

call :shortName  %GRIDTALK_HOME%\data
set DATA_HOME=%shortname%

rem set path to Ant and Java
set PATH=%ANT_HOME%\bin;%JAVA_HOME%\bin;%PATH%

rem application specific environment settings
set APPLICATION=GT(GridTalk)

rem database specific environment settings
set DB_STARTUP_CMD=startPostgres.bat
set DB_SHUTDOWN_CMD=stopPostgres.bat

rem application server specific environment settings
set APPSERVER_LOG_DIR=%APPSERVER_HOME%\logs\server\
set APPSERVER_LOG_EXT=log
set APPSERVER_CMD_DIR=%APPSERVER_HOME%\bin
set APPSERVER_STARTUP_CMD=run.bat
set APPSERVER_STARTUP_MSG="Started in"
set APPSERVER_SHUTDOWN_CMD=shutdown --server=localhost:31099
set APPSERVER_SHUTDOWN_MSG="Shutdown complete"

rem webserver specific environment settings
goto :EOF

rem -- converts the path to short name
:shortName
set shortname=%~fs1
goto :EOF
