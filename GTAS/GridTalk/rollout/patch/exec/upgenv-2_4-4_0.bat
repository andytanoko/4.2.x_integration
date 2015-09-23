@echo off

SET DB_BACKUP=%GRIDTALK_HOME%\mysql_2.4

echo.
echo Upgrading the GridTalk 2.4 environment to GridTalk 4.0 ...
echo This script will:
echo a. rename the existing "mysql" folder to "mysql_2.4"
echo b. setup "jboss-4.0.2" and copy existing GridTalk configurations and data files from "jboss-3.2.2" folder
echo c. setup "jre150_05"
echo d. setup "mysql" (version 5) and copy existing GridTalk databases "appdb" and "userdb" from "mysql_2.4" folder
echo e. reset GridTalk database password to default. Please use changeDatabasePwd.bat after upgrade to change the password, if necessary.
echo.

echo renaming existing mysql to mysql_2.4...
move /Y %DB_HOME% %DB_BACKUP% >> %1

echo copying new JBOSS, MySQL and JRE to %GRIDTALK_HOME% ...
xcopy /E /Y /H /I env\* %GRIDTALK_HOME% >> %1

echo copying existing application conf and data to new JBoss...
xcopy /E /Y /H /I %APPSERVER_HOME%\bin\conf %GRIDTALK_HOME%\jboss-4.0.2\bin\conf >>%1
xcopy /E /Y /H /I %APPSERVER_HOME%\bin\gtas %GRIDTALK_HOME%\jboss-4.0.2\bin\gtas >>%1

echo copying existing application databases to new MySQL...
xcopy /E /Y /H /I %DB_BACKUP%\data\appdb %DB_HOME%\data\appdb >>%1
xcopy /E /Y /H /I %DB_BACKUP%\data\userdb %DB_HOME%\data\userdb >>%1

echo change database password
set GRIDTALK_DB_PASSWORD=gtasdb

echo changing APPSERVER_HOME to new JBoss
call :shortName  %GRIDTALK_HOME%\jboss-4.0.2
set APPSERVER_HOME=%shortname%

echo changing JAVA_HOME to new JRE
call :shortName  %GRIDTALK_HOME%\jre150_05
set JAVA_HOME=%shortname%


echo environment upgrade complete
GOTO :EOF

rem -- converts the path to short name
:shortName
set shortname=%~fs1
goto :EOF
