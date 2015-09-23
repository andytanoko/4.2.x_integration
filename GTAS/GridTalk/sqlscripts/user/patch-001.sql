# 30 May 2003 I1 v2.1  [Neo Sok Lay] Change Password algo.

USE userdb;

UPDATE user_account
SET Password='1N3BRqvwF7dN'
WHERE ID='admin';

UPDATE user_account
SET Password='1IMDd9CLvmxP'
WHERE ID='guest';

UPDATE user_account
SET Password='hs4Q74gQey8G'
WHERE ID NOT IN('admin','guest');
