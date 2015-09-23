USE appdb;


#
# TABLE STRUCTURE FOR TABLE 'entitymetainfo'
#

DROP TABLE IF EXISTS entitymetainfo;
CREATE TABLE IF NOT EXISTS entitymetainfo (
  ObjectName VARCHAR(80) NOT NULL DEFAULT '' ,
  EntityName VARCHAR(80) NOT NULL DEFAULT '' ,
  SqlName VARCHAR(40) ,
  PRIMARY KEY (ObjectName)
);



#
# TABLE STRUCTURE FOR TABLE 'fieldmetainfo'
#

DROP TABLE IF EXISTS fieldmetainfo;
CREATE TABLE IF NOT EXISTS fieldmetainfo (
  UID bigint(20) NOT NULL auto_increment,
  ObjectName VARCHAR(80) NOT NULL DEFAULT '' ,
  FieldName VARCHAR(40) NOT NULL DEFAULT '' ,
  SqlName VARCHAR(40) ,
  ValueClass VARCHAR(80) NOT NULL DEFAULT '' ,
  EntityObjectName VARCHAR(255) NOT NULL DEFAULT '' ,
  LABEL VARCHAR(255) ,
  LENGTH int(11) DEFAULT '0' ,
  Proxy TINYINT(1) DEFAULT '0' ,
  Mandatory TINYINT(1) DEFAULT '0' ,
  Editable TINYINT(1) DEFAULT '1' ,
  Displayable TINYINT(1) DEFAULT '1' ,
  OqlName VARCHAR(40) ,
  Sequence int(3) DEFAULT '999' ,
  Presentation text ,
  Constraints text ,
  PRIMARY KEY (UID),
  UNIQUE KEY EntityObjectName (EntityObjectName,FieldName)
);

