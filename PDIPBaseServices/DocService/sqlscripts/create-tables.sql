USE userdb;

# 
# Table structure for table 'domain' 
# 

DROP TABLE IF EXISTS `domain`; 
CREATE TABLE IF NOT EXISTS domain (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  DomainName varchar(20) NOT NULL DEFAULT '' ,
  PropertyName varchar(20) NOT NULL DEFAULT '' ,
  ChildCount bigint(20) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (UID),
  UNIQUE KEY DomainName (DomainName),
    KEY PropertyName (PropertyName)
);
 
DROP TABLE IF EXISTS `folder`; 
CREATE TABLE IF NOT EXISTS folder (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  FolderName varchar(20) NOT NULL DEFAULT '' ,
  DomainId bigint(20) NOT NULL ,
  ParentId bigint(20) NOT NULL DEFAULT '0' ,
  ChildCount bigint(20) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (UID)
);
 
DROP TABLE IF EXISTS `document`; 
CREATE TABLE IF NOT EXISTS document (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  DocumentName varchar(20) NOT NULL DEFAULT '' ,
  DocType varchar(20) NOT NULL ,
  ParentId bigint(20) NOT NULL DEFAULT '0' ,
  DomainId bigint(20) NOT NULL DEFAULT '0' ,
  FileCount bigint(20) NOT NULL DEFAULT '0' ,
  Author varchar(20) NOT NULL DEFAULT '' ,
  Size bigint(20) NOT NULL DEFAULT '0' ,
  CreatedOn datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  LastAccessed datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  PRIMARY KEY (UID)
);
 
DROP TABLE IF EXISTS `file`; 
CREATE TABLE IF NOT EXISTS file (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  FileName varchar(20) NOT NULL DEFAULT '' ,
  IsMainFile tinyint(1) NOT NULL DEFAULT '0' ,
  DocumentId bigint(20) NOT NULL DEFAULT '0' ,
  ParentId bigint(20) NOT NULL DEFAULT '0' ,
  DomainId bigint(20) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (UID)
);