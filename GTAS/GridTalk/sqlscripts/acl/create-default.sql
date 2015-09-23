# Change History
# 25 Feb 2004 GT 2.1 [Neo Sok Lay] Add more features

USE userdb;

#
# Default Roles
#
DELETE FROM role
WHERE (UID BETWEEN -3 AND -1)
OR (Role IN ("Administrator","User","User Administrator"));
INSERT INTO role
(UID,Role,Description,CanDelete,Version) VALUES
("-1","Administrator","Administrators have complete and unrestricted access to all information.","0","0"),
("-2","User","Users are prevented from accessing or modifying most information except their account profile and preferences.","0","0"),
("-3","User Administrator","User Administrators have complete access rights to manage user accounts.","0","0")
;


#
# Default User Roles
#
DELETE FROM subject_role WHERE (UID BETWEEN -2 AND -1);
INSERT INTO subject_role
(UID,Subject,Role,SubjectType,Version) VALUES
("-1","-1","-1","UserAccount","0"),
("-2","-2","-2","UserAccount","0")
;


#
# Default Feature list
#
DELETE FROM feature WHERE (UID BETWEEN -9 AND -1);
INSERT INTO feature
(UID,Feature,Description,Actions,DataTypes,Version) VALUES
("-1","*","All Features",";*;",";*;","0"),
("-2","USER.PROFILE","User Profile Management",";*;UpdateUserAccount;ChangeAccountPassword;GetUserAccount;GetUserAccountList;",";*;UserAccount;","0"),
("-3","USER.ADMIN","User Administration",";*;CreateUserAccount;UpdateUserAccount;DeleteUserAccount;GetUserAccount;GetUserAccountList;AddRoleToUser;RemoveRoleFromUser;SetBizEntityListForUser;",";*;UserAccount;","0"),
("-4","ROLE","Role Management",";*;CreateRole;UpdateRole;DeleteRole;AddAccessRight;ModifyAccessRight;RemoveAccessRight;",";*;Role;AccessRight;","0"),
("-5","ENTERPRISE","Enterprise Management",";*;CreateBusinessEntity;DeleteBusinessEntity;UpdateBusinessEntity;SetChannelListForBizEntity;SendBusinessEntity;",";*;","0"),
("-6","PROCESS","Process Management",";*;CancelProcessInstance;CreateProcessDef;DeleteProcessDef;DeleteProcessInstance;UpdateProcessDef;",";*;","0"),
("-7","SYSTEM","System Management",";*;EndConnectionSetup;SetupConnection;UpgradeLicense;ValidateRegistrationInfo;CancelRegistrationInfo;ConfirmRegistrationInfo;SaveNetworkSetting;",";*;","0"),
("-8","GRIDNODE","GridNode Management",";*;SaveMyCompanyProfile;AbortGridNodeActivation;ApproveGridNodeActivation;DenyGridNodeActivation;SubmitGridNodeActivation;SubmitGridNodeDeactivation;",";*;","0"),
("-9","PARTNER","Partner Management",";*;SetBizEntityForPartner;",";*;","0")
;


#
# Default Role Access Rights
#
DELETE FROM access_right WHERE (UID BETWEEN -3 AND -1);
INSERT INTO access_right
(UID,RoleUID,Feature,Description,Action,DataType,Criteria,CanDelete,Version) VALUES
("-1","-1","*","*","*","*","","0","0"),
("-2","-2","USER.PROFILE","Access or modify self account profile and preferences","*","*","","0","0"),
("-3","-3","USER.ADMIN","Create, access or modify user account profiles","*","*","","0","0")
;
