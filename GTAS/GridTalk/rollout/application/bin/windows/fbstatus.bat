@ECHO OFF
IF "%1"=="-?" GOTO usage

REM Set the input variables
SET SEARCH_DIR=%1
SET FEEDBACK_BAT=%2
SET FEEDBACK_BAT_ARG=%3 %4 %5 %6 %7 %8 %9

REM Set default SEARCH_DIR if no input
IF "%SEARCH_DIR%"=="" (
  SET SEARCH_DIR=%GRIDTALK_HOME%\jboss-3.2.2\bin\gtas\data\alert\log\docStatus
)
SET SEARCH_PATTERN="%SEARCH_DIR%\Outbound-*-*.xml"
SET SEARCH_STRING="</GridDocument>"

REM Set default FEEDBACK_BAT if no input
if "%FEEDBACK_BAT%"=="" (
  SET FEEDBACK_BAT="%XB_HOME%\receiver\gridappimport.bat"
)

REM Set default FEEDBACK_BAT_ARG if no input
if "%FEEDBACK_BAT_ARG%"=="      " (
  SET FEEDBACK_BAT_ARG=-ATTACHMENTS %%1 -A UPDATE
)

REM Proceed to search
ECHO.
ECHO Searching Gdoc files to update Gdoc status to XB...
ECHO.
SET PROCESSED_DIR=processed
IF NOT EXIST %PROCESSED_DIR%\ MKDIR %PROCESSED_DIR%
SET MATCH_LIST=matchlist.txt
REM IF NOT EXIST %MATCH_LIST% 
SET PROCESSED_LIST=processedlist.txt

REM Process each matched file
FINDSTR /M /P /C:%SEARCH_STRING% %SEARCH_PATTERN% >>%MATCH_LIST%
FOR /F %%I IN (%MATCH_LIST%) DO CALL :do_process "%%I" "%%~nxI"
CALL :do_cleanup

ECHO.
ECHO Search Ended
ECHO.
GOTO end

:do_process
REM Move file to processed directory
IF EXIST %1 (
  MOVE %1 %PROCESSED_DIR%
  IF EXIST "%PROCESSED_DIR%\%2" (
    DIR /S/B/A:-d "%PROCESSED_DIR%\%2" >>%PROCESSED_LIST%
  )
) 

REM Call FEEDBACK_BAT for each processed filename
FOR /F %%I IN (%PROCESSED_LIST%) DO CALL :do_feedbackOne "%%~fI"
GOTO end

:do_feedbackOne
IF EXIST %1 (
  ECHO.
  ECHO Feedback Status File: %1
  CALL %FEEDBACK_BAT% %FEEDBACK_BAT_ARG%
)
GOTO end

:do_cleanup
ECHO.
ECHO Cleanup temporary files...
DEL /Q %MATCH_LIST%,%PROCESSED_LIST%
GOTO end

:usage
ECHO.
ECHO Search Gdoc status files and update to XB
ECHO Usage: %0 [search_dir [feedback_bat [feedback_args]]]
ECHO   search_dir        Pathname to the directory containing the Gdoc status files.
ECHO   feedback_bat      Pathname to the batch file to call for feedback.
ECHO   feedback_args     Commandline arguments to be passed to the feedback batch programs
ECHO                     use %1 to refer to the Gdoc status file. Up to 7 arguments. 
REM  Author: Neo Sok Lay
REM  Version: GT 2.1 v2

:end
ENDLOCAL