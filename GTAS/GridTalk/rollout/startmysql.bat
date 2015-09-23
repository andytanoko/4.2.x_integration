@echo off
call setenv.bat
start /D%DB_HOME% /MIN .\bin\mysqld --basedir=. --datadir=./data --port=3306 --default-character-set=utf8 --default-collation=utf8_unicode_ci
exit /b



