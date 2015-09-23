@ECHO OFF

REM RUN THIS BATCH FILE TO UPDATE THE RN_PROFILE TABLE DEFINITION AND FIELDMETAINFO FOR
REM RNIFVersion AND InResponseToActionID FIELDS THAT COULD HAVE FAILED 
REM TO BE ADDED IN GT_2.1.9 AND GT_2.1.13 PATCHES.

:startdb
ECHO.
ECHO Starting database
ECHO.
CALL %GRIDTALK_HOME%\bin\startmysql.bat

REM SAVE CURRENT DIRECTORY AND CD TO DB_HOME
PUSHD %DB_HOME%
SET TEMPF=TEMP.LOG

:check_table
ECHO.
ECHO Checking rn_profile table definition
bin\mysql -e"use userdb; show columns from rn_profile;" > %TEMPF%

:check_col_1
SET COLUMN=RNIFVersion
SET DEFN=varchar(80) default NULL
CALL :checkcolumn

:check_col_2
SET COLUMN=InResponseToActionID
SET DEFN=varchar(80) default NULL
CALL :checkcolumn

:check_fmi
ECHO.
ECHO Checking fieldmetainfo for rn_profile
bin\mysql -e"use appdb; select FieldName from fieldmetainfo where FieldName IN('RNIF_VERSION','IN_RESPONSE_TO_ACTION_ID') AND EntityObjectName='com.gridnode.gtas.server.rnif.model.RNProfile';" > %TEMPF%

:check_fmi_1
SET FIELDN=RNIF_VERSION
SET OBJECTN=_RNIFVersion
SET SQLN=RNIFVersion
SET LABELN=RNIFVersion
SET SIZE=80
CALL :checkmetainfo

:check_fmi_2
SET FIELDN=IN_RESPONSE_TO_ACTION_ID
SET OBJECTN=_inResponseToActionID
SET SQLN=InResponseToActionID
SET LABELN=inResponseToActionID
SET SIZE=80
CALL :checkmetainfo

:cleanup
IF EXIST %TEMPF% (
  DEL /Q %TEMPF%
)

REM CD TO ORIGINAL DIRECTORY
POPD

:stopdb
ECHO.
ECHO Shutting down database
ECHO.
CALL %GRIDTALK_HOME%\bin\stopmysql.bat

:end
EXIT

:checkcolumn
ECHO.
ECHO checkcolumn with %COLUMN% %DEFN%
ECHO.
FINDSTR /I /C:"%COLUMN%" %TEMPF%
IF NOT ERRORLEVEL 1 GOTO end_
ECHO %COLUMN% column is not present in rn_profile, trying to add
bin\mysql -e"use userdb; ALTER TABLE rn_profile ADD COLUMN %COLUMN% %DEFN%;"
GOTO end_

:checkmetainfo
ECHO.
ECHO checkmetainfo with %FIELDN% %OBJECTN% %SQLN% %LABELN% %SIZE%
ECHO.
FINDSTR /I /C:"%FIELDN%" %TEMPF%
IF NOT ERRORLEVEL 1 GOTO end_
ECHO %FIELDN% is not present in fieldmetainfo, trying to insert
bin\mysql -e"use appdb; INSERT INTO fieldmetainfo VALUES (NULL,'%OBJECTN%','%FIELDN%','%SQLN%','java.lang.String','com.gridnode.gtas.server.rnif.model.RNProfile','RNProfile.%LABELN%',%SIZE%,'0','0','1','1','',999,NULL,NULL);"
GOTO end_

:end_
endlocal
