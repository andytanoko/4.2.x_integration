@ECHO OFF
IF "%1"=="-?" GOTO usage
IF "%1"=="" GOTO usage

call .\startmysql.bat
..\mysql\bin\mysql -u root -p%1 --port=3306 -e "use mysql; update user set password=password('%2') where user='root'; flush privileges;"
if errorlevel 1 (
  call .\stopmysql.bat
  goto end
)

REM change the password first before stopmysql
.\mtr -nr setenv.bat - "GRIDTALK_DB_PASSWORD=%1" = "GRIDTALK_DB_PASSWORD=%2"

call .\stopmysql.bat

REM mtr.exe doesn't seem to recognize long filenames, so we have to copy mysql-ds.xml to local directory first
copy ..\jboss-3.2.2\server\default\deploy\mysql-ds.xml . 
.\mtr -nr mysql-ds.xml - "<password>%1</password>" = "<password>%2</password>"
move mysql-ds.xml ..\jboss-3.2.2\server\default\deploy\mysql-ds.xml

REM .\mtr -nr mysql-*.bat - "-p%1" = "-p%2"
echo Password changed successfully
GOTO end

:usage
echo *** Change mysql root password ***
echo Usage: %0 [current password] [new password]
echo Version: 4.0
REM echo Author: Guo Jianyu
REM echo Version: since GridTalk 2.4.8

:end