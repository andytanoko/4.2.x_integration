@echo off

echo Building AuditTrail...
start /DAuditTrail /I /WAIT ..\build.bat

echo Building GenReport...
start /DGenReport /I /WAIT ..\build.bat

echo Building HttpBackendConnector...
start /DHttpBackendConnector /I /WAIT ..\build.bat

echo Building txmr...
start /DPresentation /I /WAIT build-inovis.bat

echo Done.

