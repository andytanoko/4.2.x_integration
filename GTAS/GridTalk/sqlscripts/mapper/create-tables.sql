# 05 Dec 2006 GT 4.0  [Neo Sok Lay] Increase SourceDocType, TargetDocType field length to 30 chars.


USE userdb;

#
# Table structure for table 'gridtalk_mapping_file'
#

DROP TABLE IF EXISTS gridtalk_mapping_rule;
CREATE TABLE IF NOT EXISTS gridtalk_mapping_rule (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(30) NOT NULL DEFAULT '' ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  SourceDocType varchar(30) NOT NULL DEFAULT '' ,
  TargetDocType varchar(30) NOT NULL DEFAULT '' ,
  SourceDocFileType varchar(12) NOT NULL DEFAULT '' ,
  TargetDocFileType varchar(12) NOT NULL DEFAULT '' ,
  HeaderTransformation tinyint(1) NOT NULL DEFAULT '0' ,
  TransformWithHeader tinyint(1) NOT NULL DEFAULT '0' ,
  TransformWithSource tinyint(1) NOT NULL DEFAULT '0' ,
  MappingRuleUID bigint(20) NOT NULL DEFAULT '0' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY NAME_IDX (Name)
);
