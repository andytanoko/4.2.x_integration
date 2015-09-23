SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- #### rnif ####
DROP TABLE IF EXISTS "rn_profile";
CREATE TABLE "rn_profile" ( 
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "DocumentUid" BIGINT,
  "ProcessInstanceId" VARCHAR(255) ,
  "ProcessOriginatorId" VARCHAR(80) ,
  "ProcessResponderId" VARCHAR(80) ,
  "ProcessDefName" VARCHAR(80) ,
  "ReceiverDomain" VARCHAR(80) ,
  "ReceiverGlobalBusIdentifier" VARCHAR(80) ,
  "ReceiverLocationId" VARCHAR(80) ,
  "SenderDomain" VARCHAR(80) ,
  "SenderGlobalBusIdentifier" VARCHAR(80) ,
  "SenderLocationId" VARCHAR(80) ,
  "DeliveryMessageTrackingId" VARCHAR(80) ,
  "BusActivityIdentifier" VARCHAR(80) ,
  "FromGlobalPartnerRoleClassCode" VARCHAR(80) ,
  "FromGlobalBusServiceCode" VARCHAR(80) ,
  "InReplyToGlobalBusActionCode" VARCHAR(80) ,
  "InReplyToMessageStandard" VARCHAR(80) ,
  "InReplyToStandardVersion" VARCHAR(80) ,
  "InReplyToVersionIdentifier" VARCHAR(80) ,
  "ServiceMessageTrackingId" VARCHAR(80) ,
  "ActionIdentityGlobalBusActionCode" VARCHAR(80) , -- Original:ActionIdentityGlobalBusActionCode
  "ActionIdentityToMessageStandard" VARCHAR(80) , -- Original:ActionIdentityToMessageStandard
  "ActionIdentityStandardVersion" VARCHAR(80) , -- Original:ActionIdentityStandardVersion
  "ActionIdentityVersionIdentifier" VARCHAR(80) , -- Original:ActionIdentityVersionIdentifier
  "SignalIdentityGlobalBusSignalCode" VARCHAR(80) , -- Original:SignalIdentityGlobalBusSignalCode
  "SignalIdentityVersionIdentifier" VARCHAR(80) , -- Original:SignalIdentityVersionIdentifier
  "ToGlobalPartnerRoleClassCode" VARCHAR(80) ,
  "ToGlobalBusServiceCode" VARCHAR(80) ,
  "GlobalUsageCode" VARCHAR(80) ,
  "PartnerGlobalBusIdentifier" VARCHAR(80) ,
  "PIPGlobalProcessCode" VARCHAR(80) ,
  "PIPInstanceIdentifier" VARCHAR(200) ,
  "PIPVersionIdentifier" VARCHAR(80) ,
  "ProcessTransactionId" VARCHAR(200) ,
  "ProcessActionId" VARCHAR(240) ,
  "FromGlobalPartnerClassCode" VARCHAR(80) ,
  "ToGlobalPartnerClassCode" VARCHAR(80) ,
  "NumberOfAttas" DECIMAL(10) ,
  "IsSignalDoc" DECIMAL(1) ,
  "IsRequestMsg" DECIMAL(1) ,	
  "UniqueValue" VARCHAR(80) ,	
  "AttemptCount" DECIMAL(10) ,		
  "MsgDigest" VARCHAR(80) ,	
  "RNIFVersion" VARCHAR(80) ,
  "InResponseToActionID" VARCHAR(80) ,
  "UserTrackingID" VARCHAR(255) ,
  PRIMARY KEY ("UID")
);

DROP index IF EXISTS "RNPROFILE_IDX2";
DROP index IF EXISTS "RNPROFILE_IDX3";
DROP index IF EXISTS "RNPROFILE_IDX4";

CREATE INDEX "RNPROFILE_IDX2" ON "rn_profile" ("ProcessDefName", "ProcessInstanceId", "ProcessOriginatorId", "IsRequestMsg");
CREATE INDEX "RNPROFILE_IDX3" ON "rn_profile" ("ProcessDefName", "UniqueValue", "ProcessOriginatorId", "IsRequestMsg");
CREATE INDEX "RNPROFILE_IDX4" ON "rn_profile" ("PIPInstanceIdentifier", "IsRequestMsg");


ALTER TABLE "rn_profile" OWNER TO userdb;

	