# 05 Dec 2003 GT 2.1       [Neo Sok Lay] Increase column size for ProcessInstanceId,PIPInstanceIdentifier,ProcessTransactionId,ProcessActionId in rn_profile table
# 29 FEB 2008 GT 4.0.2.3   [Tam Wei Xiang] Expand ProcessInstanceId of rn_profile to 255 chars
#

use userdb;

	CREATE TABLE `rn_profile` ( 
		`UID` bigint NOT NULL default '0',
		DocumentUid bigint default NULL,
		ProcessInstanceId varchar(255) default NULL,
		ProcessOriginatorId varchar(80) default NULL,
		ProcessResponderId varchar(80) default NULL,
		ProcessDefName varchar(80) default NULL,
		ReceiverDomain varchar(80) default NULL,
		ReceiverGlobalBusIdentifier varchar(80) default NULL,
		ReceiverLocationId varchar(80) default NULL,
		SenderDomain varchar(80) default NULL,
		SenderGlobalBusIdentifier varchar(80) default NULL,
		SenderLocationId varchar(80) default NULL,
		DeliveryMessageTrackingId varchar(80) default NULL,
		BusActivityIdentifier varchar(80) default NULL,
		FromGlobalPartnerRoleClassCode varchar(80) default NULL,
		FromGlobalBusServiceCode varchar(80) default NULL,
		InReplyToGlobalBusActionCode varchar(80) default NULL,
		InReplyToMessageStandard varchar(80) default NULL,
		InReplyToStandardVersion varchar(80) default NULL,
		InReplyToVersionIdentifier varchar(80) default NULL,
		ServiceMessageTrackingId varchar(80) default NULL,
		ActionIdentityGlobalBusActionCode varchar(80) default NULL,
		ActionIdentityToMessageStandard varchar(80) default NULL,
		ActionIdentityStandardVersion varchar(80) default NULL,
		ActionIdentityVersionIdentifier varchar(80) default NULL,
		SignalIdentityGlobalBusSignalCode varchar(80) default NULL,
		SignalIdentityVersionIdentifier varchar(80) default NULL,
		ToGlobalPartnerRoleClassCode varchar(80) default NULL,
		ToGlobalBusServiceCode varchar(80) default NULL,
		GlobalUsageCode varchar(80) default NULL,
		PartnerGlobalBusIdentifier varchar(80) default NULL,
		PIPGlobalProcessCode varchar(80) default NULL,
		PIPInstanceIdentifier varchar(200) default NULL,
		PIPVersionIdentifier varchar(80) default NULL,
		ProcessTransactionId varchar(200) default NULL,
		ProcessActionId varchar(240) default NULL,
		FromGlobalPartnerClassCode varchar(80) default NULL,
		ToGlobalPartnerClassCode varchar(80) default NULL,
		NumberOfAttas int default NULL,
		IsSignalDoc tinyint(1) default NULL,
		IsRequestMsg tinyint(1) default NULL,	
		UniqueValue varchar(80) default NULL,	
		AttemptCount int default NULL,		
		MsgDigest varchar(80) default NULL,	
		RNIFVersion varchar(80) default NULL,
		InResponseToActionID varchar(80) default NULL,
		UserTrackingID varchar(80) default NULL,
		PRIMARY KEY (UID)
	) TYPE=MyISAM;

	