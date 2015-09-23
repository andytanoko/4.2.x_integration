SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

ALTER TABLE "security_profile" ALTER COLUMN "DigestAlgorithm" TYPE VARCHAR(30);

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;
UPDATE "fieldmetainfo" SET "Constraints"='type=enum'||chr(13)||chr(10)||'securityInfo.digestAlgorithm.md5=1.2.840.113549.2.5'||chr(13)||chr(10)||'securityInfo.digestAlgorithm.sha1=1.3.14.3.2.26' WHERE "EntityObjectName" LIKE '%.SecurityInfo' AND "ObjectName"='_digestAlgorithm';
UPDATE "fieldmetainfo" SET "Constraints"='type=enum'||chr(13)||chr(10)||'securityInfo.encryptionAlgorithm.TDES_ALG=1.2.840.113549.3.7'||chr(13)||chr(10)||'securityInfo.encryptionAlgorithm.RC2_ALG=1.2.840.113549.3.2' WHERE "EntityObjectName" LIKE '%.SecurityInfo' AND "ObjectName"='_encryptionAlgorithm';
