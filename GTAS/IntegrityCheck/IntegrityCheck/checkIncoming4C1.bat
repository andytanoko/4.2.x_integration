@echo off
REM Param1 = "InProcessIntegrityReport"
REM Param2 = document type to check
REM Param3 = request document type, ProcessDefname append with _Request
REM Param4 = process start datetime YYYY-MM-DD HH:MM:SS (UTC)
REM Param5 = process end datetime YYYY-MM-DD HH:MM:SS (UTC)

call check.bat "InProcessIntegrityReport" "4C1RN" "4C1Maxtor_Request" "2003-11-25 16:00:00" "2003-11-30 16:00:00"
