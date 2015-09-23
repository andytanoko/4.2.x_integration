@echo off
call setenv.bat

echo Executing shutdown script for %APPLICATION% ....

:shutdown webserver
if not defined WEBSERVER_HOME goto shutdown appserver
echo Shutting down the web server now, please wait ....
cd %WEBSERVER_CMD_DIR%
call %WEBSERVER_SHUTDOWN_CMD%

:loop1
cd %GRIDTALK_HOME%\bin
call :shortName "%WEBSERVER_LOG_DIR%"
set WEBSERVER_LOG_DIR=%shortname%
findstr /C:%WEBSERVER_SHUTDOWN_MSG% %WEBSERVER_LOG_DIR%\*.%WEBSERVER_LOG_EXT%
if errorlevel = 1 goto loop1
echo Web Server shutdown successful

:shutdown appserver
echo Shutting down the application server now, please wait ....
cd %APPSERVER_CMD_DIR%
start /MIN %APPSERVER_SHUTDOWN_CMD%

:loop2
cd %GRIDTALK_HOME%\bin
cd %APPSERVER_LOG_DIR%
findstr /C:%APPSERVER_SHUTDOWN_MSG% *.%APPSERVER_LOG_EXT%
if errorlevel = 1 goto loop2
cd %GRIDTALK_HOME%\bin
echo Application Server shutdown successful

echo Shutting down the log server now...
call %LOGSERVER_STOP_CMD%

:shutdown db
echo Shutting down the application database now ....
rem cd %DB_HOME%
rem start /MIN %DB_SHUTDOWN_CMD%
call %DB_SHUTDOWN_CMD%
echo Application Database shutdown successful

rem -- converts the path to short name
:shortName
set shortname=%~fs1
goto :EOF

echo Successfully shutdown %APPLICATION%
rem cd %GRIDTALK_HOME\bin
:end

REM Change History
REM 3 Jan 2006 [NSL] Add in call to stopLogServer.bat between shutdown appserver and shutdown db
REM                  fgrep APPSERVER_SHUTDOWN_MSG in server*.log instead of *.log
