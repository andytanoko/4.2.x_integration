# 11 Jan 2006      [Tam Wei Xiang]      Remove the field flag 'unsigned' if the field type is int or smallint

USE appdb;
ALTER TABLE country_code MODIFY NumericalCode int(6) NOT NULL DEFAULT '0';