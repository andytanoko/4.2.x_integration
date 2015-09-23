USE appdb;


#
# Table structure for table 'country_code'
#

DROP TABLE IF EXISTS country_code;
CREATE TABLE IF NOT EXISTS country_code (
  Name varchar(255) NOT NULL DEFAULT '' ,
  NumericalCode int(6) NOT NULL DEFAULT '0' ,
  Alpha2Code char(2) NOT NULL DEFAULT '' ,
  Alpha3Code char(3) NOT NULL DEFAULT '' ,
  PRIMARY KEY (NumericalCode),
  UNIQUE KEY Country_alpha3 (Alpha3Code),
  UNIQUE KEY Country_alpha2 (Alpha2Code),
   KEY Country_name_idx (Name)
);


#
# Table structure for table 'language_code'
#

DROP TABLE IF EXISTS language_code;
CREATE TABLE IF NOT EXISTS language_code (
  Name varchar(255) NOT NULL DEFAULT '0' ,
  Alpha2Code char(2) NOT NULL DEFAULT '0' ,
  BAlpha3Code char(3) NOT NULL DEFAULT '0' ,
  TAlpha3Code char(3) NOT NULL DEFAULT '' ,
  PRIMARY KEY (Alpha2Code),
  UNIQUE KEY Language_talpha3 (TAlpha3Code),
  UNIQUE KEY Language_balpha3 (BAlpha3Code),
   KEY Language_name_idx (Name)
);

