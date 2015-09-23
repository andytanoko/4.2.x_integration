@echo off

rem ==================== Database ==============================

@echo This will remove all registration-related configurations settings from database 
@echo Please shutdown the database if it was started
@echo Hit Ctrl-C to abort or any key to continue
pause 
call mysql-patch unregister.sql 

rem ==================== Config & Data Files =======================

@echo Removing configurations and data files

@echo Removing Connection Setup
if exist ..\jboss-3.0.5_tomcat-4.0.6\bin\gtas\data\sys\entity\connection-setup.xml (
  del ..\jboss-3.0.5_tomcat-4.0.6\bin\gtas\data\sys\entity\connection-setup.xml /f /q
)

if exist ..\jboss-3.2.2\bin\gtas\data\sys\entity\connection-setup.xml (
  del ..\jboss-3.2.2\bin\gtas\data\sys\entity\connection-setup.xml /f /q
)

if exist ..\jboss-4.0.2\bin\gtas\data\sys\entity\connection-setup.xml (
  del ..\jboss-4.0.2\bin\gtas\data\sys\entity\connection-setup.xml /f /q
)

rem ================= 24 Jan 06 [SC] remove more info for GT 4.0 ====================

rem TEST

@echo Performing further clean up for GT 4.0

rem change to GT folder
cd ..

setlocal
path=bin;%path%

call myrmdir jboss-4.0.2\bin\gtas\data\doc

rem remove log files

call mydel jboss-4.0.2\server\default\log
call mydel jboss-4.0.2\bin\gtas\data\log

call mydel jboss-4.0.2\bin\gtas\data\temp

call myrmdir jboss-4.0.2\bin\port

rem remove some contents of alert folder
call mydel jboss-4.0.2\bin\gtas\data\alert\email\failed
call mydel jboss-4.0.2\bin\gtas\data\alert\email\retry
call mydel jboss-4.0.2\bin\gtas\data\alert\log

rem BEGIN: del everything except 3 gridmaster certs
rem assume that jboss-4.0.2\bin\gtas\data\sys\gridmaster_certs_temp folder doesn't contains anything. 
rem This is a temporary folder used to store 3 gridmaster certs
set CERT_TEMP=jboss-4.0.2\bin\gtas\data\sys\gridmaster_certs_temp
set CERT_DEST=jboss-4.0.2\bin\gtas\data\sys\cert
if exist %CERT_TEMP% (
	call rmdir %CERT_TEMP% /s /q
)
mkdir %CERT_TEMP%
copy %CERT_DEST%\GridMasterLive.cer %CERT_TEMP%
copy %CERT_DEST%\GridMasterTest.cer %CERT_TEMP% 
copy %CERT_DEST%\GridMasterTrial.cer %CERT_TEMP%
call mydel %CERT_DEST%
copy %CERT_TEMP%\GridMasterLive.cer %CERT_DEST%
copy %CERT_TEMP%\GridMasterTest.cer %CERT_DEST% 
copy %CERT_TEMP%\GridMasterTrial.cer %CERT_DEST%
call myrmdir %CERT_TEMP%

rem END: del everything except 3 gridmaster certs

call mydel jboss-4.0.2\bin\gtas\data\sys\license

rem copy keystore file
copy jboss-4.0.2\bin\gtas\data\keystore\keystore data\keystore /y

endlocal
cd bin
@echo Unregistration successful!