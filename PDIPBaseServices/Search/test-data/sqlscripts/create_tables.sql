# MySQL-Front Dump 1.22
#
# Host: localhost Database: userdb
#--------------------------------------------------------
# Server version 3.23.51

USE userdb;


#
# Table structure for table 'address'
#

DROP TABLE /*!32200 IF EXISTS*/ address;
CREATE TABLE /*!32300 IF NOT EXISTS*/ address (
  UID int(5) unsigned NOT NULL DEFAULT '0' ,
  FirstName varchar(50) ,
  LastName varchar(50) ,
  Address varchar(100) ,
  EmailId varchar(50) ,
  PRIMARY KEY (UID)
);

