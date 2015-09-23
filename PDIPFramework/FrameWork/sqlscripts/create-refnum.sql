#
# Table structure for table 'referencenum'
#
use appdb;

DROP TABLE IF EXISTS referencenum;
CREATE TABLE IF NOT EXISTS referencenum (
  RefName varchar(50) NOT NULL DEFAULT '' ,
  LastRefNum bigint(20) NOT NULL DEFAULT '0' ,
  MaxRefNum bigint(20) NOT NULL DEFAULT '-1' ,
  PRIMARY KEY (RefName)
);


