SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- pf_trigger
DELETE FROM "pf_trigger" WHERE "UID" IN (-1,-2,-3,-4);
INSERT INTO "pf_trigger" VALUES(-1,0,'PFIP','','','','','',0,0,0,1,1,0,0,-99999999);
INSERT INTO "pf_trigger" VALUES(-2,0,'PFIB','','','','','',1,0,0,1,1,0,0,-99999999);
INSERT INTO "pf_trigger" VALUES(-3,0,'PFOB','','','','','',2,0,0,1,1,0,0,-99999999);
INSERT INTO "pf_trigger" VALUES(-4,0,'PFEP','','','','','',3,0,0,1,1,0,0,-99999999);
INSERT INTO "pf_trigger" VALUES(-5,1,'PFIP','0A1FailureNotification','RN_FAILNOTF2','','','',0,1,1,1,1,0,0,-99999999);
INSERT INTO "pf_trigger" VALUES(-6,1,'PFIB','0A1FailureNotification','RN_FAILNOTF2','','','',1,1,1,1,1,0,0,-99999999);
INSERT INTO "pf_trigger" VALUES(-7,1,'PFOB','0A1FailureNotification','RN_FAILNOTF2','','','',2,1,1,1,1,0,0,-99999999);
INSERT INTO "pf_trigger" VALUES(-8,1,'PFIP','0A1FailureNotification1.1','RN_FAILNOTF1','','','',0,1,1,1,1,0,0,-99999999);
INSERT INTO "pf_trigger" VALUES(-9,1,'PFIB','0A1FailureNotification1.1','RN_FAILNOTF1','','','',1,1,1,1,1,0,0,-99999999);
INSERT INTO "pf_trigger" VALUES(-10,1,'PFOB','0A1FailureNotification1.1','RN_FAILNOTF1','','','',2,1,1,1,1,0,0,-99999999);

