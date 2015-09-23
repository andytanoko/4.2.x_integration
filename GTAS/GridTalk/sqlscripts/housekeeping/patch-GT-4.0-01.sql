#[Tam Wei Xiang]                 27 June 2006             Added new field 'wfRecordsDaysToKeep'

USE appdb;
INSERT INTO fieldmetainfo VALUES
(NULL, '_wfRecordsDaysToKeep', 'WF_RECORDS_DAYS_TO_KEEP', NULL, 'java.lang.Integer', 'com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo', 'houseKeeping.wfRecordsDaysToKeep', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\nmandatory=true\r\neditable=true', 'type=range\r\nrange.min=0');