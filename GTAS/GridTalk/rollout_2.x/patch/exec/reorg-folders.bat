@echo off

REM RUN THIS BATCH FILE TO REORG THE DOCUMENTS INTO RESPECTIVE SYSTEM FOLDERS

:patching database 
java -jar reorg-folders.jar %APPSERVER_HOME%\bin\gtas\data\doc\gdoc

:end
exit
