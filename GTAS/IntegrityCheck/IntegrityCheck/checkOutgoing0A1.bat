@echo off
REM Param1 = "OutProcessIntegrityReport"
REM Param2 = document type to check
REM Param3 = request document type, ProcessDefname append with _Request
REM Param4 = process start datetime YYYY-MM-DD HH:MM:SS (UTC)
REM Param5 = process end datetime YYYY-MM-DD HH:MM:SS (UTC)

call check.bat "OutProcessIntegrityReport" "RN_FAILNOTF2" "0A1FailureNotification_Request" "2003-11-01 16:00:00" "2004-06-30 16:00:00"
