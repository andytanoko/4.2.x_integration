@ECHO OFF
ECHO ===========================
ECHO IntegrityCheck Build Script
ECHO ===========================
:setParams
ECHO Setting parameters...
SET ANT_BUILD_FILE=build.xml
SET ANT_TARGET=

IF "%1" NEQ "" (
	SET ANT_BUILD_FILE=%1
)

IF "%2" NEQ "" (
	SET ANT_TARGET=%2
)

IF %ANT_HOME% == "" GOTO :noAnt
IF NOT EXIST %ANT_BUILD_FILE% GOTO :noBuildFile
GOTO :startAnt

:startAnt
ECHO Ant building IntegrityCheck...
ant -buildfile %ANT_BUILD_FILE% %ANT_TARGET%

IF %ERRORLEVEL% NEQ 0 GOTO :antError
ECHO Ant building completed successfully.
:GOTO :end

:noAnt
ECHO ANT_HOME not defined. Aborted.
GOTO :end

:noBuildFile
ECHO Ant build file not found. Aborted.
GOTO :end

:antError
ECHO Error in Ant building. Aborted.
:GOTO :end

:end
pause