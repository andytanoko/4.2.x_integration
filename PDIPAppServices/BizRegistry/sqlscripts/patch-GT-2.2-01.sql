# 18 Aug 2003 GT 2.2 I1 [Neo Sok Lay] Add RegistryObjectMapping entity.
# 03 Sep 2003 GT 2.2 I1 [Neo Sok Lay] Add metainfo for SearchRegistryCriteria, SearchRegistryQuery
# 24 Sep 2003 GT 2.2 I1 [Neo Sok Lay] Add RegistryConnectInfo entity.

USE userdb;

DROP TABLE IF EXISTS registry_object_map;
CREATE TABLE IF NOT EXISTS registry_object_map (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  RegistryObjectKey varchar(50) NOT NULL,
  RegistryObjectType varchar(30)NOT NULL,
  RegistryQueryUrl varchar(255) NOT NULL,
  RegistryPublishUrl varchar(255) NOT NULL,
  ProprietaryObjectKey varchar(50) NOT NULL,
  ProprietaryObjectType varchar(30) NOT NULL,
  IsPublishedObject tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  State smallint(2) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (UID),
  UNIQUE KEY registry_obj_idx (RegistryObjectKey,RegistryObjectType,RegistryQueryUrl),
  UNIQUE KEY proprietary_obj_idx (ProprietaryObjectKey,ProprietaryObjectType,RegistryQueryUrl)
);

DROP TABLE IF EXISTS registry_connect_info;
CREATE TABLE IF NOT EXISTS registry_connect_info (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(50) NOT NULL,
  QueryUrl varchar(255) NOT NULL,
  PublishUrl varchar(255),
  PublishUser varchar(20),
  PublishPassword varchar(20),
  CanDelete tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY registry_conn_idx (Name)
);


USE appdb;

# RegistryObjectMapping
DELETE from entitymetainfo WHERE EntityName = 'RegistryObjectMapping';
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping","RegistryObjectMapping","registry_object_map");

DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%.RegistryObjectMapping';
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping","registryObjectMapping.uid","0","0","0","1","1","","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_registryObjectKey","REGISTRY_OBJECT_KEY","RegistryObjectKey","java.lang.String","com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping","registryObjectMapping.registryObjectKey","0","0","0","1","1","","1","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=text\r\ntext.length.max=50\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_registryObjectType","REGISTRY_OBJECT_TYPE","RegistryObjectType","java.lang.String","com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping","registryObjectMapping.registryObjectType","0","0","0","1","1","","2","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\nregistryObjectMapping.registryObjectType.organization=Organization\r\nregistryObjectMapping.registryObjectType.service=Service\r\nregistryObjectMapping.registryObjectType.binding=Binding\r\nregistryObjectMapping.registryObjectType.scheme=Scheme\r\nregistryObjectMapping.registryObjectType.concept=Concept\r\nregistryObjectMapping.registryObjectType.specification=Specification\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_registryQueryUrl","REGISTRY_QUERY_URL","RegistryQueryUrl","java.lang.String","com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping","registryObjectMapping.registryQueryUrl","0","0","0","1","1","","3","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_registryPublishUrl","REGISTRY_PUBLISH_URL","RegistryPublishUrl","java.lang.String","com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping","registryObjectMapping.registryPublishUrl","0","0","0","1","1","","4","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_proprietaryObjectType","PROPRIETARY_OBJECT_TYPE","ProprietaryObjectType","java.lang.String","com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping","registryObjectMapping.proprietaryObjectType","0","0","0","1","1","","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=text\r\ntext.length.max=30\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_proprietaryObjectKey","PROPRIETARY_OBJECT_KEY","ProprietaryObjectKey","java.lang.String","com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping","registryObjectMapping.proprietaryObjectKey","0","0","0","1","1","","999","displayable=false\r\nmandatory=false\r\neditable=false\r\n","type=text\r\ntext.length.max=50\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_isPublishedObject","IS_PUBLISHED_OBJECT","IsPublishedObject","java.lang.Boolean","com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping","registryObjectMapping.isPublishedObject","0","0","0","1","1","","999","displayable=false\r\nmandatory=false\r\neditable=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_state","STATE","State","java.lang.Short","com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping","registryObjectMapping.state","0","0","0","1","1","","5","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\nregistryObjectMapping.state.sync=0\r\nregistryObjectMapping.state.deleted=1\r\nregistryObjectMapping.state.pendDelete=2\r\nregistryObjectMapping.state.pendUpdate=3\r\n");

