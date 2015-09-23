SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE from "entitymetainfo" WHERE "EntityName" IN ('CountryCode','LanguageCode');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.locale.model.CountryCode', 'CountryCode', '"country_code"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.locale.model.LanguageCode', 'LanguageCode', '"language_code"');

--------- CountryCode
DELETE from "fieldmetainfo" WHERE "EntityObjectName" LIKE '%CountryCode';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.locale.model.CountryCode','countryCode.name',0,0,0,1,1,NULL,999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_numericalCode','NUMERICAL_CODE','"NumericalCode"','java.lang.Integer','com.gridnode.pdip.base.locale.model.CountryCode','countryCode.numericalCode',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_alpha2Code','ALPHA_2_CODE','"Alpha2Code"','java.lang.String','com.gridnode.pdip.base.locale.model.CountryCode','countryCode.alpha2Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_alpha3Code','ALPHA_3_CODE','"Alpha3Code"','java.lang.String','com.gridnode.pdip.base.locale.model.CountryCode','countryCode.alpha3Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');

--------- LanguageCode
DELETE from "fieldmetainfo" WHERE "EntityObjectName" LIKE '%LanguageCode';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.locale.model.LanguageCode','languageCode.name',0,0,0,1,1,NULL,999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_bAlpha3Code','B_ALPHA_3_CODE','"BAlpha3Code"','java.lang.String','com.gridnode.pdip.base.locale.model.LanguageCode','languageCode.balpha3Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_alpha2Code','ALPHA_2_CODE','"Alpha2Code"','java.lang.String','com.gridnode.pdip.base.locale.model.LanguageCode','languageCode.alpha2Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_tAlpha3Code','T_ALPHA_3_CODE','"TAlpha3Code"','java.lang.String','com.gridnode.pdip.base.locale.model.LanguageCode','languageCode.talpha3Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
