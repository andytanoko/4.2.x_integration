@echo off
SET GT_REPORT=acked_wexport_list.csv
SET XB_REPORT=docheader.csv

java -cp "." ReconcileCheck %GT_REPORT% %XB_REPORT%
