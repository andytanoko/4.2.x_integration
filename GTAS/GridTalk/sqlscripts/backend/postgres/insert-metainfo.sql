SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

--
-- Dumping data for table 'entitymetainfo'
--
-- Field 1 ObjectName: full qualified class name of the entity
-- Field 2 EntityName: short class name of the entity
-- Field 3 SqlName: table name of the entity

-- entitymetainfo for Port, Rfc
DELETE FROM "entitymetainfo" WHERE "ObjectName" IN ('com.gridnode.gtas.server.backend.model.Port','com.gridnode.gtas.server.backend.model.Rfc');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.gtas.server.backend.model.Port','Port','"port"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.gtas.server.backend.model.Rfc','Rfc','"rfc"');

--
-- Dumping data for table 'fieldmetainfo'
--
-- UID bigint(20) NOT NULL auto_increment,
-- ObjectName: field name in Entity class ,
-- FieldName: field ID in Entity Interface class ,
-- SqlName: Field column name in Table,
-- ValueClass: field data type,
-- EntityObjectName: full qualified class name of the entity
-- Label: field display label
-- Length: valid field length ,
-- Proxy: '1' if proxy, '0' otherwise,,
-- Mandatory: '1' if mandatory, '0' otherwise,
-- Editable: '1' if editable, '0' otherwise
-- Displayable: '1' if displayable, '0' otherwise
-- OqlName: ,
-- Sequence: default '999' ,
-- Presentation: properties for presentation of the field
-- Constraints: constraint imposed on the values of the field
--

-- fieldmetainfo for trigger
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName"='com.gridnode.gtas.server.backend.model.Port';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.gtas.server.backend.model.Port','port.uid',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=uid');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.gtas.server.backend.model.Port','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.gtas.server.backend.model.Port','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'candelete.enabled=true'||chr(13)||chr(10)||'candelete.disabled=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_name','NAME','"PortName"','java.lang.String','com.gridnode.gtas.server.backend.model.Port','port.name',15,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true','type=text'||chr(13)||chr(10)||'text.length.max=15');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.gtas.server.backend.model.Port','port.description',80,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_isRfc','IS_RFC','"IsRfc"','java.lang.Boolean','com.gridnode.gtas.server.backend.model.Port','port.isRfc',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'port.isRfc.true=true'||chr(13)||chr(10)||'port.isRfc.false=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_rfc','RFC','"RfcUid"','com.gridnode.gtas.server.backend.model.Rfc','com.gridnode.gtas.server.backend.model.Port','port.rfc',0,0,1,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=rfc.uid'||chr(13)||chr(10)||'foreign.display=rfc.name'||chr(13)||chr(10)||'foreign.cached=true');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_hostDir','HOST_DIR','"HostDir"','java.lang.String','com.gridnode.gtas.server.backend.model.Port','port.hostDir',80,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_isDiffFileName','IS_DIFF_FILE_NAME','"IsDiffFileName"','java.lang.Boolean','com.gridnode.gtas.server.backend.model.Port','port.isDiffFileName',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'port.isDiffFileName.true=true'||chr(13)||chr(10)||'port.isDiffFileName.false=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_isOverwrite','IS_OVERWRITE','"IsOverwrite"','java.lang.Boolean','com.gridnode.gtas.server.backend.model.Port','port.isOverwrite',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'port.isOverwrite.true=true'||chr(13)||chr(10)||'port.isOverwrite.false=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_fileName','FILE_NAME','"FileName"','java.lang.String','com.gridnode.gtas.server.backend.model.Port','port.fileName',80,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_isAddFileExt','IS_ADD_FILE_EXT','"IsAddFileExt"','java.lang.Boolean','com.gridnode.gtas.server.backend.model.Port','port.isAddFileExt',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'port.isAddFileExt.true=true'||chr(13)||chr(10)||'port.isAddFileExt.false=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_fileExtType','FILE_EXT_TYPE','"FileExtType"','java.lang.Integer','com.gridnode.gtas.server.backend.model.Port','port.fileExtType',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'port.fileExtType.datetime=1'||chr(13)||chr(10)||'port.fileExtType.gdoc=2'||chr(13)||chr(10)||'port.fileExtType.sequence=3');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_fileExtValue','FILE_EXT_VALUE','"FileExtValue"','java.lang.String','com.gridnode.gtas.server.backend.model.Port','port.fileExtValue',64,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'port.fileExtValue.format1=ddMMyyHHmmss'||chr(13)||chr(10)||'port.fileExtValue.format2=ddMMyyHHmmssSSS'||chr(13)||chr(10)||'port.fileExtValue.format3=ddMMyyyyHHmmss'||chr(13)||chr(10)||'port.fileExtValue.format4=ddMMyyyyHHmmssSSS'||chr(13)||chr(10)||'port.fileExtValue.format5=MMddyyHHmmss'||chr(13)||chr(10)||'port.fileExtValue.format6=MMddyyHHmmssSSS'||chr(13)||chr(10)||'port.fileExtValue.format7=MMddyyyyHHmmss'||chr(13)||chr(10)||'port.fileExtValue.format8=MMddyyyyHHmmssSSS'||chr(13)||chr(10)||'port.fileExtValue.format9=yyMMddHHmmss'||chr(13)||chr(10)||'port.fileExtValue.format10=yyMMddHHmmssSSS'||chr(13)||chr(10)||'port.fileExtValue.format11=yyyyMMddHHmmss'||chr(13)||chr(10)||'port.fileExtValue.format12=yyyyMMddHHmmssSSS'||chr(13)||chr(10)||'port.fileExtValue.format13=yyyyMMdd'||chr(13)||chr(10)||'port.fileExtValue.format14=yyMMdd'||chr(13)||chr(10)||'port.fileExtValue.format15=ddMMyyyy'||chr(13)||chr(10)||'port.fileExtValue.format16=ddMMyy'||chr(13)||chr(10)||'port.fileExtValue.format17=MMddyyyy'||chr(13)||chr(10)||'port.fileExtValue.format18=MMddyy');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_isExportGdoc','IS_EXPORT_GDOC','"IsExportGdoc"','java.lang.Boolean','com.gridnode.gtas.server.backend.model.Port','port.isExportGdoc',0,0,1,1,1,'',999,'||chr(13)||chr(10)displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'port.isExportGdoc.true=true'||chr(13)||chr(10)||'port.isExportGdoc.false=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_startNum','START_NUM','"StartNum"','java.lang.Integer','com.gridnode.gtas.server.backend.model.Port','port.startNum',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_rolloverNum','ROLLOVER_NUM','"RolloverNum"','java.lang.Integer','com.gridnode.gtas.server.backend.model.Port','port.rolloverNum',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_nextNum','NEXT_NUM','"NextNum"','java.lang.Integer','com.gridnode.gtas.server.backend.model.Port','port.nextNum',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_isPadded','IS_PADDED','"IsPadded"','java.lang.Boolean','com.gridnode.gtas.server.backend.model.Port','port.isPadded',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'port.isPadded.true=true'||chr(13)||chr(10)||'port.isPadded.false=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_fixNumLength','FIX_NUM_LENGTH','"FixNumLength"','java.lang.Integer','com.gridnode.gtas.server.backend.model.Port','port.fixNumLength',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_fileGrouping','FILE_GROUPING','"FileGrouping"','java.lang.Integer','com.gridnode.gtas.server.backend.model.Port','port.fileGrouping',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'port.fileGrouping.flat=1'||chr(13)||chr(10)||'port.fileGrouping.gdocAttachment=2'||chr(13)||chr(10)||'port.fileGrouping.all=3');

