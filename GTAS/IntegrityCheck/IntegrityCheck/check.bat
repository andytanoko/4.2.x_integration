@echo off
SET PROGRAM=%1
SET DOC_TYPE=%2
SET REQ_DOC_TYPE=%3
SET BETWEEN_DT1=%4
SET BETWEEN_DT2=%5

SET CLASSPATH=.\integrity-check.jar

SET SQL=select distinct(processuid) from rtprocess a ,rtprocessdoc b where a.processtype='BpssBinaryCollaboration' and a.uid=b.rtbinarycollaborationuid and requestdoctype='%REQ_DOC_TYPE%';
%GRIDTALK_HOME%\mysql\bin\mysql -u root -Pgtasdb -e "use userdb;%SQL%" > %DOC_TYPE%.txt

java -cp "%CLASSPATH%" %PROGRAM% %DOC_TYPE% %BETWEEN_DT1% %BETWEEN_DT2%
