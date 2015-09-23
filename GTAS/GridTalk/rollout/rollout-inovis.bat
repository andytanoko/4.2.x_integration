@echo off

call rollout.bat
call rollout-gtvan.bat

SET OVERWRITE_DIR=..\..\Customers\Inovis

REM overwrite txmr.war customization
REM xcopy /e /Y /H /I %OVERWRITE_DIR%\GTAS\GT-VAN\Presentation\txmr.war .\application\dist\jboss-4.0.5.GA\txmr.war

REM overwrite appserver conf
xcopy /e /Y /H /I /EXCLUDE:rollout.excludes.txt %OVERWRITE_DIR%\Oracle\appserver .\appserver

REM arrange conf
SET APPSERVER_BASE=.\appserver\jboss-4.0.5.GA\server\default

del /Q %APPSERVER_BASE%\deploy\mysql-ds.xml
del /Q %APPSERVER_BASE%\lib\mysql*.jar
del /Q %APPSERVER_BASE%\deploy\jms\mysql*.xml
del /Q %APPSERVER_BASE%\lib\postgre*.jar

xcopy /e /Y /H /I %APPSERVER_BASE%\deploy\jms %APPSERVER_BASE%\deploy-hasingleton\jms
del /S /Q %APPSERVER_BASE%\deploy\jms

REM copy inovis setup files here
if exist .\inovis del /S /Q .\inovis
xcopy /e /Y /H /I %OVERWRITE_DIR%\rollout\* .

REM Invoke package-inovis-release.xml here
ant -buildfile package-inovis-release.xml
