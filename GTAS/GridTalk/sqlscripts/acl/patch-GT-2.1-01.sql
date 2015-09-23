# 25 Feb 2004 GT 2.1 [Neo Sok Lay] Add features.
USE userdb;

UPDATE feature
SET Actions=";*;UpdateUserAccount;ChangeAccountPassword;GetUserAccount;GetUserAccountList;"
WHERE UID=-2
;

UPDATE feature
SET Actions=";*;CreateUserAccount;UpdateUserAccount;DeleteUserAccount;GetUserAccount;GetUserAccountList;AddRoleToUser;RemoveRoleFromUser;SetBizEntityListForUser;"
WHERE UID=-3
;

UPDATE feature
SET Actions=";*;CreateRole;UpdateRole;DeleteRole;AddAccessRight;ModifyAccessRight;RemoveAccessRight;"
WHERE UID=-4
;

UPDATE feature SET Description="All Features" WHERE UID=-1;
UPDATE feature SET Description="User Profile Management" WHERE UID=-2;
UPDATE feature SET Description="User Administration" WHERE UID=-3;
UPDATE feature SET Description="Role Management" WHERE UID=-4;

INSERT INTO feature
(UID,Feature,Description,Actions,DataTypes,Version) VALUES
("-5","ENTERPRISE","Enterprise Management",";*;CreateBusinessEntity;DeleteBusinessEntity;UpdateBusinessEntity;SetChannelListForBizEntity;SendBusinessEntity;",";*;","0"),
("-6","PROCESS","Process Management",";*;CancelProcessInstance;CreateProcessDef;DeleteProcessDef;DeleteProcessInstance;UpdateProcessDef;",";*;","0"),
("-7","SYSTEM","System Management",";*;EndConnectionSetup;SetupConnection;UpgradeLicense;ValidateRegistrationInfo;CancelRegistrationInfo;ConfirmRegistrationInfo;SaveNetworkSetting;",";*;","0"),
("-8","GRIDNODE","GridNode Management",";*;SaveMyCompanyProfile;AbortGridNodeActivation;ApproveGridNodeActivation;DenyGridNodeActivation;SubmitGridNodeActivation;SubmitGridNodeDeactivation;",";*;","0"),
("-9","PARTNER","Partner Management",";*;SetBizEntityForPartner;",";*;","0")
;

