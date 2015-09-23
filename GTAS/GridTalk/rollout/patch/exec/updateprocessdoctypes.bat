@echo off

REM call ..\..\startmysql.bat
REM call ..\..\pingmysql.bat "gtasdb"

echo.>> %1
echo Updating Process Doc Types... >> %1

echo JAVA_HOME is %JAVA_HOME% >> %1
java -jar updateprocessdoctypes.jar >> %1 2>> %2

echo Finished updating >> %1

REM call ..\..\stopmysql.bat "gtasdb"

exit

