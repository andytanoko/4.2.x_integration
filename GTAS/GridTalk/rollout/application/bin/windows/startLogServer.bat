@echo off
call %GRIDTALK_HOME%\bin\setenv.bat
TITLE %LOGSERVER_WINDOW%
echo To enable logging, please do not close this window while GridTalk is running...
java -Duser.dir=%GRIDTALK_HOME% -Dapplog.home="%LOGS_HOME%" -Dhost.id="%HOST_ID%" -cp "%APPSERVER_HOME%/server/default/lib/log4j.jar;%APPSERVER_HOME%/server/default/lib/custom-log.jar" com.gridnode.pdip.framework.log.GNSocketServer 12345 "%APPS_HOME%/conf/default/lcf"
exit /b
