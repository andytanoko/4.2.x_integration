SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('Role','SubjectRole','Feature','AccessRight');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.acl.model.Role','Role','"role"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.acl.model.SubjectRole','SubjectRole','"subject_role"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.acl.model.Feature','Feature','"feature"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.acl.model.AccessRight','AccessRight','"access_right"');

--------- Role
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%Role';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.acl.model.Role','role.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_role','ROLE','"Role"','java.lang.String','com.gridnode.pdip.base.acl.model.Role','role.role',30,0,1,1,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.min=2'||chr(13)||chr(10)||'text.length.max=30'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.acl.model.Role','role.description',255,0,1,1,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.min=1'||chr(13)||chr(10)||'text.length.max=255'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.base.acl.model.Role','role.canDelete',0,0,1,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.acl.model.Role','role.version',0,0,1,1,1,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));

--------- Subject Role, NOTE: must be after Role.
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%SubjectRole';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.acl.model.SubjectRole','',0,0,1,1,1,'0',999,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_subjectType','SUBJECT_TYPE','"SubjectType"','java.lang.String','com.gridnode.pdip.base.acl.model.SubjectRole','',30,0,1,1,1,'0',1,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_role','ROLE','"Role"','java.lang.Long','com.gridnode.pdip.base.acl.model.SubjectRole','',0,0,1,1,1,'0',999,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_subject','SUBJECT','"Subject"','java.lang.Long','com.gridnode.pdip.base.acl.model.SubjectRole','',0,0,1,1,1,'0',999,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.acl.model.SubjectRole','',0,0,0,1,0,'0',999,'','');

--------- ACCESS RIGHT
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%AccessRight';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_roleUID','ROLE','"RoleUID"','java.lang.Long','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.role',20,0,1,1,1,'0',2,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=role.uid'||chr(13)||chr(10)||'foreign.display=role.description'||chr(13)||chr(10)||'foreign.cached=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_feature','FEATURE','"Feature"','java.lang.String','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.feature',0,0,1,1,1,'0',3,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=feature.feature'||chr(13)||chr(10)||'foreign.display=feature.description'||chr(13)||chr(10)||'foreign.cached=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_descr','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.description',80,0,1,1,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80'||chr(13)||chr(10)||'text.length.min=1'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_action','ACTION','"Action"','java.lang.String','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.actionName',30,0,1,1,1,'0',4,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataType','DATA_TYPE','"DataType"','java.lang.String','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.dataType',30,0,0,1,1,'0',5,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_criteria','CRITERIA','"Criteria"','com.gridnode.pdip.framework.db.filter.IDataFilter','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.criteria',0,0,0,1,1,'0',6,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=other'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.canDelete',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.version',0,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));

--------- Feature
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%Feature';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.acl.model.Feature','feature.version',0,0,1,1,1,'0',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.acl.model.Feature','feature.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_feature','FEATURE','"Feature"','java.lang.String','com.gridnode.pdip.base.acl.model.Feature','feature.feature',30,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.acl.model.Feature','feature.description',80,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_actions','ACTIONS','"Actions"','java.util.List','com.gridnode.pdip.base.acl.model.Feature','feature.actions',1024,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'collection=true'||chr(13)||chr(10)||'collection.element=java.lang.String'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataTypes','DATA_TYPES','"DataTypes"','java.util.List','com.gridnode.pdip.base.acl.model.Feature','feature.dataTypes',1024,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'collection=true'||chr(13)||chr(10)||'collection.element=java.lang.String'||chr(13)||chr(10));
