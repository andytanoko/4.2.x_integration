USE userdb;

#
# Table structure for table 'rolemapentity'
#

DROP TABLE IF EXISTS rolemapentity;
CREATE TABLE IF NOT EXISTS rolemapentity (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	RoleKey varchar(60) ,
        PartnerKey varchar(60) ,
        ProcessDefKey varchar(120) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;
