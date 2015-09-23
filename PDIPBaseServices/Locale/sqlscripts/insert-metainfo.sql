USE appdb;

DELETE from entitymetainfo WHERE EntityName IN ('CountryCode','LanguageCode');
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.locale.model.CountryCode', 'CountryCode', 'country_code');
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.locale.model.LanguageCode', 'LanguageCode', 'language_code');

DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%CountryCode';
INSERT INTO fieldmetainfo VALUES
(NULL, '_name', 'NAME', 'Name', 'java.lang.String', 'com.gridnode.pdip.base.locale.model.CountryCode', 'countryCode.name', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=text\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_numericalCode', 'NUMERICAL_CODE', 'NumericalCode', 'java.lang.Integer', 'com.gridnode.pdip.base.locale.model.CountryCode', 'countryCode.numericalCode', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=uid');
INSERT INTO fieldmetainfo VALUES
(NULL, '_alpha2Code', 'ALPHA_2_CODE', 'Alpha2Code', 'java.lang.String', 'com.gridnode.pdip.base.locale.model.CountryCode', 'countryCode.alpha2Code', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=text');
INSERT INTO fieldmetainfo VALUES
(NULL, '_alpha3Code', 'ALPHA_3_CODE', 'Alpha3Code', 'java.lang.String', 'com.gridnode.pdip.base.locale.model.CountryCode', 'countryCode.alpha3Code', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=text');

DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%LanguageCode';
INSERT INTO fieldmetainfo VALUES
(NULL, '_name', 'NAME', 'Name', 'java.lang.String', 'com.gridnode.pdip.base.locale.model.LanguageCode', 'languageCode.name', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=text\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_bAlpha3Code', 'B_ALPHA_3_CODE', 'BAlpha3Code', 'java.lang.String', 'com.gridnode.pdip.base.locale.model.LanguageCode', 'languageCode.balpha3Code', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=text');
INSERT INTO fieldmetainfo VALUES
(NULL, '_alpha2Code', 'ALPHA_2_CODE', 'Alpha2Code', 'java.lang.String', 'com.gridnode.pdip.base.locale.model.LanguageCode', 'languageCode.alpha2Code', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=uid');
INSERT INTO fieldmetainfo VALUES
(NULL, '_tAlpha3Code', 'T_ALPHA_3_CODE', 'TAlpha3Code', 'java.lang.String', 'com.gridnode.pdip.base.locale.model.LanguageCode', 'languageCode.talpha3Code', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=text');

