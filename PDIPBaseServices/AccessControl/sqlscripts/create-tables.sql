USE userdb;

# 
# Table structure for table 'role' 
# 

DROP TABLE IF EXISTS `role`; 
CREATE TABLE IF NOT EXISTS role (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Role varchar(30) NOT NULL DEFAULT '' ,
  Description varchar(255) ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '0' ,
  PRIMARY KEY (UID),
  UNIQUE KEY Role (Role)
);
 
 
 
# 
# Table structure for table 'subject_role' 
# 

DROP TABLE IF EXISTS `subject_role`; 
CREATE TABLE IF NOT EXISTS subject_role (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Subject bigint(20) NOT NULL DEFAULT '0' ,
  Role bigint(20) NOT NULL DEFAULT '0' ,
  SubjectType varchar(30) NOT NULL DEFAULT '' ,
  Version double NOT NULL DEFAULT '0' ,
  PRIMARY KEY (UID),
  UNIQUE KEY role_subject_subjectType_idx (Role,Subject,SubjectType),
   KEY subject_subjectType_idx (Subject,SubjectType),
   KEY role_subjectType_idx (Role,SubjectType)

);



#
# Table structure for table 'access_right'
#

DROP TABLE IF EXISTS access_right;
CREATE TABLE IF NOT EXISTS access_right (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  RoleUID bigint(20) NOT NULL DEFAULT '0' ,
  Feature varchar(50) NOT NULL DEFAULT '' ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  Action varchar(30) NOT NULL DEFAULT '' ,
  DataType varchar(30) ,
  Criteria text ,
  CanDelete tinyint(1) unsigned NOT NULL DEFAULT '0' ,
  Version double NOT NULL DEFAULT '0' ,
  PRIMARY KEY (UID),
  UNIQUE KEY UID (UID),
   KEY Role_rights (RoleUID),
   KEY Role_feature_rights (RoleUID,Feature),
  UNIQUE KEY action_data_access_rights (RoleUID,Feature,Action,DataType)
);



#
# Table structure for table 'feature'
#

DROP TABLE IF EXISTS feature;
CREATE TABLE IF NOT EXISTS feature (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Feature varchar(30) NOT NULL DEFAULT '' ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  Actions text NOT NULL DEFAULT '' ,
  DataTypes text NOT NULL DEFAULT '' ,
  Version double NOT NULL DEFAULT '0' ,
  PRIMARY KEY (UID),
  UNIQUE KEY Feature (Feature)
);

