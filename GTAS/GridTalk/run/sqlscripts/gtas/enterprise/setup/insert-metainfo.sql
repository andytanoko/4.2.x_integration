SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('ResourceLink','SharedResource','SearchedBusinessEntity','SearchedChannelInfo');
INSERT INTO "entitymetainfo" ("ObjectName","EntityName","SqlName") VALUES ('com.gridnode.gtas.server.enterprise.model.ResourceLink','ResourceLink','"resource_link"');
INSERT INTO "entitymetainfo" ("ObjectName","EntityName","SqlName") VALUES ('com.gridnode.gtas.server.enterprise.model.SharedResource','SharedResource','"shared_resource"');
INSERT INTO "entitymetainfo" ("ObjectName","EntityName","SqlName") VALUES ('com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity','SearchedBusinessEntity','');
INSERT INTO "entitymetainfo" ("ObjectName","EntityName","SqlName") VALUES ('com.gridnode.gtas.model.enterprise.ISearchedChannelInfo','SearchedChannelInfo','');

---------- ResourceLink
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ResourceLink';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.gtas.server.enterprise.model.ResourceLink',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_fromType','FROM_TYPE','"FromResourceType"','java.lang.String','com.gridnode.gtas.server.enterprise.model.ResourceLink',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_fromUID','FROM_UID','"FromResourceUID"','java.lang.Long','com.gridnode.gtas.server.enterprise.model.ResourceLink',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_toType','TO_TYPE','"ToResourceType"','java.lang.String','com.gridnode.gtas.server.enterprise.model.ResourceLink',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_toUID','TO_UID','"ToResourceUID"','java.lang.Long','com.gridnode.gtas.server.enterprise.model.ResourceLink',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_priority','PRIORITY','"Priority"','java.lang.Integer','com.gridnode.gtas.server.enterprise.model.ResourceLink',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.gtas.server.enterprise.model.ResourceLink',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.gtas.server.enterprise.model.ResourceLink',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_nextLinks','NEXT_LINKS',NULL,'java.util.Collection','com.gridnode.gtas.server.enterprise.model.ResourceLink',NULL,0,0,0,1,1,NULL,999,NULL,NULL);

---------- SharedResource
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%SharedResource';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.gtas.server.enterprise.model.SharedResource',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_resourceType','RESOURCE_TYPE','"ResourceType"','java.lang.String','com.gridnode.gtas.server.enterprise.model.SharedResource',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_resourceUID','RESOURCE_UID','"ResourceUID"','java.lang.Long','com.gridnode.gtas.server.enterprise.model.SharedResource',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_toEnterpriseID','TO_ENTERPRISE_ID','"ToEnterpriseID"','java.lang.String','com.gridnode.gtas.server.enterprise.model.SharedResource',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_state','STATE','"State"','java.lang.Short','com.gridnode.gtas.server.enterprise.model.SharedResource',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_syncChecksum','SYNC_CHECKSUM','"SyncChecksum"','java.lang.Long','com.gridnode.gtas.server.enterprise.model.SharedResource',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.gtas.server.enterprise.model.SharedResource',NULL,0,0,0,1,1,NULL,999,NULL,NULL);
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.gtas.server.enterprise.model.SharedResource',NULL,0,0,0,1,1,NULL,999,NULL,NULL);

---------- SearchedBusinessEntity
DELETE from "fieldmetainfo" WHERE "EntityObjectName" LIKE '%.SearchedBusinessEntity';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uuid','UUID','','java.lang.String','com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity','searchedBusinessEntity.uuid',0,0,0,1,1,'',1,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_enterpriseId','ENTERPRISE_ID','"EnterpriseID"','java.lang.String','com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity','searchedBusinessEntity.enterpriseId',0,0,0,1,1,'',1,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_busEntId','ID','"ID"','java.lang.String','com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity','searchedBusinessEntity.id',0,0,0,1,1,'',2,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_desc','DESCRIPTION','"Description"','java.lang.String','com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity','searchedBusinessEntity.description',0,0,0,1,1,'',3,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_state','STATE','"State"','java.lang.Integer','com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity','searchedBusinessEntity.beState',0,0,0,1,1,'',5,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'searchedBusinessEntity.state.newBe=0'||chr(13)||chr(10)||'searchedBusinessEntity.state.myBe=1'||chr(13)||chr(10)||'searchedBusinessEntity.state.partnerBe=2'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_whitePage','WHITE_PAGE','','com.gridnode.pdip.app.bizreg.model.WhitePage','com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity','searchedBusinessEntity.whitePage',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=embedded'||chr(13)||chr(10)||'embedded.type=whitePage'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_channels','CHANNELS','','java.util.Collection','com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity','searchedBusinessEntity.channels',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'collection=true'||chr(13)||chr(10)||'type=embedded'||chr(13)||chr(10)||'embedded.type=searchedChannelInfo'||chr(13)||chr(10));

---------- SearchedChannelInfo
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%.ISearchedChannelInfo';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_refId','REF_ID','','java.lang.String','com.gridnode.gtas.model.enterprise.ISearchedChannelInfo','searchedChannelInfo.refId',30,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'displayable=true','type=text'||chr(13)||chr(10)||'text.length.max=30');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_tptProtocolType','TPT_PROTOCOL_TYPE','','java.lang.String','com.gridnode.gtas.model.enterprise.ISearchedChannelInfo','searchedChannelInfo.tptProtocolType',10,0,1,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true','type=enum'||chr(13)||chr(10)||'channelInfo.tptProtocolType.jms=JMS'||chr(13)||chr(10)||'channelInfo.tptProtocolType.http=HTTP'||chr(13)||chr(10)||'channelInfo.tptProtocolType.soap=SOAP-HTTP'||chr(13)||chr(10)||'channelInfo.tptProtocolType.upc=UPC');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_description','DESCRIPTION','','java.lang.String','com.gridnode.gtas.model.enterprise.ISearchedChannelInfo','searchedChannelInfo.description',80,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true','type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_tptCommInfo','TPT_COMM_INFO','','com.gridnode.pdip.app.channel.model.CommInfo','com.gridnode.gtas.model.enterprise.ISearchedChannelInfo','searchedChannelInfo.tptCommInfo',0,0,1,1,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=true','type=embedded'||chr(13)||chr(10)||'embedded.type=commInfo');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packagingInfo','PACKAGING_PROFILE','','com.gridnode.pdip.app.channel.model.PackagingInfo','com.gridnode.gtas.model.enterprise.ISearchedChannelInfo','searchedChannelInfo.packagingProfile',0,0,1,1,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=true','type=embedded'||chr(13)||chr(10)||'embedded.type=packagingInfo');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_securityInfo','SECURITY_PROFILE','','com.gridnode.pdip.app.channel.model.SecurityInfo','com.gridnode.gtas.model.enterprise.ISearchedChannelInfo','searchedChannelInfo.securityProfile',0,0,1,1,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=true','type=embedded'||chr(13)||chr(10)||'embedded.type=securityInfo');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_name','NAME','','java.lang.String','com.gridnode.gtas.model.enterprise.ISearchedChannelInfo','searchedChannelInfo.name',30,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=true','type=text'||chr(13)||chr(10)||'text.length.max=30');
