@echo off
call setenv.bat

echo Executing startup script for %APPLICATION% ....

:start db
echo Starting the application database now ....
cd %GRIDTALK_HOME%\bin
rem start /MIN %DB_STARTUP_CMD%
call %DB_STARTUP_CMD%
call %DB_CHECK_CMD%
echo Application Database started successful

:clear server logs
echo clearing the server logs now...
call :shortName "%APPSERVER_LOG_DIR%"
set APPSERVER_LOG_DIR=%shortname%
if not exist %APPSERVER_LOG_DIR% mkdir %APPSERVER_LOG_DIR%
cd %APPSERVER_LOG_DIR%
del /Q *.%APPSERVER_LOG_EXT%
echo "" > dummy.%APPSERVER_LOG_EXT%

:start log server
echo Starting the logging server now...
cd %GRIDTALK_HOME%\bin
start "%LOGSERVER_WINDOW%" /MIN %LOGSERVER_START_CMD%
nircmd.exe win hide stitle %LOGSERVER_WINDOW%

:start appserver
echo Starting the application server now, please wait ....
cd %APPSERVER_CMD_DIR%
start /MIN %APPSERVER_STARTUP_CMD%

:loop1
cd %GRIDTALK_HOME%\bin
findstr /C:%APPSERVER_STARTUP_MSG% %APPSERVER_LOGS%
if errorlevel = 1 goto loop1
echo Application Server startup successful

:start webserver
if not defined WEBSERVER_HOME goto end
echo Starting the web server now, please wait ....
call :shortName "%WEBSERVER_LOG_DIR%"
set WEBSERVER_LOG_DIR=%shortname%
if not exist %WEBSERVER_LOG_DIR% mkdir %WEBSERVER_LOG_DIR%
cd %WEBSERVER_LOG_DIR%
del /Q *.%WEBSERVER_LOG_EXT%
echo "" > dummy.%WEBSERVER_LOG_EXT%
cd %WEBSERVER_CMD_DIR%
call %WEBSERVER_STARTUP_CMD%

:loop2
cd %GRIDTALK_HOME%\bin
fgrep -x -f -e %WEBSERVER_STARTUP_MSG% %WEBSERVER_LOG_DIR%\*.%WEBSERVER_LOG_EXT%
if errorlevel = 1 goto loop2
echo Web Server startup successful

:end
echo Successfully startup %APPLICATION%
rem cd %GRIDTALK_HOME%\bin

rem -- converts the path to short name
:shortName
set shortname=%~fs1
goto :EOF


REM Change history
REM 3 Jan 2006 [NSL] Add in call to startLogServer.bat between clearing of server logs and starting app server
REM                  fgrep APPSERVER_STARTUP_MSG in server*.log instead of *.log
