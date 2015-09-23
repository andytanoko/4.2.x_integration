@echo off

if "%1"=="" goto usage

CALL %GRIDTALK_HOME%/bin/setenv.bat
%JAVA_HOME%\bin\java -jar MaskedPassword.jar %1
GOTO :EOF

:usage
echo.
echo maskedPass - mask the plain text password
echo.
echo Usage: 
echo. 
echo maskedPass.bat password
echo.
GOTO :EOF