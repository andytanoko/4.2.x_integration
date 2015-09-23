@echo off

REM RUN THIS BATCH FILE TO CONFIGURE EmailConfig FROM mail.properties

:patching EmailConfig 
java -jar patch-emailconfig.jar %APPSERVER_HOME%\bin\

:end
exit
