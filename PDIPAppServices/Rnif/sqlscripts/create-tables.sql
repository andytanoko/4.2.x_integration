# Change Log
# 16 Jul 2003 GT 2.2 I1 [Neo Sok Lay] Add CanDelete column in process_def.

USE userdb;

	DROP TABLE IF EXISTS process_def;
	CREATE TABLE `process_def` ( 
		`UID` bigint NOT NULL default '0',
		DefName varchar(80) default NULL,
		ActionTimeOut int default NULL,
		ProcessType varchar(80) default NULL,
		RNIFVersion varchar(80) default NULL,
		FromPartnerRoleClassCode varchar(80) default NULL,
		FromBizServiceCode varchar(80) default NULL,
		FromPartnerClassCode varchar(80) default NULL,
		GToPartnerRoleClassCode varchar(80) default NULL,
		GToBizServiceCode varchar(80) default NULL,
		GToPartnerClassCode varchar(80) default NULL,
		GProcessIndicatorCode varchar(80) default NULL,
		VersionIdentifier varchar(80) default NULL,
		GUsageCode varchar(80) default NULL,
		RequestDocThisDocIdentifier varchar(150) default NULL,
		ResponseDocThisDocIdentifier varchar(150) default NULL,
		ResponseDocRequestDocIdentifier varchar(150) default NULL,
		UserTrackingIdentifier varchar(150) default NULL,
                IsSynchronous tinyint(1) default NULL,
    CanDelete tinyint(1) NOT NULL default '1',
		PRIMARY KEY (UID)
	) TYPE=MyISAM;

        DROP TABLE IF EXISTS process_act;
	CREATE TABLE `process_act` ( 
		`UID` bigint NOT NULL default '0',
		ProcessDefUid int default NULL,
		ProcessDefAct int default NULL,
		MsgType bigint default NULL,
		Retries int default NULL,
		TimeToAcknowledge int default NULL,
		IsAuthorizationRequired tinyint(1) default NULL,
		IsNonRepudiationRequired tinyint(1) default NULL,
		IsSecureTransportRequired tinyint(1) default NULL,
		BizActivityIdentifier varchar(80) default NULL,
		GBizActionCode varchar(80) default NULL,
		DictFile bigint default NULL,
		XMLSchema bigint default NULL,
		
		GDigestAlgCode varchar(80) default NULL,
		DisableDTD tinyint(1) default NULL,
		DisableSchema tinyint(1) default NULL,
		DisableEncryption tinyint(1) default NULL,
		DisableSignature tinyint(1) default NULL,
		ValidateAtSender tinyint(1) default NULL,
		OnlyEncryptPayload tinyint(1) default NULL,
		DigestAlgorithm varchar(80) default NULL,
		EncryptionAlgorithm varchar(80) default NULL,
		EncryptionAlgorithmLength int default NULL,		
		IsCompressRequired tinyint(1) default NULL,
		PRIMARY KEY (UID)
	) TYPE=MyISAM;