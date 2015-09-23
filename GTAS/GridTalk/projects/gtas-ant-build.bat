@echo off

SET CLASSPATH=%JAVA_HOME%\jre\lib\plugin.jar;%CLASSPATH%

:module.1 
if  "%1"==""  goto default
set module="%1"
set return=2
goto module.build

:module.2
if "%2"=="" goto deploy
set module="%2"
set return=3
goto module.build

:module.3
if "%3"=="" goto deploy
set module="%3"
set return=4
goto module.build

:module.4
if "%4"=="" goto deploy
set module="%4"
set return=5
goto module.build

:module.5
if "%5"=="" goto deploy
set module="%5"
set return=6
goto module.build

:module.6
goto end

:deploy
@echo Deploying GTAS to appserver ........
ant -quiet -buildfile gtas-ant-build.xml deploy
goto end

:default
ant -quiet -buildfile gtas-ant-build.xml build
goto end

:module.build
@echo Building GTAS module %module% ..........
if %module%=="common" goto common.build
REM start cmd.exe /k ant -quiet -Dbuild.default=defined -Dmodule.properties=%module%-build.properties -buildfile module-ant-build.xml build
start cmd.exe /k ant -quiet -Dbuild.default=defined -Dgtas.module=%module% -buildfile gtas-module-build.xml
echo Press any key to build the next module ....
pause > nul
goto module.%return%

:common.build
start cmd.exe /k ant -quiet -Dbuild.default=defined -Dmodule.properties=%module%-build.properties -buildfile common-ant-build.xml build
echo Press any key to build the next module ....
pause > nul
goto module.%return%

:end

