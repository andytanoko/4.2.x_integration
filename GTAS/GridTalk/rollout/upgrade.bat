@echo off

rem usage: just execute the batch file
echo.
echo This script upgrades your V2.4.8 GridTalk located at: %GRIDTALK_HOME%
echo.
echo If this is not the GridTalk to upgrade, or the GridTalk is not V2.4.8, please DO NOT proceed with this upgrade.
echo It is recommended to do a backup of your GridTalk before the upgrade, if you have not done so, please do it before continuing.
echo.
set /P CH=Do you want to continue with the upgrade? [y/n]
if /I NOT "%CH%" == "y" goto abort

echo.
echo Please DO NOT CLOSE this window while the upgrade is being performed.
echo.

:upgrade
rem keep the old settings
call %GRIDTALK_HOME%\bin\setenv.bat
call :shortName %APPSERVER_HOME%

SET curr.majver=2_4
SET curr.version=2.4.8
SET upgrade.majver=4_0

:start_upgrade
rem delete old upgrade logs
del /q *.log

rem setup environment for upgrade version
call .\patch\exec\upgenv-%curr.majver%-%upgrade.majver%.bat upgenv.log

rem copy the keystore file to the keystore folder reference in jbossweb-tomcat55.sar/server.xml
rem keystore file must be present during application startup
if not exist %GRIDTALK_HOME%\data\keystore\keystore (
  mkdir %GRIDTALK_HOME%\data\keystore
  copy .\application\data\keystore\keystore %GRIDTALK_HOME%\data\keystore   
)


rem Put tools.jar into JRE to prevent exception for JSP
REM if not exist %JAVA_HOME%\lib\tools.jar (
REM   copy /B /Y .\jre150_05\lib\tools.jar %JAVA_HOME%\lib\tools.jar
REM )

IF EXIST %GRIDTALK_HOME%\docs\readme.txt (
  MOVE %GRIDTALK_HOME%\docs\readme.txt %GRIDTALK_HOME%\docs\readme_%curr.majver%.txt
)

rem application server environment settings
set properties=
set properties=-Dappserver.name=jboss
set properties=%properties% -Dappserver.version=4.0.2
set properties=%properties% -Dappserver.home=%APPSERVER_HOME% 
set properties=%properties% -Dappserver.deploy.dir=%APPSERVER_HOME%/server/default/deploy

rem database environment settings
set properties=%properties% -Ddb.name=mysql
set properties=%properties% -Ddb.home=%DB_HOME%
set properties=%properties% -Ddb.start.cmd=startmysql.bat
set properties=%properties% -Ddb.stop.cmd=stopmysql.bat
set properties=%properties% -Ddb.exec.cmd=bin/mysql.exe
set properties=%properties% -Ddb.check.cmd=pingmysql.bat
set properties=%properties% -Ddb.root.password=%GRIDTALK_DB_PASSWORD%


rem application environment settings
set properties=%properties% -Dapplication.name=gtas
set properties=%properties% -Dapplication.home= %GRIDTALK_HOME%
set properties=%properties% -Dapplication.bin.dir=%GRIDTALK_HOME%/bin
set properties=%properties% -Dapplication.docs.dir=%GRIDTALK_HOME%/docs
set properties=%properties% -Dapplication.data.dir=%APPSERVER_HOME%/bin/gtas/data
set properties=%properties% -Dapplication.conf.dir=%APPSERVER_HOME%/bin/conf
set properties=%properties% -Dapplication.backend.dir=%GRIDTALK_HOME%/backend

set properties=%properties% -Dgtas.version.file=%GRIDTALK_HOME%/bin/gtas.version
set properties=%properties% -Dtarget.version.file=./application/bin/gtas.version

IF NOT EXIST %GRIDTALK_HOME%/bin/gtas.version (
  echo %curr.version%> %GRIDTALK_HOME%/bin/gtas.version
)


:upgrade
ECHO.
ECHO upgrading application data and files...
start /WAIT CMD /C ant %properties% -buildfile upgrade-main.xml -logfile antupg.log

:end
echo.
echo Upgrade process completed
echo.
echo Please review the upgrade logs after patching for any errors.
echo You may now close this window.
PAUSE >nul

rem -- converts the path to short name
:shortName
set shortname=%~fs1
goto :EOF

:abort
echo.
echo upgrade aborted
echo You may close this window.
PAUSE >nul
