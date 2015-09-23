REM $1 Backup Dir
REM $2 Path to the patch's log file
REM $3 Backout command file

echo
echo Upgrading the ant 1.5.1  to ant 1.8.2
echo This script will:
echo a. install the new ant 1.8.2
echo b. move the existing ant 1.5.1 folder to Backup Dir
echo

SET workingDir=%CD%

REM copy the ant1.5 to the backup folder
cd %GRIDTALK_HOME%
cd ..
SET targetDir=%CD%
XCOPY /S /I /Q jakarta-ant-1.5.1-bin %workingDir%\%1\jakarta-ant-1.5.1-bin >> %2
RMDIR /S /Q jakarta-ant-1.5.1-bin >> %2

REM copy the ant1.8.2 into the GT install directory
cd %workingDir%
XCOPY /S /I /Q env\apache-ant-1.8.2 %targetDir%\apache-ant-1.8.2

REM include the rollback script
REM add to backout list so that the new ant can be backout
echo RMDIR /S /Q %targetDir%\apache-ant-1.8.2 >> %3
echo XCOPY /S /I /Q jakarta-ant-1.5.1-bin %targetDir%\jakarta-ant-1.5.1-bin >> %3

echo apache-ant is upgraded successfully >>%2
