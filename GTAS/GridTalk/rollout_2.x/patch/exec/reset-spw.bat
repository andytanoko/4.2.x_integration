@echo off

REM RUN THIS BATCH FILE TO UPDATE THE CERTIFICATE ENTITY IN USERDB DATABASE.
REM THE ISSUERNAME IS PATCHED WITH THE NEW ISSUERNAME IF AND ONLY IF ISSUERNAME
REM RETRIEVED FROM X509CERTIFICATE IS INCONSISTENT WITH ENTITY VALUE.

:startdb
echo starting database
call %GRIDTALK_HOME%\bin\startmysql.bat

:patching database 
java -jar reset-spw.jar

:stopdb
echo shutting down database
call %GRIDTALK_HOME%\bin\stopmysql.bat

:end
exit



