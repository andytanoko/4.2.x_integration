# Change Log
# 05 Dec 2003 GT 2.2 [Neo Sok Lay] Limit GridNode Name input length to 64 char, upper limit for OrganizationName by RDNAttributes

USE appdb;

DELETE FROM entitymetainfo WHERE EntityName IN ('RegistrationInfo');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.registration.model.RegistrationInfo', 'RegistrationInfo', NULL);
DELETE FROM entitymetainfo WHERE EntityName IN ('GridTalkLicense');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.registration.model.GridTalkLicense', 'GridTalkLicense', "gridtalk_license");

# RegistrationInfo
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%RegistrationInfo';
INSERT INTO fieldmetainfo VALUES
(NULL, '_nodeID', 'GRIDNODE_ID', NULL, 'java.lang.Integer', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.gridnodeId', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=true\r\n', 'type=range\r\nrange.min=1\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_nodeName', 'GRIDNODE_NAME', NULL, 'java.lang.String', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.gridnodeName', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=true\r\n', 'type=text\r\ntext.length.max=64\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_prodKeyF1', 'PRODUCT_KEY_F1', NULL, 'java.lang.String', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.prodKeyF1', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=true\r\n', 'type=text\r\ntext.length.min=5\r\ntext.length.max=5\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_prodKeyF2', 'PRODUCT_KEY_F2', NULL, 'java.lang.String', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.prodKeyF2', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=true\r\n', 'type=text\r\ntext.length.min=6\r\ntext.length.max=6\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_prodKeyF3', 'PRODUCT_KEY_F3', NULL, 'java.lang.String', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.prodKeyF3', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=true\r\n', 'type=text\r\ntext.length.min=5\r\ntext.length.max=5\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_prodKeyF4', 'PRODUCT_KEY_F4', NULL, 'java.lang.String', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.prodKeyF4', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=true\r\n', 'type=text\r\ntext.length.min=6\r\ntext.length.max=6\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_category', 'CATEGORY', NULL, 'java.lang.String', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.category', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=foreign\r\nforeign.key=gnCategory.code\r\nforeign.display=gnCategory.name\r\nforeign.cached=false\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_profile', 'COMPANY_PROFILE', NULL, 'com.gridnode.pdip.app.coyprofile.model.CompanyProfile', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.companyProfile', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=embedded\r\nembedded.type=coyProfile\r\n\r\n\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_licStartDate', 'LIC_START_DATE', NULL, 'java.util.Date', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.licStartDate', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=false\r\n\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_licEndDate', 'LIC_END_DATE', NULL, 'java.util.Date', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.licEndDate', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=false\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_bizConnections', 'BIZ_CONNECTIONS', NULL, 'java.lang.Integer', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.bizConnections', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=range\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_registrationState', 'REGISTRATION_STATE', NULL, 'java.lang.Short', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.registrationState', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=enum\r\nregistrationInfo.registrationState.notRegistered=0\r\nregistrationInfo.registrationState.registered=1\r\nregistrationInfo.registrationState.regInProgress=2\r\nregistrationInfo.registrationState.expired=3\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_licFile', 'LICENSE_FILE', NULL, 'java.lang.String', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.licFile', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\neditable.update=true\r\nmandatory=true\r\n', 'type=file\r\nfile.downloadable=false\r\nfile.pathKey=registration.path.license\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_osName', 'OS_NAME', NULL, 'java.lang.String', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.osName', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=true\r\n', 'type=text');
INSERT INTO fieldmetainfo VALUES
(NULL, '_osVersion', 'OS_VERSION', NULL, 'java.lang.String', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.osVersion', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=true\r\n', 'type=text');
INSERT INTO fieldmetainfo VALUES
(NULL, '_machineName', 'MACHINE_NAME', NULL, 'java.lang.String', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.machineName', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=true\r\n', 'type=text');
INSERT INTO fieldmetainfo VALUES
(NULL, '_licenseState', 'LICENSE_STATE', NULL, 'java.lang.Short', 'com.gridnode.gtas.server.registration.model.RegistrationInfo', 'registrationInfo.licenseState', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=true\r\n', 'type=enum\r\nregistrationInfo.licenseState.valid=0\r\nregistrationInfo.licenseState.notCommenced=1\r\nregistrationInfo.licenseState.expired=2\r\nregistrationInfo.licenseState.revoked=3\r\n');

# GridTalkLicense
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%GridTalkLicense';
INSERT INTO fieldmetainfo VALUES
(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.gtas.server.registration.model.GridTalkLicense","gridTalkLicense.uid","0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=uid");
INSERT INTO fieldmetainfo VALUES
(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.gtas.server.registration.model.GridTalkLicense","","0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=range");
INSERT INTO fieldmetainfo VALUES
(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.gtas.server.registration.model.GridTalkLicense","","0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false");
INSERT INTO fieldmetainfo VALUES
(NULL,"_licenseUid","LICENSE_UID","LicenseUid","java.lang.Long","com.gridnode.gtas.server.registration.model.GridTalkLicense","gridTalkLicense.licenseUid","0","0","1","0","0","","999","displayable=false\r\nmandatory=true\r\neditable=false","type=foreign\r\nforeign.key=license.uid");
INSERT INTO fieldmetainfo VALUES
(NULL,"_osName","OS_NAME","OsName","java.lang.String","com.gridnode.gtas.server.registration.model.GridTalkLicense","gridTalkLicense.osName","0","0","1","0","0","","999","displayable=false\r\nmandatory=true\r\neditable=false","type=text");
INSERT INTO fieldmetainfo VALUES
(NULL,"_osVersion","OS_VERSION","OsVersion","java.lang.String","com.gridnode.gtas.server.registration.model.GridTalkLicense","gridTalkLicense.osVersion","0","0","1","0","0","","999","displayable=false\r\nmandatory=true\r\neditable=false","type=text");
INSERT INTO fieldmetainfo VALUES
(NULL,"_machineName","MACHINE_NAME","MachineName","java.lang.String","com.gridnode.gtas.server.registration.model.GridTalkLicense","gridTalkLicense.machineName","0","0","1","0","0","","999","displayable=false\r\nmandatory=true\r\neditable=false","type=text");
INSERT INTO fieldmetainfo VALUES
(NULL,"_startDate","START_DATE","StartDate","java.lang.String","com.gridnode.gtas.server.registration.model.GridTalkLicense","gridTalkLicense.startDate","0","0","1","0","0","","999","displayable=false\r\nmandatory=true\r\neditable=false","type=text");
INSERT INTO fieldmetainfo VALUES
(NULL,"_endDate","END_DATE","EndDate","java.lang.String","com.gridnode.gtas.server.registration.model.GridTalkLicense","gridTalkLicense.endDate","0","0","1","0","0","","999","displayable=false\r\nmandatory=true\r\neditable=false","type=text");

