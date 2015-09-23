@echo off

echo Migrating cacerts from old JRE to new JRE... >> %1

java -jar migratecacerts.jar %GRIDTALK_HOME%\jre142_05\lib\security\cacerts %JAVA_HOME%\lib\security\cacerts >> %1

echo Finished migration >> %1

exit

