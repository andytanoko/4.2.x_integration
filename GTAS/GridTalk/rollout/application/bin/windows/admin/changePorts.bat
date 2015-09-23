@echo off
set mapfile=%1
if  "%1"=="" set mapfile=ports.map 

set APP_BASE=%GRIDTALK_HOME%
CALL %GRIDTALK_HOME%\bin\setenv.bat

echo Mapping ports using %mapfile%
attrib -r /s %APP_BASE%/bin/*.bat
attrib -r /s %APP_BASE%/data/GNapps/conf/default/*.*

call :shortName "%APP_BASE%"
set APP_BASE=%shortname%
echo App: %APP_BASE%
echo Jboss: %APPSERVER_HOME%

set PORT_MAP_FILE=%GRIDTALK_HOME%\bin\admin\%mapfile%
set SFK_CMD=%GRIDTALK_HOME%\bin\sfk.exe
set DATA_HOME=%APP_BASE%\data

REM new
%SFK_CMD% replace -bylist %PORT_MAP_FILE% -nosub -dir "%APP_BASE%\bin" -file .bat -yes
%SFK_CMD% replace -bylist %PORT_MAP_FILE% -nosub -dir "%APP_BASE%\bin\admin" -file .bat -yes 
%SFK_CMD% replace -bylist %PORT_MAP_FILE% -dir "%DATA_HOME%\GNapps\conf\default" -yes
%SFK_CMD% replace -bylist %PORT_MAP_FILE% -dir "%APPSERVER_HOME%\server\default\conf" -yes
%SFK_CMD% replace -bylist %PORT_MAP_FILE% -nosub -dir "%APPSERVER_HOME%\server\default\deploy" -file .xml -yes
%SFK_CMD% replace -bylist %PORT_MAP_FILE% -dir "%APPSERVER_HOME%\server\default\deploy\jboss-web.deployer" -file .xml -yes
%SFK_CMD% replace -bylist %PORT_MAP_FILE% -dir "%APPSERVER_HOME%\server\default\deploy\jms" -file .xml -yes
%SFK_CMD% replace -bylist %PORT_MAP_FILE% -dir "%APPSERVER_HOME%\server\default\deploy\http-invoker.sar\META-INF" -file .xml -yes
%SFK_CMD% replace -bylist %PORT_MAP_FILE% "%APP_BASE%\db\postgresql.conf" -yes
REM end new

:end
endlocal

rem -- converts the path to short name
:shortName
set shortname=%~fs1
goto :EOF
