CONNECT APPDB/gridnode;

-- TWX20070617 Update for archive by customer

-- FMI for archiveProcess
DELETE FROM "entitymetainfo" WHERE "EntityName"='GWFArchiveProcess';
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','GWFArchiveProcess','"archive_process_view"');

DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" = 'com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"RTProcessUID"','java.lang.Long','com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_engineType','ENGINE_TYPE','"EngineType"','java.lang.String','com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','',30,'0','0','1','1','',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_processType','PROCESS_TYPE','"ProcessType"','java.lang.String','com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','',50,'0','0','1','1','',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_processStartTime','PROCESS_START_TIME','"StartTime"','java.util.Date','com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_processEndTime','PROCESS_END_TIME','"EndTime"','java.util.Date','com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_processUID','PROCESS_UID','"ProcessUId"','java.lang.Long','com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_rtprocessDocUID','RT_PROCESS_DOC_UID','"RTProcessDocUID"','java.lang.Long','com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','','0','0','0','0','0','','999','displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_customerBEId','CUSTOMER_BE_ID','"CustomerBEId"','java.lang.String','com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','CUSTOMER_BE_ID','4','0','0','0','0','','0','displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_partnerKey','PARTNER_KEY','"PartnerKey"','java.lang.String','com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','PARTNER_KEY','50','0','1','1','1','','3','displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_processStatus','PROCESS_STATUS','"ProcessStatus"','java.lang.Integer','com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','STATUS','0','0','0','0','0','','6','displayable=false','');

COMMIT;