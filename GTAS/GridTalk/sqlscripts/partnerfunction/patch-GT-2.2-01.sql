# 23 Jul 2003 GT 2.2 I1 [Neo Sok Lay] Set CanDelete to false for default partner functions

USE userdb;

UPDATE partner_function
SET CanDelete='0'
WHERE PartnerFunctionId IN ("PFIP","PFIB","PFEP","PFOB");
