@echo off

SET CURR_DIR=%~d0%~p0
echo current dir is %CURR_DIR%
SET ADMIN_GUIDE=%CURR_DIR%..\GridTalk\doc\product\guides\GridTalk4.0_AdministrationGuide.htm
SET USER_GUIDE=%CURR_DIR%..\GridTalk\doc\product\guides\GridTalk4.0_UserGuide.htm
SET STYLESHEET=%CURR_DIR%gridnode.css
SET SPLIT_LEVEL=5

cd %CURR_DIR%web\gtas_ag
SET DEST_DIR=%CURR_DIR%web\gtas_ag
SET TITLE=GridTalk Administration Guide
SET SHORT_NAME=gtas_ag
java -Duser.dir="%CURR_DIR%web" -cp "../classes;../reqlib/jhall.jar" com.gridnode.helpmaker.HelpMaker "-xui" "%TITLE%" "%SHORT_NAME%" "%ADMIN_GUIDE%" "%DEST_DIR%" "%STYLESHEET%" "%SPLIT_LEVEL%"

cd %CURR_DIR%web\gtas_ug
SET DEST_DIR=%CURR_DIR%web\gtas_ug
SET TITLE=GridTalk User Guide
SET SHORT_NAME=gtas_ug
java -Duser.dir="%CURR_DIR%web" -cp "../classes;../reqlib/jhall.jar" com.gridnode.helpmaker.HelpMaker "-xui" "%TITLE%" "%SHORT_NAME%" "%USER_GUIDE%" "%DEST_DIR%" "%STYLESHEET%" "%SPLIT_LEVEL%"

cd %CURR_DIR%
