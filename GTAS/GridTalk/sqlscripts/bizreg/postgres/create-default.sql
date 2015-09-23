SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- registry_connect_info
INSERT INTO "registry_connect_info"("UID","Name","QueryUrl","PublishUrl","CanDelete") VALUES (-1,'IBM UDDI Business Registry','http://uddi.ibm.com/ubr/inquiryapi','https://uddi.ibm.com/ubr/publishapi',0);
INSERT INTO "registry_connect_info"("UID","Name","QueryUrl","PublishUrl","CanDelete") VALUES (-2,'IBM UDDI Business Test Registry','http://uddi.ibm.com/testregistry/inquiryapi','https://uddi.ibm.com/testregistry/publishapi',0);
INSERT INTO "registry_connect_info"("UID","Name","QueryUrl","PublishUrl","CanDelete") VALUES (-3,'Microsoft UDDI Business Registry','http://uddi.microsoft.com/inquire','https://uddi.microsoft.com/publish',0);
INSERT INTO "registry_connect_info"("UID","Name","QueryUrl","PublishUrl","CanDelete") VALUES (-4,'Microsoft UDDI Business Registry (Test Site)','http://test.uddi.microsoft.com/inquire','https://test.uddi.microsoft.com/publish',0);
INSERT INTO "registry_connect_info"("UID","Name","QueryUrl","PublishUrl","CanDelete") VALUES (-5,'GridNode UDDI Business Registry','http://uddi.gridnode.com/inquiry','https://uddi.gridnode.com/publish',0);


