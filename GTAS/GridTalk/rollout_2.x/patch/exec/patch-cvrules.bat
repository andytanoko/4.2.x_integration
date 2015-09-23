@echo off
set mapfile=cvrule.map
set APPLICATION_DATA_DIR=%APPSERVER_HOME%\bin\gtas\data
set APPLICATION_DATA_DIR=%APPLICATION_DATA_DIR:\=/%

echo patching conversion rules using %mapfile%
attrib -r /s "%APPLICATION_DATA_DIR%/sys/mapping/cvrules/*.*"
attrib -r /s "%APPLICATION_DATA_DIR%/sys/mapping/xsl/*.*"

call :shortName "%APPLICATION_DATA_DIR%"
set APPLICATION_DATA_DIR=%shortname%

mtr -xb-rnki:%mapfile% %APPLICATION_DATA_DIR%/sys/mapping/cvrules/*.*
mtr -xb-rnki:%mapfile% %APPLICATION_DATA_DIR%/sys/mapping/xsl/*.*

:end
exit

rem -- converts the path to short name
:shortName
set shortname=%~fs1
goto :EOF

