USE appdb;

DELETE FROM entitymetainfo WHERE EntityName IN ('HousekeepingInfo');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo', 'HousekeepingInfo', NULL);

# HousekeepingInfo
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%HousekeepingInfo';
INSERT INTO fieldmetainfo VALUES
(NULL, '_tempFilesDaysToKeep', 'TEMP_FILES_DAYS_TO_KEEP', NULL, 'java.lang.Integer', 'com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo', 'houseKeeping.tempFilesDaysToKeep', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\nmandatory=true\r\neditable=true', 'type=range\r\nrange.min=0');
INSERT INTO fieldmetainfo VALUES
(NULL, '_logFilesDaysToKeep', 'LOG_FILES_DAYS_TO_KEEP', NULL, 'java.lang.Integer', 'com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo', 'houseKeeping.logFilesDaysToKeep', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\nmandatory=true\r\neditable=true', 'type=range\r\nrange.min=0');
INSERT INTO fieldmetainfo VALUES
(NULL, '_wfRecordsDaysToKeep', 'WF_RECORDS_DAYS_TO_KEEP', NULL, 'java.lang.Integer', 'com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo', 'houseKeeping.wfRecordsDaysToKeep', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\nmandatory=true\r\neditable=true', 'type=range\r\nrange.min=0');