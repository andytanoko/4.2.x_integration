SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE FROM "entitymetainfo" WHERE "EntityName" = 'License';
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.app.license.model.License','License','"license"');

--------- License
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%License';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.app.license.model.License','license.uid',0,0,0,1,1,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_prodKey','PRODUCT_KEY','"ProductKey"','java.lang.String','com.gridnode.pdip.app.license.model.License','license.productKey',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=30'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_prodName','PRODUCT_NAME','"ProductName"','java.lang.String','com.gridnode.pdip.app.license.model.License','license.productName',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_prodVersion','PRODUCT_VERSION','"ProductVersion"','java.lang.String','com.gridnode.pdip.app.license.model.License','license.productVersion',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=20'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_startDate','START_DATE','"StartDate"','java.util.Date','com.gridnode.pdip.app.license.model.License','license.startDate',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=datetime'||chr(13)||chr(10)||'datetime.date=true'||chr(13)||chr(10)||'datetime.time=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_endDate','END_DATE','"EndDate"','java.util.Date','com.gridnode.pdip.app.license.model.License','license.endDate',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=datetime'||chr(13)||chr(10)||'datetime.date=true'||chr(13)||chr(10)||'datetime.time=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_state','STATE','"State"','java.lang.Short','com.gridnode.pdip.app.license.model.License','license.state',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'license.state.valid=0'||chr(13)||chr(10)||'license.state.notCommenced=1'||chr(13)||chr(10)||'license.state.expired=2'||chr(13)||chr(10)||'license.state.revoked=3'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT, '_version', 'VERSION', '"Version"', 'java.lang.Double', 'com.gridnode.pdip.app.license.model.License', 'license.version', 0, 0, 0, 1, 1, NULL, 999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10), 'type=range'||chr(13)||chr(10));
