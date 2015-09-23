@echo off

:new patch logic
SET logfile=patchlog.txt

echo Please review the patch log after patching for any errors

if NOT EXIST %GRIDTALK_HOME% (
  echo No installed GridTalk found at GRIDTALK_HOME: %GRIDTALK_HOME%
  echo Patch aborted.
  GOTO :EOF
)

echo Patching GridTalk at this location %GRIDTALK_HOME%

CALL %GRIDTALK_HOME%\bin\setenv.bat

echo The GridTalk application must be shutdown before patching. Please shut it down now if you have not done so.
set /p PROCEED=Are you ready to proceed for patching (y/n)? %=%

if /I '%PROCEED%' NEQ 'y' (
  echo Patch is cancelled.
  GOTO :EOF
)

rem create backup dir
SET BACKUP_DIR=backup_%RANDOM%_%RANDOM%
mkdir %BACKUP_DIR%

SET BACKUP_APP=%BACKUP_DIR%\gridtalk
mkdir %BACKUP_APP%

SET BACKOUT_LIST=%BACKUP_DIR%\backout.list
SET BACKOUT_BAT=%BACKUP_DIR%\backout.bat

rem start patching
SET properties=-Dpatch.properties=patch-win.properties -Dbackup.dir="%BACKUP_APP%" -Dbackout.list="%BACKOUT_LIST%"
SET properties=-Dgtas.version.file="%GRIDTALK_HOME%\bin\gtas.version" %properties%

if /I '%1%' EQU '-data' (
  set properties=-Dpatch.commondata="yes" %properties%
)

echo Patching starts...
copy %GRIDTALK_HOME%\bin\gtas.version %BACKUP_DIR%
CALL ant %properties% -buildfile patch-main.xml -logfile %logfile%

if errorlevel 1 (
  echo Some problems found during patching. Please check the log file %logfile% for details.
  GOTO :EOF
)

echo "Creating backout script: %BACKOUT_BAT% ..."
echo "echo Performing backout using %BACKOUT_BAT%" > %BACKOUT_BAT%
FOR /F %%s in (%BACKOUT_LIST%) do (
    echo del /F %%s >> %BACKOUT_BAT%
)

echo xcopy /E /Y /H /I gridtalk\* %GRIDTALK_HOME% >> %BACKOUT_BAT%
echo copy gtas.version %GRIDTALK_HOME%\bin >> %BACKOUT_BAT%

FOR /F %%v in (%BACKUP_DIR%\gtas.version) do (
	echo echo GridTalk reverted back to %%v >> %BACKOUT_BAT%
)

echo Patch completed

:EOF

