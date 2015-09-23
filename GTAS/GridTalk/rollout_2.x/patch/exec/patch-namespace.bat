@echo off
set mapfile=namespace.map
set APPLICATION_DATA_DIR=%APPSERVER_HOME%\bin\gtas\data
set APPLICATION_DATA_DIR=%APPLICATION_DATA_DIR:\=/%

if exist "%APPLICATION_DATA_DIR%/sys/entity/connection-setup.xml" (
  echo patching files using %mapfile%
  attrib -r /s "%APPLICATION_DATA_DIR%/sys/entity/connection-setup.xml"

  call :shortName "%APPLICATION_DATA_DIR%"
  set APPLICATION_DATA_DIR=%shortname%

  mtr -b:* -n -i:%mapfile% %APPLICATION_DATA_DIR%/sys/entity/connection-setup.xml
)

:end
exit

rem -- converts the path to short name
:shortName
set shortname=%~fs1
goto :EOF

