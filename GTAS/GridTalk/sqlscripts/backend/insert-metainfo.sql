# MySQL-Front Dump 2.1
#
# Host: 127.0.0.1   Database: appdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

# Modified by Tam Wei Xiang 24 Aug 2005
# The field 'attachmentDir' in table port is no longer needed.
# GDOC will be stored in the folder same as UDOC

use appdb;

#
# Dumping data for table 'entitymetainfo'
#
# Field 1 ObjectName: full qualified class name of the entity
# Field 2 EntityName: short class name of the entity
# Field 3 SqlName: table name of the entity

# entitymetainfo for Port, Rfc
DELETE FROM entitymetainfo
WHERE ObjectName
IN ('com.gridnode.gtas.server.backend.model.Port',
    'com.gridnode.gtas.server.backend.model.Rfc');

INSERT INTO entitymetainfo VALUES ("com.gridnode.gtas.server.backend.model.Port","Port","port");
INSERT INTO entitymetainfo VALUES ("com.gridnode.gtas.server.backend.model.Rfc","Rfc","rfc");

#
# Dumping data for table 'fieldmetainfo'
#
# UID bigint(20) NOT NULL auto_increment,
# ObjectName: field name in Entity class ,
# FieldName: field ID in Entity Interface class ,
# SqlName: Field column name in Table,
# ValueClass: field data type,
# EntityObjectName: full qualified class name of the entity
# Label: field display label
# Length: valid field length ,
# Proxy: '1' if proxy, '0' otherwise,,
# Mandatory: '1' if mandatory, '0' otherwise,
# Editable: '1' if editable, '0' otherwise
# Displayable: '1' if displayable, '0' otherwise
# OqlName: ,
# Sequence: default '999' ,
# Presentation: properties for presentation of the field
# Constraints: constraint imposed on the values of the field
#

# fieldmetainfo for trigger
DELETE FROM fieldmetainfo WHERE EntityObjectName='com.gridnode.gtas.server.backend.model.Port';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.backend.model.Port","port.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.backend.model.Port","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.backend.model.Port","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_name","NAME","PortName","java.lang.String",
"com.gridnode.gtas.server.backend.model.Port","port.name",
"15","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=15"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.gtas.server.backend.model.Port","port.description",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isRfc","IS_RFC","IsRfc","java.lang.Boolean",
"com.gridnode.gtas.server.backend.model.Port","port.isRfc",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nport.isRfc.true=true\r\nport.isRfc.false=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_rfc","RFC","RfcUid","com.gridnode.gtas.server.backend.model.Rfc",
"com.gridnode.gtas.server.backend.model.Port","port.rfc",
"0","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=rfc.uid\r\nforeign.display=rfc.name\r\nforeign.cached=true"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_hostDir","HOST_DIR","HostDir","java.lang.String",
"com.gridnode.gtas.server.backend.model.Port","port.hostDir",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isDiffFileName","IS_DIFF_FILE_NAME","IsDiffFileName","java.lang.Boolean",
"com.gridnode.gtas.server.backend.model.Port","port.isDiffFileName",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nport.isDiffFileName.true=true\r\nport.isDiffFileName.false=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isOverwrite","IS_OVERWRITE","IsOverwrite","java.lang.Boolean",
"com.gridnode.gtas.server.backend.model.Port","port.isOverwrite",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nport.isOverwrite.true=true\r\nport.isOverwrite.false=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_fileName","FILE_NAME","FileName","java.lang.String",
"com.gridnode.gtas.server.backend.model.Port","port.fileName",
"80","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isAddFileExt","IS_ADD_FILE_EXT","IsAddFileExt","java.lang.Boolean",
"com.gridnode.gtas.server.backend.model.Port","port.isAddFileExt",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nport.isAddFileExt.true=true\r\nport.isAddFileExt.false=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_fileExtType","FILE_EXT_TYPE","FileExtType","java.lang.Integer",
"com.gridnode.gtas.server.backend.model.Port","port.fileExtType",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nport.fileExtType.datetime=1\r\nport.fileExtType.gdoc=2\r\nport.fileExtType.sequence=3"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_fileExtValue","FILE_EXT_VALUE","FileExtValue","java.lang.String",
"com.gridnode.gtas.server.backend.model.Port","port.fileExtValue",
"64","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nport.fileExtValue.format1=ddMMyyHHmmss\r\nport.fileExtValue.format2=ddMMyyHHmmssSSS\r\nport.fileExtValue.format3=ddMMyyyyHHmmss\r\nport.fileExtValue.format4=ddMMyyyyHHmmssSSS\r\nport.fileExtValue.format5=MMddyyHHmmss\r\nport.fileExtValue.format6=MMddyyHHmmssSSS\r\nport.fileExtValue.format7=MMddyyyyHHmmss\r\nport.fileExtValue.format8=MMddyyyyHHmmssSSS\r\nport.fileExtValue.format9=yyMMddHHmmss\r\nport.fileExtValue.format10=yyMMddHHmmssSSS\r\nport.fileExtValue.format11=yyyyMMddHHmmss\r\nport.fileExtValue.format12=yyyyMMddHHmmssSSS\r\nport.fileExtValue.format13=yyyyMMdd\r\nport.fileExtValue.format14=yyMMdd\r\nport.fileExtValue.format15=ddMMyyyy\r\nport.fileExtValue.format16=ddMMyy\r\nport.fileExtValue.format17=MMddyyyy\r\nport.fileExtValue.format18=MMddyy"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isExportGdoc","IS_EXPORT_GDOC","IsExportGdoc","java.lang.Boolean",
"com.gridnode.gtas.server.backend.model.Port","port.isExportGdoc",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nport.isExportGdoc.true=true\r\nport.isExportGdoc.false=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_startNum","START_NUM","StartNum","java.lang.Integer",
"com.gridnode.gtas.server.backend.model.Port","port.startNum",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_rolloverNum","ROLLOVER_NUM","RolloverNum","java.lang.Integer",
"com.gridnode.gtas.server.backend.model.Port","port.rolloverNum",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_nextNum","NEXT_NUM","NextNum","java.lang.Integer",
"com.gridnode.gtas.server.backend.model.Port","port.nextNum",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isPadded","IS_PADDED","IsPadded","java.lang.Boolean",
"com.gridnode.gtas.server.backend.model.Port","port.isPadded",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=enum\r\nport.isPadded.true=true\r\nport.isPadded.false=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_fixNumLength","FIX_NUM_LENGTH","FixNumLength","java.lang.Integer",
"com.gridnode.gtas.server.backend.model.Port","port.fixNumLength",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_fileGrouping","FILE_GROUPING","FileGrouping","java.lang.Integer",
"com.gridnode.gtas.server.backend.model.Port","port.fileGrouping",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nport.fileGrouping.flat=1\r\nport.fileGrouping.gdocAttachment=2\r\nport.fileGrouping.all=3"
);


