use userdb;

#
# Table structure for table 'gridform_definition'
#

DROP TABLE IF EXISTS gridform_definition;
CREATE TABLE IF NOT EXISTS gridform_definition (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(50) NOT NULL DEFAULT '' ,
  Filename varchar(80) NOT NULL DEFAULT '' ,
  TemplateUID bigint(20) NOT NULL DEFAULT '0' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY unique_definition_name (Name),
  KEY definition_name_idx (Name)
);



#
# Table structure for table 'gridform_template'
#

DROP TABLE IF EXISTS gridform_template;
CREATE TABLE IF NOT EXISTS gridform_template (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(50) NOT NULL DEFAULT '' ,
  Filename varchar(80) NOT NULL DEFAULT '' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY unique_template_name (Name),
  KEY template_name_idx (Name)
);

