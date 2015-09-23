#
# Table structure for table 'user_account'
#
use userdb;

DROP TABLE IF EXISTS user_account;
CREATE TABLE IF NOT EXISTS user_account (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(50) NOT NULL DEFAULT '' ,
  ID varchar(15) NOT NULL DEFAULT '' ,
  Password varchar(12) NOT NULL DEFAULT '' ,
  Phone varchar(16) ,
  Email varchar(255) ,
  Property varchar(255) ,
  Version double NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE KEY ID (ID),
  KEY user_name_idx (Name)
);



#
# Table structure for table 'user_account_state'
#

DROP TABLE IF EXISTS user_account_state;
CREATE TABLE IF NOT EXISTS user_account_state (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  UserID varchar(15) NOT NULL DEFAULT '' ,
  NumLoginTries smallint(6) NOT NULL DEFAULT '0' ,
  IsFreeze tinyint(1) NOT NULL DEFAULT '0' ,
  FreezeTime datetime ,
  LastLoginTime datetime ,
  LastLogoutTime datetime ,
  State smallint(1) NOT NULL DEFAULT '1' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  CreateTime datetime ,
  CreateBy varchar(15) ,
  PRIMARY KEY (UID),
  UNIQUE KEY unique_user_id (UserID)
);

