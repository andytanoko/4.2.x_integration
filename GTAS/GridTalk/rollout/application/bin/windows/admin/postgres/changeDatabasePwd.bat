@ECHO OFF
IF "%1"=="-?" GOTO usage
IF "%1"=="" GOTO usage

call ..\setenv.bat
call ..\%DB_STARTUP_CMD%
set SFK_CMD=%GRIDTALK_HOME%\bin\sfk.exe

IF "%1"=="1" (
  ..\..\pgsql\bin\psql --username postgres --port 35432 -c "ALTER USER postgres ENCRYPTED PASSWORD '%3';"
  if errorlevel 1 (
    call ..\%DB_SHUTDOWN_CMD%
	goto end
  )
  
  %SFK_CMD% replace "%APPDATA%\postgresql\pgpass.conf" "=localhost:35432:*:postgres:%2=localhost:35432:*:postgres:%3=" -yes
  
  call ..\%DB_SHUTDOWN_CMD%
  goto end
)

IF "%1"=="2" (
  ..\..\pgsql\bin\psql --username postgres --port 35432 -c "ALTER USER userdb ENCRYPTED PASSWORD '%3';ALTER USER userdb ENCRYPTED PASSWORD '%3';ALTER USER appdb ENCRYPTED PASSWORD '%3';ALTER USER jbossdb ENCRYPTED PASSWORD '%3';ALTER USER archivedb ENCRYPTED PASSWORD '%3';"
  if errorlevel 1 (
    call ..\%DB_SHUTDOWN_CMD%
	GOTO end
  )
  
  call ..\%DB_SHUTDOWN_CMD%

  rem new
  %SFK_CMD% replace "%APPSERVER_HOME%\server\default\deploy\gtas-ds.xml" "=<password>%2</password>=<password>%3</password>=" -yes
  rem end new

  echo Password changed successfully
  GOTO end
)

:usage
echo *** Change postgres root/DB role password ***
echo Usage: change PW option 1 : change default 'postgres' role password
echo Usage: change PW option 2 : change GTAS DB role password
echo Usage: %0 [PW option 1 or 2] [current password] [new password]
echo Version: 4.2
REM echo Author: Guo Jianyu
REM echo Version: since GridTalk 2.4.8

REM echo Modified by: Tam Wei Xiang since version GT4.2.1

:end
