@echo off
REM TWX 19012009    GT4.2.1    The Postgres DB is installed as services. Removed the setup for DB services.

rem set environment variables
call ..\setenv.bat

SET LOGGER_SVC=GridTalk_Logger
REM SET DB_SVC=GridTalk_DB
SET AS_SVC=GridTalk_AS

if "%1"=="" goto choice
if "%1"=="-install" goto install
if "%1"=="-remove" goto remove

if [%JAVA_HOME%] == [] goto help
if [%APPSERVER_HOME%] == [] goto help
REM if [%DB_HOME%] == [] goto help

:choice
echo Please select an option
echo [I]. Install and Start the Services [%LOGGER_SVC%, %AS_SVC%] 
echo [R]. Stop and Remove the Services [%LOGGER_SVC%, %AS_SVC%]
echo.
set /P CH=[I,R]?
if /I "%CH%"=="I" goto install
if /I "%CH%"=="R" goto remove
goto choice

:install
REM echo.
REM echo Installing and start %DB_SVC% as Windows Service
REM echo ---------------------------------------------------
REM echo.
REM set REPLACE=%DB_HOME:\=/%
REM mtr -nb+:.ini my.txt - DB_HOME = %REPLACE%
REM copy my.ini %DB_HOME%\%DB_SVC%.ini

REM call %DB_HOME%\bin\mysqld --install %DB_SVC% --defaults-file=%DB_HOME%\%DB_SVC%.ini
REM if ERRORLEVEL 1 (
REM   echo cannot install %DB_SVC% as service, it has probably been installed already, try removing it first
REM   goto end
REM ) else (
REM   call :start.service "%DB_SVC%" 
REM )

echo.
echo Installing and start %LOGGER_SVC% as Windows service
echo ------------------------------------------------------
echo.
set CLASSPATH="%APPSERVER_HOME%/server/default/lib/log4j.jar;%APPSERVER_HOME%/server/default/lib/custom-log.jar"
set JAVA_OPTS=-Duser.dir=%GRIDTALK_HOME%
set JAVA_OPTS=%JAVA_OPTS% -Djava.class.path=%CLASSPATH%

javaservice -install %LOGGER_SVC% %JAVA_HOME%\bin\client\jvm.dll %JAVA_OPTS% -start com.gridnode.pdip.framework.log.GNSocketServer -params 12345 "%APPS_HOME%/conf/default/lcf/" -out %GRIDTALK_HOME%/bin/log.log -err %GRIDTALK_HOME%/bin/log.log -auto
if ERRORLEVEL 1 (
  echo cannot install %LOGGER_SVC% as service, it has probably been installed already, try removing it first
  goto end
) else (
  call :start.service "%LOGGER_SVC%" 
)


echo Installing %AS_SVC% as Windows service 
echo -----------------------------------------
echo.

rem set classpaths
set CLASSPATH=%JAVA_HOME%\lib\tools.jar
FOR /F %%I IN (classpath.txt) DO CALL :APPENDCP %%~sI

set JAVA_OPTS=-Djava.class.path=%CLASSPATH% 
set JAVA_OPTS=%JAVA_OPTS% -Xms512m -Xmx512m -XX:NewRatio=2 -XX:MaxPermSize=256m -XX:ThreadStackSize=512 -XX:GCTimeRatio=99 -XX:+DisableExplicitGC
set JAVA_OPTS=%JAVA_OPTS% -Djava.endorsed.dirs=%APPSERVER_HOME%\lib\endorsed

SET JAVA_OPTS=-Dlogs.home.dir="%LOGS_HOME%" %JAVA_OPTS%
SET JAVA_OPTS=-Dsys.global.dir="%DATA_HOME%" -Djava.io.tmpdir="%APPS_HOME%/temp" %JAVA_OPTS%
SET JAVA_OPTS=-Dhostid=%HOST_ID% -Dnode.clustered=false -Djboss.partition.name=GNGtasPartition %JAVA_OPTS%
rem For the compatibility issue between JBOSS 4.0.5 GA client and JBOSS4.2.2GA server
set JAVA_OPTS=-Djboss.remoting.pre_2_0_compatible=true %JAVA_OPTS%

rem With Sun JVMs reduce the RMI GCs to once per hour
set JAVA_OPTS=-Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000 %JAVA_OPTS%

if not exist %GRIDTALK_HOME%\logs\server\ (
  mkdir %GRIDTALK_HOME%\logs\server
)

javaservice -install %AS_SVC% %JAVA_HOME%\bin\server\jvm.dll %JAVA_OPTS% -start org.jboss.Main -params "--host=0.0.0.0" -stop org.jboss.Main -method systemExit  -err %GRIDTALK_HOME%\logs\server\stderr.log -current %APPSERVER_HOME%\bin -depends "pgsql-8.2" -auto

if ERRORLEVEL 1 (
  echo cannot install %AS_SVC% as NT service, it has probably been installed already, try removing it first
  goto end
) else (
  call :start.service "%AS_SVC%"
)

goto end


:remove
@echo Stopping and removing the %AS_SVC% service .....
call :stop.service "%AS_SVC%"
javaservice.exe -uninstall %AS_SVC%
if ERRORLEVEL 1 (
  echo Error removing %AS_SVC% service, check that the service is installed
)

echo.
@echo Stopping and removing the %LOGGER_SVC% service .....
call :stop.service "%LOGGER_SVC%"
javaservice.exe -uninstall %LOGGER_SVC%
if ERRORLEVEL 1 (
  echo Error removing %LOGGER_SVC% service, check that the service is installed
)

goto end

:start.service
echo Starting the %1 service ...
echo.
net start %1
if ERRORLEVEL 1 echo Error starting %1 as Windows service
goto :EOF

:stop.service
echo Stopping the %1 service ...
echo.
net stop %1
if ERRORLEVEL 1 echo Error stopping %1 as Windows service
goto :EOF

:APPENDCP
SET CLASSPATH=%CLASSPATH%;%1

:end

