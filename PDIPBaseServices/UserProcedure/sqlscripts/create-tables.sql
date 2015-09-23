# 09 Jul 2003 I1 v2.2 [Koh Han Sing] Add in field GridDocField in UserProcedure.
#

USE userdb;
#
# Table structure for table 'procedure_definition_file'
#

DROP TABLE IF EXISTS procedure_definition_file;
CREATE TABLE `procedure_definition_file` (
  `UID` bigint(20) NOT NULL default '0',
  `Name` varchar(30) default NULL,
  `Description` varchar(30) default NULL,
  `Type` int(11) default NULL,
  `FileName` varchar(30) default NULL,
  `FilePath` varchar(30) default NULL,
  `CanDelete` tinyint(1) NOT NULL DEFAULT '1',
  `Version` double NOT NULL DEFAULT '1',
   PRIMARY KEY (UID),
   UNIQUE name_index (Name)
) TYPE=MyISAM COMMENT='Data Structure for Procedure Definition File';


#
# Table structure for table 'user_procedure'
#

DROP TABLE IF EXISTS user_procedure;
CREATE TABLE `user_procedure` (
  `UID` bigint(20) NOT NULL default '0',
  `Name` varchar(30) default NULL,
  `Description` varchar(40) default NULL,
  `IsSynchronous` tinyint(1) default NULL,
  `ProcType` int(11) default NULL,
  `ProcDefFile` bigint(20) NOT NULL default '0',
  `ProcDef` blob,
  `ProcParamList` blob,
  `ReturnDataType` int(11) default NULL,
  `DefAction` int(11) default NULL,
  `DefAlert` bigint(20) default NULL,
  `ProcReturnList` blob,
  `CanDelete` tinyint(1) NOT NULL DEFAULT '1',
  `Version` double NOT NULL DEFAULT '1',
  `GridDocField` int(3) default NULL,
   PRIMARY KEY (UID),
   UNIQUE name_index (Name)
) TYPE=MyISAM;

