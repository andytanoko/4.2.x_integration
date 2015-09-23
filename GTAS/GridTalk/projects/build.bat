@echo off
SET CLASSPATH=%JAVA_HOME%\jre\lib\plugin.jar;%CLASSPATH%

if "%1" =="gtas" goto build.gtas.module
if "%1" =="app" goto build.app.module
if "%1" =="base" goto build.base.module
if "%1" =="frmwrk" goto build.framework.module
if "%1" =="deploy" goto deploy
if "%1" =="" goto end

:build.gtas.module
REM set build.script=module-ant-build.xml
set build.script=gtas-module-build.xml
set lib.dir=""
set property.param="-Dgtas.module=%2"
if "%3" =="-xd" goto build.exclude.deplib
if "%3" =="" goto build.include.deplib
goto build.include.deplib

:build.app.module
@echo building app service layer module
cd ..\..\..\PDIPAppServices\%2\projects
set build.script=ant-build.xml
set lib.dir=..\deploy
set property.param="-Dmodule.properties=%2-build.properties"
if "%3" =="-xd" goto build.exclude.deplib
if "%3" =="" goto build.include.deplib


:build.base.module
@echo building app service layer module
cd ..\..\..\PDIPBaseServices\%2\projects
set build.script=ant-build.xml
set lib.dir=..\deploy
set property.param="-Dmodule.properties=%2-build.properties"
if "%3" =="-xd" goto build.exclude.deplib
if "%3" =="" goto build.include.deplib

:build.framework.module
@echo building framework layer module
cd ..\..\..\PDIPFramework\Framework\projects
set build.script=ant-build.xml
set lib.dir=..\deploy
set property.param="-Dmodule.properties=%2-build.properties"
goto build.exclude.deplib

:build.include.deplib
@echo building module %2% including dependent modules ........
REM start cmd.exe /k ant -quiet -Dbuild.default=defined -Dmodule.properties=%2-build.properties -buildfile %build.script% build
start cmd.exe /k ant -quiet -Dbuild.default=defined %property.param% -buildfile %build.script% build
goto copy.lib

:build.exclude.deplib
@echo building module %2% excluding dependent modules ........
REM start cmd.exe /k ant -quiet -Dbuild.default=defined -Dmodule.properties=%2-build.properties -Dno.deplib=defined -buildfile %build.script% build
start cmd.exe /k ant -quiet -Dbuild.default=defined %property.param% -Dno.deplib=defined -buildfile %build.script% build
goto copy.lib


:copy.lib
echo Press any key to continue ....
pause > nul
if %lib.dir% =="" goto deploy
@echo Copying jars and wars to top level deplib folder
cd %lib.dir%
copy *.jar ..\..\..\GTAS\GridTalk\deplib
copy *.war ..\..\..\GTAS\GridTalk\deplib
cd ..\..\..\GTAS\GridTalk\projects
goto deploy

:deploy
@echo Deploying to appserver ........
ant -quiet -buildfile gtas-ant-build.xml deploy
goto end

:end
