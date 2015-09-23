set db=postgres

echo off
echo cleaning up ......
if exist .\application\db\%db% del /S /Q .\application\db\%db%\*.*
if exist .\application\db\%db% rmdir /S /Q .\application\db\%db%
mkdir .\application\db\%db%

if exist .\application\data del /S /Q .\application\data\*.*
if exist .\application\data rmdir /S /Q .\application\data
mkdir .\application\data

if exist .\application\conf\server del /S /Q .\application\conf\server\*.*
if exist .\application\conf\server rmdir /S /Q .\application\conf\server
mkdir .\application\conf\server

if exist .\application\dist del /S /Q .\application\dist\*.*
if exist .\application\dist rmdir /S /Q .\application\dist
mkdir .\application\dist

if exist .\appserver del /S /Q .\appserver\*.*
if exist .\appserver rmdir /S /Q .\appserver
mkdir .\appserver

REM if exist .\webserver del /S /Q .\webserver\*.*
REM if exist .\webserver rmdir /S /Q .\webserver
REM mkdir .\webserver

if exist .\application\backend\receiver del /Q .\application\backend\receiver\*.*
if exist .\application\backend\sender del /Q .\application\backend\sender\*.*
if exist .\application\backend\ibtransporter del /Q .\application\backend\ibtransporter\*.*

echo setting up the application files .....
xcopy /e /Y /H /I ..\run\sqlscripts .\application\db\%db%
xcopy /e /Y /H /I ..\run\data .\application\data
xcopy /e /Y /H /I ..\run\conf .\application\conf\server
REM xcopy /e /Y /H /I ..\conf\client\default .\application\conf\client\default
xcopy /e /Y /H /I /EXCLUDE:rollout.excludes.txt ..\conf\appserver .\appserver
REM xcopy /e /Y /H /I ..\conf\webserver .\webserver
xcopy /e /Y /H /I /EXCLUDE:rollout.excludes.txt ..\dist .\application\dist

echo setting up the backend module files .....
xcopy /e /Y /H /I ..\data\backend .\application\backend
copy ..\deploy\receiver.jar .\application\backend\receiver\receiver.jar
copy ..\deploy\sender.jar .\application\backend\sender\sender.jar
copy ..\deploy\senderlite.jar .\application\backend\sender\senderlite.jar

xcopy /e /Y /H /I ..\..\Customers\Extensions\programs\gt-ibtransporter .\application\backend\ibtransporter