# fieldmetainfo for rfc
DELETE FROM fieldmetainfo WHERE EntityObjectName='com.gridnode.gtas.server.backend.model.Rfc';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.backend.model.Rfc","rfc.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.backend.model.Rfc","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.backend.model.Rfc","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_name","NAME","RfcName","java.lang.String",
"com.gridnode.gtas.server.backend.model.Rfc","rfc.name",
"18","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=18"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.gtas.server.backend.model.Rfc","rfc.description",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_connectionType","CONNECTION_TYPE","ConnectionType","java.lang.String",
"com.gridnode.gtas.server.backend.model.Rfc","rfc.connectionType",
"2","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=2"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_host","HOST","Host","java.lang.String",
"com.gridnode.gtas.server.backend.model.Rfc","rfc.host",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_portNumber","PORT_NUMBER","PortNumber","java.lang.Integer",
"com.gridnode.gtas.server.backend.model.Rfc","rfc.portNumber",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_useCommandFile","USE_COMMAND_FILE","UseCommandFile","java.lang.Boolean",
"com.gridnode.gtas.server.backend.model.Rfc","rfc.useCommandFile",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nrfc.useCommandFile.true=true\r\nrfc.useCommandFile.false=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_commandFileDir","COMMAND_FILE_DIR","CommandFileDir","java.lang.String",
"com.gridnode.gtas.server.backend.model.Rfc","rfc.commandFileDir",
"120","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=120"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_commandFile","COMMAND_FILE","CommandFile","java.lang.String",
"com.gridnode.gtas.server.backend.model.Rfc","rfc.commandFile",
"80","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_commandLine","COMMAND_LINE","CommandLine","java.lang.String",
"com.gridnode.gtas.server.backend.model.Rfc","rfc.commandLine",
"120","0","1","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=120"
);


###By Wei Xiang: Backup only, can remove it safely ###
#INSERT INTO fieldmetainfo VALUES(
#NULL,"_attachmentDir","ATTACHMENT_DIR","AttachmentDir","java.lang.String",
#"com.gridnode.gtas.server.backend.model.Port","port.attachmentDir",
#"80","0","0","1","1","","999",
#"displayable=true\r\nmandatory=false\r\neditable=true",
#"type=text\r\ntext.length.max=80"
#);