# SearchRegistryCriteria
DELETE from entitymetainfo WHERE EntityName = 'SearchRegistryCriteria';
INSERT INTO entitymetainfo VALUES('com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria', 'SearchRegistryCriteria', '');

DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%.SearchRegistryCriteria';
INSERT INTO fieldmetainfo VALUES(NULL,"_match","MATCH","","java.lang.Short","com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria","searchRegistryCriteria.match","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\neditable.display=false\r\n","type=enum\r\searchRegistryCriteria.match.all=1\r\nsearchRegistryCriteria.match.any=2\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_busEntityDesc","BUS_ENTITY_DESC","","java.lang.String","com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria","searchRegistryCriteria.busEntityDesc","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_messagingStandards","MESSAGING_STANDARDS","","java.util.Collection","com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria","searchRegistryCriteria.messagingStandards","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\neditable.display=false\r\n","collection=true\r\ntype=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_queryUrl","QUERY_URL","","java.lang.String","com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria","searchRegistryCriteria.queryUrl","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\neditable.display=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_duns","DUNS","","java.lang.String","com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria","searchRegistryCriteria.duns","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=text\r\n");

# SearchRegistryQuery
DELETE from entitymetainfo WHERE EntityName = 'SearchRegistryQuery';
INSERT INTO entitymetainfo VALUES('com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery', 'SearchRegistryQuery', '');

DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%.SearchRegistryQuery';
INSERT INTO fieldmetainfo VALUES(NULL, '_searchID', 'SEARCH_ID', '', 'java.lang.Long', 'com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery', 'searchRegistryQuery.searchId', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=uid\r\n');
INSERT INTO fieldmetainfo VALUES(NULL, '_dtSubmitted', 'DT_SUBMITTED', '', 'java.sql.Timestamp', 'com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery', 'searchRegistryQuery.dtSubmitted', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES(NULL, '_dtResponded', 'DT_RESPONDED', '', 'java.sql.Timestamp', 'com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery', 'searchRegistryQuery.dtResponded', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES(NULL, '_criteria', 'CRITERIA', '', 'com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria', 'com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery', 'searchRegistryQuery.criteria', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=embedded\r\nembedded.type=searchRegistryCriteria');
INSERT INTO fieldmetainfo VALUES(NULL, '_results', 'RESULTS', '', 'java.util.Collection', 'com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery', 'searchRegistryQuery.results', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'collection=true\r\ntype=embedded\r\nembedded.type=searchedBusinessEntity\r\n');
INSERT INTO fieldmetainfo VALUES(NULL, '_rawResults', 'RAW_RESULTS', '', 'java.util.Collection', 'com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery', '', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', '');
INSERT INTO fieldmetainfo VALUES(NULL, '_isException', 'IS_EXCEPTION', '', 'java.lang.Boolean', 'com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery', 'searchRegistryQuery.isException', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=enum\r\ngeneric.yes=true\r\ngeneric.no=false');
INSERT INTO fieldmetainfo VALUES(NULL, '_exceptionStr', 'EXCEPTION_STR', '', 'java.lang.String', 'com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery', 'searchRegistryQuery.exceptionStr', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=text\r\n');

# RegistryConnectInfo
DELETE from entitymetainfo WHERE EntityName = 'RegistryConnectInfo';
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo","RegistryConnectInfo","registry_connect_info");

DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%.RegistryConnectInfo';
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo","registryConnectInfo.uid","0","0","0","1","1","","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo","registryConnectInfo.name","0","0","0","1","1","","1","displayable=true\r\neditable=false\r\neditable.create=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=50\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_queryUrl","QUERY_URL","QueryUrl","java.lang.String","com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo","registryConnectInfo.queryUrl","0","0","0","1","1","","3","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_publishUrl","PUBLISH_URL","PublishUrl","java.lang.String","com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo","registryConnectInfo.publishUrl","0","0","0","1","1","","4","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_publishPassword","PUBLISH_PASSWORD","PublishPassword","java.lang.String","com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo","registryConnectInfo.publishPassword","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=20\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_publishUser","PUBLISH_USER","PublishUser","java.lang.String","com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo","registryConnectInfo.publishUser","0","0","0","1","1","","999","displayable=true\r\nmandatory=true\r\neditable=true\r\n","type=text\r\ntext.length.max=20\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo","registryConnectInfo.canDelete","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.pdip.app.model.RegistryConnectInfo","registryConnectInfo.version","0","0","0","1","1","","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=range\r\n");