---------- rfc
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName"='com.gridnode.gtas.server.backend.model.Rfc';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.gtas.server.backend.model.Rfc','rfc.uid',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=uid');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.gtas.server.backend.model.Rfc','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.gtas.server.backend.model.Rfc','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'candelete.enabled=true'||chr(13)||chr(10)||'candelete.disabled=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_name','NAME','"RfcName"','java.lang.String','com.gridnode.gtas.server.backend.model.Rfc','rfc.name',18,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true','type=text'||chr(13)||chr(10)||'text.length.max=18');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.gtas.server.backend.model.Rfc','rfc.description',80,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_connectionType','CONNECTION_TYPE','"ConnectionType"','java.lang.String','com.gridnode.gtas.server.backend.model.Rfc','rfc.connectionType',2,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=text'||chr(13)||chr(10)||'text.length.max=2');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_host','HOST','"Host"','java.lang.String','com.gridnode.gtas.server.backend.model.Rfc','rfc.host',80,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_portNumber','PORT_NUMBER','"PortNumber"','java.lang.Integer','com.gridnode.gtas.server.backend.model.Rfc','rfc.portNumber',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_useCommandFile','USE_COMMAND_FILE','"UseCommandFile"','java.lang.Boolean','com.gridnode.gtas.server.backend.model.Rfc','rfc.useCommandFile',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'rfc.useCommandFile.true=true'||chr(13)||chr(10)||'rfc.useCommandFile.false=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_commandFileDir','COMMAND_FILE_DIR','"CommandFileDir"','java.lang.String','com.gridnode.gtas.server.backend.model.Rfc','rfc.commandFileDir',120,0,1,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=120');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_commandFile','COMMAND_FILE','"CommandFile"','java.lang.String','com.gridnode.gtas.server.backend.model.Rfc','rfc.commandFile',80,0,1,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_commandLine','COMMAND_LINE','"CommandLine"','java.lang.String','com.gridnode.gtas.server.backend.model.Rfc','rfc.commandLine',120,0,1,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=120');
