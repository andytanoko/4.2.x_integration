SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;


DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('GFDefinition','GFTemplate');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.app.gridform.model.GFDefinition','GFDefinition','"gridform_definition"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.app.gridform.model.GFTemplate','GFTemplate','"gridform_template"');

--------- GFDefinition
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%GFDefinition';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.app.gridform.model.GFDefinition','definition.uid',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false','type=uid');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.app.gridform.model.GFDefinition','definition.name',50,0,1,1,1,'',1,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.min=1'||chr(13)||chr(10)||'text.length.max=50');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_filename','FILENAME','"Filename"','java.lang.String','com.gridnode.pdip.app.gridform.model.GFDefinition','definition.filename',80,0,1,1,1,'',2,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=file'||chr(13)||chr(10)||'file.downloadable=true'||chr(13)||chr(10)||'file.pathKey=gridform.path.ldf');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_template','TEMPLATE','"TemplateUID"','com.gridnode.pdip.app.gridform.model.GFTemplate','com.gridnode.pdip.app.gridform.model.GFDefinition','definition.template',0,0,1,1,1,'',3,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true','type=foreign'||chr(13)||chr(10)||'foreign.key=template.uid'||chr(13)||chr(10)||'foreign.display=template.name'||chr(13)||chr(10)||'foreign.cached=true');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.app.gridform.model.GFDefinition','definition.version',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.app.gridform.model.GFDefinition','definition.canDelete',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false','type=enum'||chr(13)||chr(10)||'candelete.enabled=true'||chr(13)||chr(10)||'candelete.disabled=false');

--------- GFTemplate
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%GFTemplate';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.app.gridform.model.GFTemplate','template.uid',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false','type=uid');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.app.gridform.model.GFTemplate','template.name',50,0,0,0,1,'',1,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.min=1'||chr(13)||chr(10)||'text.length.max=50');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_filename','FILENAME','"Filename"','java.lang.String','com.gridnode.pdip.app.gridform.model.GFTemplate','template.filename',80,0,1,1,1,'',2,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10),'type=file'||chr(13)||chr(10)||'file.downloadable=true'||chr(13)||chr(10)||'file.pathKey=gridform.path.ltf');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.app.gridform.model.GFTemplate','template.version',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.app.gridform.model.GFTemplate','template.canDelete',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false','type=enum'||chr(13)||chr(10)||'candelete.enabled=true'||chr(13)||chr(10)||'candelete.disabled=false');
