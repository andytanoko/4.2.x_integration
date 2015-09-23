# 11 Apr 2003 I8 v2.0.27 [Koh Han Sing] Add in new entity GridTalkLicense.
#

USE appdb;

DELETE FROM entitymetainfo WHERE EntityName IN ('GridTalkLicense');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.registration.model.GridTalkLicense', 'GridTalkLicense', "gridtalk_license");

# RegistrationInfo
UPDATE fieldmetainfo SET Constraints= 'type=enum\r\nregistrationInfo.registrationState.notRegistered=0\r\nregistrationInfo.registrationState.registered=1\r\nregistrationInfo.registrationState.regInProgress=2\r\nregistrationInfo.registrationState.expired=3' WHERE EntityObjectName='com.gridnode.gtas.server.registration.model.RegistrationInfo' AND FieldName='REGISTRATION_STATE';
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


USE userdb;

#
# Table structure for table 'gridtalk_license'
#

DROP TABLE IF EXISTS gridtalk_license;
CREATE TABLE IF NOT EXISTS gridtalk_license (
  UID bigint(20) NOT NULL DEFAULT '0',
  LicenseUid bigint(20) NOT NULL,
  OsName text NOT NULL DEFAULT '',
  OsVersion text NOT NULL DEFAULT '',
  MachineName text NOT NULL DEFAULT '',
  StartDate text NOT NULL DEFAULT '',
  EndDate text NOT NULL DEFAULT '',
  CanDelete tinyint(1) NOT NULL DEFAULT '1',
  Version double NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE KEY ID (LicenseUid)
);

