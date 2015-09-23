SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('StringData','ByteData','ContextData');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.contextdata.entities.model.StringData','StringData','"data_stringdata"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.contextdata.entities.model.ByteData','ByteData','"data_bytedata"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.contextdata.entities.model.ContextData','ContextData','"data_contextdata"');

--------- StringData
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%StringData';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.contextdata.entities.model.StringData','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_data','DATA','"Data"','java.lang.String','com.gridnode.pdip.base.contextdata.entities.model.StringData','DATA',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataType','DATA_TYPE','"DataType"','java.lang.String','com.gridnode.pdip.base.contextdata.entities.model.StringData','DATA_TYPE',50,0,1,1,1,'',2,'displayable=false','');

--------- ByteData
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ByteData';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.contextdata.entities.model.ByteData','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_data','DATA','"Data"','byte[]','com.gridnode.pdip.base.contextdata.entities.model.ByteData','DATA',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataType','DATA_TYPE','"DataType"','java.lang.String','com.gridnode.pdip.base.contextdata.entities.model.ByteData','DATA_TYPE',50,0,1,1,1,'',2,'displayable=false','');

--------- ContextData
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ContextData';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.contextdata.entities.model.ContextData','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_contextUId','CONTEXT_UID','"ContextUId"','java.lang.Long','com.gridnode.pdip.base.contextdata.entities.model.ContextData','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_contextData','CONTEXT_DATA','"ContextData"','byte[]','com.gridnode.pdip.base.contextdata.entities.model.ContextData','',0,0,0,0,0,'',999,'displayable=false','');
