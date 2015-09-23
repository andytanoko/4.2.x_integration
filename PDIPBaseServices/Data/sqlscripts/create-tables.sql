
#
# Table structure for table 'data_businessdocument'
#

DROP TABLE IF EXISTS data_businessdocument;
CREATE TABLE IF NOT EXISTS data_businessdocument (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	Name varchar(50) ,
	ConditionExpr varchar(50) ,
	SpecElement varchar(50) ,
	SpecLocation varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'data_dataitem'
#

DROP TABLE IF EXISTS data_dataitem;
CREATE TABLE IF NOT EXISTS data_dataitem (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	DataContextType varchar(50) ,
	ContextUId bigint(20) ,
	DataDefKey varchar(50) ,
	IsCopy tinyint(1) NOT NULL DEFAULT '0' ,
        DataDefName varchar(50) ,
        DataDefType varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'data_dataitemspecmap'
#

DROP TABLE IF EXISTS data_dataitemspecmap;
CREATE TABLE IF NOT EXISTS data_dataitemspecmap (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	DataItemUId bigint(20) ,
        DataFieldName varchar(50) ,
	DataLocation varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'data_numericdata'
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
# Table structure for table 'data_controldocument'
#

DROP TABLE IF EXISTS data_controldocument;
CREATE TABLE IF NOT EXISTS data_controldocument (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	Name varchar(50) ,
	ValueLocation varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;

