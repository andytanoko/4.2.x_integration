@echo off
REM Param1 = "OutProcessIntegrityReport"
REM Param2 = document type to check
REM Param3 = request document type, ProcessDefname append with _Request
REM Param4 = process start datetime YYYY-MM-DD HH:MM:SS (UTC)
REM Param5 = process end datetime YYYY-MM-DD HH:MM:SS (UTC)

call check.bat "OutProcessIntegrityReport" "3C3RN" "3C3_Request" "2003-11-26 16:00:00" "2003-11-27 16:00:00"
