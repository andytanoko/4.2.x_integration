# Change Log
# 25 Mar 2004 GT 2.1.20 [Neo Sok Lay] Index data_contextdata table on ContextUId for better performance

USE userdb;

#
# Table structure for table 'data_stringdata'
#

DROP TABLE IF EXISTS data_stringdata;
CREATE TABLE IF NOT EXISTS data_stringdata (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	Data varchar(100) ,
	DataType varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'data_bytedata'
#

DROP TABLE IF EXISTS data_bytedata;
CREATE TABLE IF NOT EXISTS data_bytedata (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	Data blob ,
	DataType varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;

#
# Table structure for table 'data_contextdata'
#

DROP TABLE IF EXISTS data_contextdata;
CREATE TABLE IF NOT EXISTS data_contextdata (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	ContextUId bigint(20) NOT NULL DEFAULT '0',
	ContextData longblob ,
	PRIMARY KEY (UID),
	UNIQUE data_context_idx (ContextUId)
) TYPE = MyISAM;

