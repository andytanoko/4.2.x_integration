USE appdb;


DELETE FROM entitymetainfo WHERE EntityName = 'CompanyProfile';
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.coyprofile.model.CompanyProfile","CompanyProfile","coy_profile");


# CompanyProfile
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%CompanyProfile';
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.uid","0","0","0","1","1","","999","displayable=false\r\nmandatory=false\r\neditable=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_coyName","COY_NAME","CoyName","java.lang.String","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.coyName","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_altEmail","ALT_EMAIL","AltEmail","java.lang.String","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.altEmail","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_email","EMAIL","Email","java.lang.String","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.email","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_tel","TEL","Tel","java.lang.String","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.tel","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\ntext.length.max=16\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_altTel","ALT_TEL","AltTel","java.lang.String","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.altTel","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\ntext.length.max=16\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_fax","FAX","Fax","java.lang.String","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.fax","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\ntext.length.max=16\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_address","ADDRESS","Address","java.lang.String","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.address","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_city","CITY","City","java.lang.String","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.city","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\ntext.length.max=50\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_state","STATE","State","java.lang.String","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.state","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\n","## may consider choose item from some State list\r\ntype=text\r\ntext.length.max=6\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_zipCode","ZIP_CODE","ZipCode","java.lang.String","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.zipCode","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\ntext.length.max=10\r\n\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_country","COUNTRY","Country","java.lang.String","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.country","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=foreign\r\nforeign.key=countryCode.alpha3Code\r\nforeign.display=countryCode.name\r\nforeign.cached=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_language","LANGUAGE","Language","java.lang.String","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.language","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=foreign\r\nforeign.key=languageCode.alpha2Code\r\nforeign.display=languageCode.name\r\nforeign.cached=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_isPartner","IS_PARTNER","IsPartner","java.lang.Boolean","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.isPartner","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.coyprofile.model.CompanyProfile","coyProfile.canDelete","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES (NULL, '_version', 'VERSION', 'Version', 'java.lang.Double', 'com.gridnode.pdip.app.coyprofile.model.CompanyProfile', 'coyProfile.version', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=range\r\n');

