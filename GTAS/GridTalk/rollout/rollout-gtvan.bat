@echo off

REM rollout GTVAN modules
SET GTVAN_DEPLOY_DIR=..\..\GT-VAN\deploy-env

xcopy /e /Y /H /I %GTVAN_DEPLOY_DIR%\bin\conf\*.* .\application\conf\server
xcopy /e /Y /H /I %GTVAN_DEPLOY_DIR%\bin\gtvan .\application\gtvan
xcopy /e /Y /H /I %GTVAN_DEPLOY_DIR%\jboss-4.0.5.GA\*.* .\appserver\jboss-4.0.5.GA\server\default

SET DIST_DIR=.\application\dist\jboss-4.0.5.GA
copy ..\..\GT-VAN\AuditTrail\dist\isat\gtvan-at-isat.ear %DIST_DIR%
copy ..\..\GT-VAN\HttpBackendConnector\dist\ishb\gtvan-httpbc-ishb.ear %DIST_DIR%
copy ..\..\GT-VAN\GenReport\dist\reportservice\gtvan-genreport-reportservice.ear %DIST_DIR%
REM xcopy /e /Y /H ..\..\GT-VAN\Presentation\txmr.war\*.* %DIST_DIR%\txmr.war\
copy ..\..\GT-VAN\Presentation\dist\txmr.war %DIST_DIR%

copy ..\..\GT-VAN\HttpBackendConnector\dist\gthb\gtvan-httpbc-gthb.jar .\application\data\sys\uproc\jar
