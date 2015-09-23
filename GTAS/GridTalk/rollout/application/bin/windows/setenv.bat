@echo off
set NOPAUSE=YES

call :shortName "%GRIDTALK_HOME%"
set GRIDTALK_HOME=%shortname%
 
call :shortName  %GRIDTALK_HOME%\jboss-4.2.3.GA
set APPSERVER_HOME=%shortname%

call :shortName  %GRIDTALK_HOME%\jre150_20
set JAVA_HOME=%shortname%

call :shortName %GRIDTALK_HOME%\..\apache-ant-1.8.2
set ANT_HOME=%shortname%

rem set path to Ant and Java
set PATH=%ANT_HOME%\bin;%JAVA_HOME%\bin;%PATH%

rem application specific environment settings
set APPLICATION=GT(GridTalk)

rem database specific environment settings
set DB_STARTUP_CMD=startPostgres.bat
set DB_SHUTDOWN_CMD=stopPostgres.bat
set DB_CHECK_CMD=pingpostgres.bat
set GRIDTALK_DB_PASSWORD=gtasdb

rem set data dir
set DATA_HOME=%GRIDTALK_HOME%\data
set APPS_HOME=%DATA_HOME%\GNapps
set LOGS_HOME=%GRIDTALK_HOME%\logs

rem application server specific environment settings
set APPSERVER_LOG_DIR=%LOGS_HOME%\server
set APPSERVER_LOG_EXT=log
set APPSERVER_LOGS=%APPSERVER_LOG_DIR%\*.%APPSERVER_LOG_EXT%
set APPSERVER_CMD_DIR=%APPSERVER_HOME%\bin
set APPSERVER_STARTUP_CMD=run.bat --host=0.0.0.0 
set APPSERVER_STARTUP_MSG="Started in"
set APPSERVER_SHUTDOWN_CMD=shutdown --server=localhost:31099
set APPSERVER_SHUTDOWN_MSG="Shutdown complete"

rem log server specific settings
SET LOGSERVER_WINDOW=GtasLogServer
SET LOGSERVER_START_CMD=startLogServer.bat
SET LOGSERVER_STOP_CMD=stopLogServer.bat

rem change this to uniquely identify the node on the cluster
SET HOST_ID=gnode1

rem set java options
SET JAVA_OPTS=-Dentity.ejb.use.remote=false

rem set GLOBAL Data DIR, Temp Dir, Log Dir
SET JAVA_OPTS=-Dlogs.home.dir="%LOGS_HOME%" %JAVA_OPTS%
SET JAVA_OPTS=-Dsys.global.dir="%DATA_HOME%" -Djava.io.tmpdir="%APPS_HOME%/temp" %JAVA_OPTS%

rem For GT cluster
SET JAVA_OPTS=-Dhostid=%HOST_ID% -Dnode.clustered=false -Djboss.partition.name=GNGtasPartition %JAVA_OPTS%


rem webserver specific environment settings
goto :EOF

rem -- converts the path to short name
:shortName
set shortname=%~fs1
goto :EOF
